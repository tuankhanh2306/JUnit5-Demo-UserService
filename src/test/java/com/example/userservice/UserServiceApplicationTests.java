package com.example.userservice;

import com.example.userservice.model.User;
import com.example.userservice.model.UserDto;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SpringBootTest
@Execution(ExecutionMode.SAME_THREAD)
class UserServiceApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clearDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        // Kiểm tra xem Spring Application Context có khởi động thành công không
    }

    @Test
    void testCreateAndFetchUser_RealDatabaseFlow() {
        // Given
        UserDto newUser = new UserDto("Holy", "holydev@gmail.com", "kc-123");

        // When (Lưu vào database H2 thật)
        User savedUser = userService.createUser(newUser);

        // Then
        assertNotNull(savedUser.getId(), "ID phải được tự động sinh ra bởi Database");
        assertEquals("Holy", savedUser.getName());
        assertEquals("holydev@gmail.com", savedUser.getEmail());

        // Xác minh truy vấn trực tiếp từ database
        List<User> allUsers = userService.getAllUsers();
        assertEquals(1, allUsers.size());
        assertEquals("Holy", allUsers.get(0).getName());
    }

    @Test
    void testCreateDuplicateEmail_ShouldThrowException() {
        // Given
        UserDto user1 = new UserDto("Holy", "holydev@gmail.com", "kc-121");
        userService.createUser(user1);

        UserDto user2 = new UserDto("Other", "holydev@gmail.com", "kc-122");

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            userService.createUser(user2);
        });
    }
}
