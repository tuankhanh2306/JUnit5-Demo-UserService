package com.example.userservice.services;

import com.example.userservice.model.User;
import com.example.userservice.model.UserDto;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    public User createUser(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().trim().length() < 3) {
            throw new IllegalArgumentException("Name must be at least 3 characters long");
        }

        if (userDto.getEmail() == null || !userDto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        userRepository.findByEmail(userDto.getEmail()).ifPresent(u -> {
            throw new IllegalStateException("Email already taken: " + userDto.getEmail());
        });

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setKeycloakId(userDto.getKeycloakId());

        return userRepository.save(user);
    }

    public User updateUser(Long id, UserDto userDto) {
        User user = getUserById(id);

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            if (!userDto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new IllegalArgumentException("Invalid email format");
            }
            userRepository.findByEmail(userDto.getEmail()).ifPresent(u -> {
                throw new IllegalStateException("Email already taken: " + userDto.getEmail());
            });
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getKeycloakId() != null) {
            user.setKeycloakId(userDto.getKeycloakId());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
