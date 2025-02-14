package org.innowise.testProject.mapper.userMapper;

import org.innowise.testProject.dto.userDTO.UserResponseDTO;
import org.innowise.testProject.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserResponseMapper {
    UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);

    UserResponseDTO toDTO(User user);
    User toEntity(UserResponseDTO userResponseDTO);
}
