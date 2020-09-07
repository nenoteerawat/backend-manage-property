package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.models.Room;
import com.bayneno.backen_manage_property.repository.RoomRepository;
import com.bayneno.backen_manage_property.requests.room.AddReq;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomRepository repository;

    public RoomController(RoomRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/add")
    public Room add(@RequestBody AddReq req, Principal principal) {
        Room room = Room.builder()
                .name(req.getName())
                .price(req.getPrice())
                .build();
        return repository.save(room);
    }
}
