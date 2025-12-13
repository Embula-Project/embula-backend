package com.embula.embula_backend.services.impl;

import com.embula.embula_backend.dto.AdminDTO;
import com.embula.embula_backend.entity.Admin;
import com.embula.embula_backend.repository.AdminRepository;
import com.embula.embula_backend.services.AdminService;
import com.embula.embula_backend.util.mappers.AdminMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceIMPL implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminMappers adminMappers;

    @Override
    public String saveAdmin(AdminDTO adminDTO){
        Admin admin = adminMappers.saveAdmin(adminDTO);
        adminRepository.save(admin);
        return "Admin saved successfully with Id: " + adminDTO.getId();
    }
}

