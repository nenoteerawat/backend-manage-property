package com.bayneno.backen_manage_property.repository;

import com.bayneno.backen_manage_property.models.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, String> {
}
