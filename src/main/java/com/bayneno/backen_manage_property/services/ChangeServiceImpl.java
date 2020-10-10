package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.enums.EStateChangeLog;
import com.bayneno.backen_manage_property.enums.ETypeChangeLog;
import com.bayneno.backen_manage_property.enums.ESubmitTypeChangeLog;
import com.bayneno.backen_manage_property.models.*;
import com.bayneno.backen_manage_property.repository.ChangeLogRepository;
import com.bayneno.backen_manage_property.repository.ListingRepository;
import com.bayneno.backen_manage_property.repository.ProjectRepository;
import com.bayneno.backen_manage_property.repository.UserRepository;
import com.bayneno.backen_manage_property.payload.request.change_log.ApproveReq;
import com.bayneno.backen_manage_property.payload.request.change_log.SubmitReq;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChangeServiceImpl {
    private final ChangeLogRepository changeLogRepository;
    private final ListingRepository listingRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ChangeServiceImpl(ChangeLogRepository changeLogRepository
            , ListingRepository listingRepository
            , ProjectRepository projectRepository
            , UserRepository userRepository) {
        this.changeLogRepository = changeLogRepository;
        this.listingRepository = listingRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public void submit(SubmitReq req){
        Object changeFromValue = null;
        if(req.getSubmitType() == ESubmitTypeChangeLog.EDIT || req.getSubmitType() == ESubmitTypeChangeLog.DELETE) {
            if (req.getType() == ETypeChangeLog.LISTING) {
                if (req.getId() != null) {
                    changeFromValue = listingRepository.findById(req.getId()).orElse(null);
                    if(changeFromValue != null) {
                        Listing toValue = (Listing) req.getToValue();
                        toValue.setCreatedBy(((Listing) changeFromValue).getCreatedBy());
                        toValue.setCreatedDateTime(((Listing) changeFromValue).getCreatedDateTime());
                        req.setToValue(toValue);
                    }
                }

            } else if (req.getType() == ETypeChangeLog.PROJECT) {
                if (req.getId() != null) {
                    changeFromValue = projectRepository.findById(req.getId()).orElse(null);
                    if(changeFromValue != null) {
                        Project toValue = (Project) req.getToValue();
                        toValue.setCreatedBy(((Project)changeFromValue).getCreatedBy());
                        toValue.setCreatedDateTime(((Project)changeFromValue).getCreatedDateTime());
                    }
                }
            }
        }
        User submitUser = userRepository.findByUsername(req.getUsername()).orElse(null);
        changeLogRepository.save(ChangeLog.builder()
                .state(EStateChangeLog.WAIT_APPROVE.name())
                .submitBy(submitUser)
                .typeId(req.getId())
                .type(req.getType())
                .submitType(req.getSubmitType())
                .fromValue(changeFromValue)
                .toValue(req.getToValue())
                .comment(req.getComment())
                .createDate(ZonedDateTimeUtil.now())
                .updateDate(ZonedDateTimeUtil.now())
                .build());
    }

    public void approve(ApproveReq req, String username){
        ChangeLog changeLog = changeLogRepository.findById(req.getChangeLogId()).orElse(null);
        User approveUser = userRepository.findByUsername(username).orElse(null);
        assert changeLog != null;
        if(req.getIsApprove()){
            changeLog.setState(EStateChangeLog.APPROVED.name());
            changeLog.setApproveBy(approveUser);
            if(changeLog.getType() == ETypeChangeLog.LISTING){
                switch (changeLog.getSubmitType()) {
                    case DELETE: listingRepository.delete((Listing) changeLog.getFromValue());
                        break;
                    default: listingRepository.save((Listing) changeLog.getToValue());
                        break;
                }
            } else if(changeLog.getType() == ETypeChangeLog.PROJECT){
                switch (changeLog.getSubmitType()){
                    case DELETE: projectRepository.delete((Project) changeLog.getFromValue());
                        break;
                    default: projectRepository.save((Project) changeLog.getToValue());
                        break;
                }
            }
        } else {
            changeLog.setState(EStateChangeLog.CANCEL.name());
        }
        changeLogRepository.save(changeLog);
    }

    private Map<String, Object> getChangeFromValue(Object changeObject, Set<String> fields){
        return fields.stream().filter(fieldName -> {
            boolean hasField = true;
            try {
                changeObject.getClass().getField(fieldName);
            } catch (NoSuchFieldException e) {
                hasField = false;
            }
            return hasField;
        }).collect(Collectors.toMap(fieldName->fieldName,fieldName->{
            Object object;
            try {
                Field field = changeObject.getClass().getField(fieldName);
                field.setAccessible(true);
                object = field.get(changeObject);
                field.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return object;
        }));
    }

    private Map<String, String> getSubFieldValue(Object object, Set<String> fieldNames){
        final Map<String, String> values = new HashMap<>();
        fieldNames.forEach(fieldName -> {
            try {
                Field field = object.getClass().getField(fieldName);
                field.setAccessible(true);
                values.put(fieldName, (String) field.get(object));
                field.setAccessible(false);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new  RuntimeException(e);
            }
        });
        return values;
    }

    private void setSubFieldValue(Object object, Map<String, String> fieldValues){
        fieldValues.forEach((k,v) -> {
            try {
                Field field = object.getClass().getField(k);
                field.setAccessible(true);
                field.set(object, v);
                field.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
