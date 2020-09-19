package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.enums.FieldObjectType;
import com.bayneno.backen_manage_property.models.FieldObjectMap;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.OwnerRequest;
import com.bayneno.backen_manage_property.payload.request.RoomRequest;
import com.bayneno.backen_manage_property.payload.request.change_log.ApproveReq;
import com.bayneno.backen_manage_property.payload.response.change_log.ChangeLogDetailShowResponse;
import com.bayneno.backen_manage_property.payload.response.change_log.ChangeLogShowResponse;
import com.bayneno.backen_manage_property.repository.ChangeLogRepository;
import com.bayneno.backen_manage_property.repository.FieldObjectMapRepository;
import com.bayneno.backen_manage_property.services.ChangeServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/change")
public class ChangeController {

    private final ChangeLogRepository changeLogRepository;
    private final ChangeServiceImpl changeService;
    private final FieldObjectMapRepository fieldObjectMapRepository;


    public ChangeController(ChangeLogRepository changeLogRepository
            , ChangeServiceImpl changeService
            , FieldObjectMapRepository fieldObjectMapRepository) {
        this.changeLogRepository = changeLogRepository;
        this.changeService = changeService;
        this.fieldObjectMapRepository = fieldObjectMapRepository;
    }

    @PostMapping("/approve")
    public String approve(@RequestBody ApproveReq req, Principal principal) {
        changeService.approve(req, principal.getName());
        return "SUCCESS";
    }

    @GetMapping("/query")
    public List<ChangeLogShowResponse> query(@RequestParam String state) {
        return changeLogRepository.findAllByState(state).stream().map(changeLog -> {
            ChangeLogShowResponse logShowResponse = new ChangeLogShowResponse();
            Field[] declaredFields = ChangeLogShowResponse.class.getDeclaredFields();
            for (Field field: declaredFields) {
                try {
                    Field logField = changeLog.getClass().getDeclaredField(field.getName());
                    field.setAccessible(true);
                    logField.setAccessible(true);
                    if(User.class.isAssignableFrom(logField.getType())){
                        User user = (User) logField.get(changeLog);
                        if(user != null)
                            field.set(logShowResponse, user.getFirstName() + " " + user.getLastName());
                    } else {
                        field.set(logShowResponse, logField.get(changeLog));
                    }
                    logField.setAccessible(false);
                    field.setAccessible(false);
                }catch (NoSuchFieldException | IllegalAccessException ignored){

                }
            }
            return logShowResponse;
        }).collect(Collectors.toList());
    }

    @GetMapping("/get")
    public List<ChangeLogDetailShowResponse> getChange(@RequestParam String id) {
        final Set<String> excludeField = Stream.of("id", "createdBy", "createdDateTime","updatedBy","updatedDateTime")
                .collect(Collectors.toSet());
        List<ChangeLogDetailShowResponse> changeLogShowResponses = new ArrayList<>();
        changeLogRepository.findById(id).ifPresent(changeLog -> {
            Object toValue = changeLog.getToValue();
            Object fromValue = changeLog.getFromValue() == null ? toValue : changeLog.getFromValue();

            final Map<String, String> thFieldObjectMaps = fieldObjectMapRepository.findAllByType(changeLog.getType())
                    .stream().collect(Collectors.toMap(FieldObjectMap::getFieldName, FieldObjectMap::getTh));

            for (Field oldField : fromValue.getClass().getDeclaredFields()) {
                if(!excludeField.contains(oldField.getName())) {
                    try {
                        Field newField;
                        try {
                            newField = toValue.getClass().getDeclaredField(oldField.getName());
                        } catch (NoSuchFieldException e) {
                            continue;
                        }
                        oldField.setAccessible(true);
                        newField.setAccessible(true);
                        if (OwnerRequest.class.isAssignableFrom(oldField.getType())) {
                            Field[] ownerRequestDeclaredFields = OwnerRequest.class.getDeclaredFields();
                            addFieldValueToShowResponse(thFieldObjectMaps, changeLogShowResponses, fromValue, toValue, oldField, ownerRequestDeclaredFields);
                        } else if (RoomRequest.class.isAssignableFrom(oldField.getType())) {
                            Field[] roomDeclaredFields = RoomRequest.class.getDeclaredFields();
                            addFieldValueToShowResponse(thFieldObjectMaps, changeLogShowResponses, fromValue, toValue, oldField, roomDeclaredFields);
                        } else {
                            changeLogShowResponses.add(ChangeLogDetailShowResponse.builder()
                                    .key(thFieldObjectMaps.getOrDefault(oldField.getName(), oldField.getName()))
                                    .fromValue(toStringEmptyIfNull(oldField.get(fromValue)))
                                    .toValue(toStringEmptyIfNull(oldField.get(toValue)))
                                    .build());
                        }
                        oldField.setAccessible(false);
                        newField.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        return changeLogShowResponses;
    }

    private void addFieldValueToShowResponse(Map<String, String> thFieldObjectMaps
            , List<ChangeLogDetailShowResponse> changeLogShowResponses
            , Object fromValue
            , Object toValue
            , Field oldField
            , Field[] declaredFields) throws IllegalAccessException {
        for(Field field: declaredFields) {
            field.setAccessible(true);
            changeLogShowResponses.add(ChangeLogDetailShowResponse.builder()
                    .key(thFieldObjectMaps.getOrDefault(field.getName(), field.getName()))
                    .fromValue(toStringEmptyIfNull(field.get(oldField.get(fromValue))))
                    .toValue(toStringEmptyIfNull(field.get(oldField.get(toValue))))
                    .build());
            field.setAccessible(false);
        }
    }

    private String toStringEmptyIfNull(Object object) {
        return Optional.ofNullable(object).map(Object::toString).orElse("");
    }
}
