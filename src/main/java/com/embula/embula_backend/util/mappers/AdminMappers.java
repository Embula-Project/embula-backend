package com.embula.embula_backend.util.mappers;

import com.embula.embula_backend.dto.AdminDTO;
import com.embula.embula_backend.entity.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface AdminMappers {

    Admin saveAdmin(AdminDTO adminDTO);

}

