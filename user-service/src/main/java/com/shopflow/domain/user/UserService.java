package com.shopflow.domain.user;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createUser(UserEntity user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        return this.userRepository.save(user);
    }

    public UserEntity updateUser(java.util.UUID id, UserEntity user) {
        return this.userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setPassword(user.getPassword());
                    return this.userRepository.save(existingUser);
                })
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id));
    }

    public void deleteUser(java.util.UUID id) {
        this.userRepository.deleteById(id);
    }

    public UserEntity findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o email: " + email));
    }

    public List<UserEntity> getAllUsers() {
        return this.userRepository.findAll();
    }
    public UserEntity getUserById(java.util.UUID id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id));
    }
}
