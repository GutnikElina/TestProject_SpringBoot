package org.innowise.testProject.handler;

import jakarta.validation.ConstraintViolationException;
import org.innowise.testProject.constants.ParamConstant;
import org.innowise.testProject.constants.RedirectConstant;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException ex,
                                                     RedirectAttributes redirectAttributes) {
        List<String> errorMessages = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.toList());

        redirectAttributes.addFlashAttribute(ParamConstant.ERRORS, errorMessages);

        return RedirectConstant.REGISTER;
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex, RedirectAttributes redirectAttributes) {
        List<String> errorMessages = ex.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        redirectAttributes.addFlashAttribute(ParamConstant.ERRORS, errorMessages);

        return RedirectConstant.REGISTER;
    }
}
