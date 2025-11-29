package com.embula.embula_backend.util.mappers;

import com.embula.embula_backend.dto.UserDTO;
import com.embula.embula_backend.entity.User;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Configuration;

@Mapper(componentModel="spring")
public interface UserMappers {

    User userDTOToUser(UserDTO userDTO);
}
