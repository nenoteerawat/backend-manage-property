package com.bayneno.backen_manage_property.security.services;

import com.bayneno.backen_manage_property.models.*;
import com.bayneno.backen_manage_property.repository.ChangeLogRepository;
import com.bayneno.backen_manage_property.repository.LeadRepository;
import com.bayneno.backen_manage_property.repository.ProjectRepository;
import com.bayneno.backen_manage_property.requests.change_log.ApproveReq;
import com.bayneno.backen_manage_property.requests.change_log.SubmitReq;
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
    private final LeadRepository leadRepository;
    private final ProjectRepository projectRepository;

    public ChangeServiceImpl(ChangeLogRepository changeLogRepository
            , LeadRepository leadRepository
            , ProjectRepository projectRepository) {
        this.changeLogRepository = changeLogRepository;
        this.leadRepository = leadRepository;
        this.projectRepository = projectRepository;
    }

    public void submit(SubmitReq req){
        Object changeFromValue = null;
        if(req.getSubmitType().equals(ChangeSubmitType.EDIT.name())) {
            if (req.getType().equals(ChangeLogType.LEAD.name())) {
                if (req.getId() != null) {
                    changeFromValue = leadRepository.findById(req.getId()).orElse(null);
                }

            } else if (req.getType().equals(ChangeLogType.PROJECT.name())) {
                if (req.getId() != null) {
                    changeFromValue = projectRepository.findById(req.getId()).orElse(null);
                }
            }
        }
        changeLogRepository.save(ChangeLog.builder()
                .state(ChangeLogState.WAIT_APPROVE.name())
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

    public void approve(ApproveReq req){
        ChangeLog changeLog = changeLogRepository.findById(req.getChangeLogId()).orElse(null);
        assert changeLog != null;
        if(req.isApprove()){
            changeLog.setState(ChangeLogState.APPROVED.name());
            if(changeLog.getType().equals(ChangeLogType.LEAD.name())){

            } else if(changeLog.getType().equals(ChangeLogType.PROJECT.name())){

            }
        } else {
            changeLog.setState(ChangeLogState.CANCEL.name());
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
