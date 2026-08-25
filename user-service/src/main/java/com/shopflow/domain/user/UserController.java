package com.shopflow.domain.user;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopflow.domain.user.dto.UserRequestDTO;
import com.shopflow.domain.user.dto.UserResponseDTO;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/users") 
public class UserController {


    private final UserService userService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UserController(UserService userService, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public org.springframework.http.ResponseEntity<?> createUser(@RequestBody @Valid UserRequestDTO data) {
        try {
            String encodedPassword = passwordEncoder.encode(data.password());
            User novoUser = new User(data.name(), data.email(), encodedPassword);
            User usuarioSalvo = this.userService.createUser(novoUser);
            return org.springframework.http.ResponseEntity.ok(new UserResponseDTO(usuarioSalvo.getId(), usuarioSalvo.getName(), usuarioSalvo.getEmail()));
        } catch (IllegalArgumentException e) {
            return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = this.userService.getAllUsers();
        return users.stream().map( user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail())).toList();
    }

   
    
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {
        User user = this.userService.getUserById(id);
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id, @RequestBody @Valid UserRequestDTO data) {
        User updatedData = new User(data.name(), data.email(), data.password());
        User user = this.userService.updateUser(id, updatedData);
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {
        this.userService.deleteUser(id);
    }

    @GetMapping("/email")
    public UserResponseDTO getUserByEmail(@org.springframework.web.bind.annotation.RequestParam String email) {
        User user = this.userService.findByEmail(email);
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    @GetMapping("/{id}/details")
    public UserResponseDTO getUserDetails(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {
        User user = this.userService.getUserById(id);
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }


}
