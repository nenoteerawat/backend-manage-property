package com.nenobay.backend_manage_property.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ResourceController {

    @GetMapping("/api/admin")
    public String admin(){
        return "Admin";
    }
}
