package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.SignupRequest;
import com.bayneno.backen_manage_property.repository.RoleRepository;
import com.bayneno.backen_manage_property.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/user/edit")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> userEdit(@RequestBody SignupRequest signupRequest, HttpServletRequest request, Principal principal) {

        Optional<User> user = userRepository.findByUsername(signupRequest.getUsername());
        if(user.isPresent()) {
            user.get().setFirstName(signupRequest.getFirstName());
            user.get().setLastName(signupRequest.getLastName());
            user.get().setNickName(signupRequest.getNickName());
            user.get().setPassword(signupRequest.getPassword());
            user.get().setEmail(signupRequest.getEmail());
            user.get().setTeam(signupRequest.getTeam());
            user.get().setSubZone(signupRequest.getSubZone());
            user.get().setSubZoneTags(signupRequest.getSubZoneTags());
            userRepository.save(user.get());
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/user/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> userEdit(@RequestParam String id) {

        Optional<User> user = userRepository.findById(id);
        userRepository.delete(user.get());
        return ResponseEntity.ok("delete success");
    }

    @PostMapping("/user/list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> projectList(@RequestBody SignupRequest signupRequest) {

        List<User> users = new ArrayList<>();
        if(signupRequest.getUsername() != null) {
            Optional<User> user = userRepository.findByUsername(signupRequest.getUsername());
            users.add(user.get());
        } else {
            users = userRepository.findAll();
        }
        users = users.stream().filter(user -> !user.getUsername().equals("manager") && !user.getUsername().equals("admin")).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
