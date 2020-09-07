package com.bayneno.backen_manage_property.security.services;

import com.bayneno.backen_manage_property.models.ChangeLog;
import com.bayneno.backen_manage_property.models.ChangeLogState;
import com.bayneno.backen_manage_property.repository.ChangeLogRepository;
import com.bayneno.backen_manage_property.repository.RoomRepository;
import com.bayneno.backen_manage_property.requests.change_log.ApproveReq;
import com.bayneno.backen_manage_property.requests.change_log.SubmitReq;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ChangeServiceImpl {
    private RoomRepository roomRepo;
    private ChangeLogRepository changeLogRepo;

    public ChangeServiceImpl(RoomRepository roomRepo, ChangeLogRepository changeLogRepo) {
        this.roomRepo = roomRepo;
        this.changeLogRepo = changeLogRepo;
    }

    public void submit(SubmitReq req, Principal principal){
        roomRepo.findById(req.getRoom()).ifPresent(room -> {
            ChangeLog changeLog = ChangeLog.builder()
                    .room(room)
                    .comment(req.getComment())
                    .toValue(req.getToValue())
                    .state(ChangeLogState.WAIT_APPROVE.name())
                    .fromValue(getSubFieldValue(room, req.getToValue().keySet()))
                    .createDate(ZonedDateTimeUtil.now())
                    .updateDate(ZonedDateTimeUtil.now())
                    .build();
            changeLogRepo.save(changeLog);
        });
    }

    public void approve(ApproveReq req, Principal principal){
        changeLogRepo.findById(req.getChangeLog()).ifPresent(changeLog -> {
            roomRepo.findById(changeLog.getRoom().getId()).ifPresent(room -> {
                changeLog.setState(ChangeLogState.APPROVED.name());
                changeLog.setUpdateDate(ZonedDateTimeUtil.now());
                changeLog.setApproveDate(ZonedDateTimeUtil.now());
                changeLogRepo.save(changeLog);
                setSubFieldValue(room, changeLog.getToValue());
                roomRepo.save(room);
            });
        });
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
