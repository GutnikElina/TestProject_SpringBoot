package org.innowise.testProject.mapper.userMapper;

import org.innowise.testProject.dto.userDTO.UserCreateDTO;
import org.innowise.testProject.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserCreateMapper {
    UserCreateMapper INSTANCE = Mappers.getMapper(UserCreateMapper.class);

    User toEntity(UserCreateDTO userCreateDTO);
}
