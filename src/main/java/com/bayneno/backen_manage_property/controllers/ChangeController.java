package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.models.ChangeLog;
import com.bayneno.backen_manage_property.payload.response.ChangeLogShowResponse;
import com.bayneno.backen_manage_property.repository.ChangeLogRepository;
import com.bayneno.backen_manage_property.requests.change_log.ApproveReq;
import com.bayneno.backen_manage_property.requests.change_log.SubmitReq;
import com.bayneno.backen_manage_property.security.services.ChangeServiceImpl;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public String submit(@RequestBody SubmitReq req) {
        changeService.submit(req);
        return "SUCCESS";
    }

    @PostMapping("/approve")
    public String approve(@RequestBody ApproveReq req) {
        changeService.approve(req);
        return "SUCCESS";
    }

    @GetMapping("/query")
    public List<ChangeLog> query(@RequestParam String state) {
        return repository.findAllByState(state);
    }

    @GetMapping("/get")
    public List<ChangeLogShowResponse> getChange(@RequestParam String id) {
        Optional<ChangeLog> changeLog = repository.findById(id);
        Object fromValue = changeLog.get().getFromValue();
        Object toValue = changeLog.get().getToValue();
        List<ChangeLogShowResponse> changeLogShowResponses = new ArrayList<>();

        for (Field oldField : fromValue.getClass().getDeclaredFields()) {
            try {
                Field newField = null;
                try {
                    newField = toValue.getClass().getDeclaredField(oldField.getName());
                } catch (NoSuchFieldException e) {
                    continue;
                }
                oldField.setAccessible(true);
                newField.setAccessible(true);
                changeLogShowResponses.add(ChangeLogShowResponse.builder()
                        .key(oldField.getName())
                        .fromValue(Optional.ofNullable(oldField.get(fromValue)).map(Object::toString).orElse(""))
                        .toValue(Optional.ofNullable(oldField.get(toValue)).map(Object::toString).orElse(""))
                        .build());
                oldField.setAccessible(false);
                newField.setAccessible(false);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return changeLogShowResponses;
    }
}
