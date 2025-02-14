package org.innowise.testProject.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.innowise.testProject.constants.PageConstant;
import org.innowise.testProject.constants.ParamConstant;
import org.innowise.testProject.constants.RedirectConstant;
import org.innowise.testProject.dto.userDTO.UserAuthDTO;
import org.innowise.testProject.dto.userDTO.UserCreateDTO;
import org.innowise.testProject.dto.userDTO.UserResponseDTO;
import org.innowise.testProject.handler.ErrorHandler;
import org.innowise.testProject.security.AuthenticationService;
import org.innowise.testProject.security.JwtAuthenticationResponse;
import org.innowise.testProject.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping("/")
    public String homePage() {
        return PageConstant.HOME;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute(ParamConstant.USER, new UserCreateDTO());
        model.addAttribute("errors", model.asMap().get("errors"));
        return PageConstant.REGISTER;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserCreateDTO userCreateDTO, RedirectAttributes redirectAttributes) {
        try {
            log.info("Registering user with username {}", userCreateDTO.getUsername());
            authenticationService.signUp(userCreateDTO);
            return RedirectConstant.LOGIN;
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            redirectAttributes.addFlashAttribute(ParamConstant.ERROR, errorMessage);
            return RedirectConstant.REGISTER;
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return PageConstant.LOGIN;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserAuthDTO userAuthDTO, HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {
        try {
        JwtAuthenticationResponse authResponse = authenticationService.signIn(userAuthDTO);
        ResponseCookie jwtCookie = ResponseCookie.from(ParamConstant.JWT_TOKEN, authResponse.getToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        return RedirectConstant.PROFILE;
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            redirectAttributes.addFlashAttribute(ParamConstant.ERROR, errorMessage);
            return RedirectConstant.LOGIN;
        }
    }

    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        UserResponseDTO userResponseDTO = userService.findByUsername(principal.getName());
        model.addAttribute(ParamConstant.USER, userResponseDTO);
        return PageConstant.PROFILE;
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute UserCreateDTO userCreateDTO, Principal principal,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfile(principal.getName(), userCreateDTO);
            redirectAttributes.addFlashAttribute(ParamConstant.MESSAGE, "Profile updated successfully.");
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            redirectAttributes.addFlashAttribute(ParamConstant.ERROR, errorMessage);
        }
        return RedirectConstant.PROFILE;
    }

    @RequestMapping("/refresh")
    public JwtAuthenticationResponse refreshToken(@RequestParam String refreshToken) {
        return authenticationService.refreshToken(refreshToken);
    }

}