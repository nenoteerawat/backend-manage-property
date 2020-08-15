package com.nenobay.backend_manage_property.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin
public class ResourceController {

    @GetMapping("/api/admin")
    public String admin(){
        return "Admin";
    }

    @PostMapping("/api/sale")
    public @ResponseBody SaleModel sale(@RequestBody SaleModel sale){
        return sale;
    }
}
