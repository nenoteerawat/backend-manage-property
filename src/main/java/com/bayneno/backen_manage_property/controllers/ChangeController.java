package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.models.ChangeLog;
import com.bayneno.backen_manage_property.repository.ChangeLogRepository;
import com.bayneno.backen_manage_property.requests.change_log.ApproveReq;
import com.bayneno.backen_manage_property.requests.change_log.SubmitReq;
import com.bayneno.backen_manage_property.security.services.ChangeServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/change")
public class ChangeController {

    private final ChangeLogRepository repository;
    private final ChangeServiceImpl changeService;

    public ChangeController(ChangeLogRepository repository, ChangeServiceImpl changeService) {
        this.repository = repository;
        this.changeService = changeService;
    }

    @PostMapping("/submit")
    public String submit(@RequestBody SubmitReq req, Principal principal) {
        changeService.submit(req, principal);
        return "SUCCESS";
    }

    @PostMapping("/approve")
    public String approve(@RequestBody ApproveReq req, Principal principal) {
        changeService.approve(req, principal);
        return "SUCCESS";
    }

    @GetMapping("/query")
    public List<ChangeLog> query(@RequestParam String state, Principal principal) {
        return repository.findAllByState(state);
    }
}
