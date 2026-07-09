package com.example.userservice.unit.service;

import com.example.userservice.model.User;
import com.example.userservice.model.UserDto;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.services.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test - UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository; // STUB

    @InjectMocks
    private UserService userService; // MODULE

     private User mockUser;

     @BeforeEach
     void setUp() {
     mockUser = new User(1L, "Khanh", "khanhcute@gmail.com", "k-123");
     }

    @Test
    @DisplayName("1. Interface — getAllUsers() phải gọi findAll() đúng 1 lần")
    void interface_getAllUsers_shouldCallFindAll() {
        // given phase
        when(userRepository.findAll()).thenReturn(List.of(mockUser));
        // when phase
        userService.getAllUsers();
        // and phase
        verify(userRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("2. Local Data Structures — getAllUsers() trả về đúng dữ liệu")
    void localData_getAllUsers_shouldReturnCorrectData() {
        // given
        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        // when phase
        List<User> result = userService.getAllUsers();
        // and phases
        assertEquals(1, result.size());
        assertEquals("Khanh", result.get(0).getName());
        assertEquals("khanhcute@gmail.com", result.get(0).getEmail());
        assertEquals("k-123", result.get(0).getKeycloakId());

    }

    @ParameterizedTest
    @ValueSource(strings = { "invalidemail", "test@", "@gmail.com", "", "   " })
    @DisplayName("3. Boundary Conditions — Email sai định dạng biên → exception")
    void boundary_createUser_invalidEmail_shouldThrow(String invalidEmail) {
        UserDto dto = new UserDto("Khanh", invalidEmail, "k-123");

        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(dto));
    }

    @Test
    @DisplayName("4. Independent Paths — getUserById() tìm thấy vs không tìm thấy")
    void independentPaths_getUserById_foundAndNotFound() {
        // Path A: tìm thấy
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        User found = userService.getUserById(1L);
        assertEquals("Khanh", found.getName());

        // Path B: không tìm thấy
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> userService.getUserById(99L));
    }

    // ──────────────────────────────────────────────────────────────
    // 5. ERROR HANDLING PATHS — Khi lỗi, save() KHÔNG được gọi
    // ──────────────────────────────────────────────────────────────
    @Test
    @DisplayName("5. Error Handling — Email trùng → exception, save() không được gọi")
    void errorHandling_createUser_duplicateEmail_shouldNotSave() {
        UserDto dto = new UserDto("Khanh", "khanhcute@gmail.com", "k-123");
        when(userRepository.findByEmail("khanhcute@gmail.com"))
                .thenReturn(Optional.of(mockUser)); // email đã tồn tại

        assertThrows(IllegalStateException.class,
                () -> userService.createUser(dto));

        verify(userRepository, never()).save(any()); // save KHÔNG được gọi
    }



}