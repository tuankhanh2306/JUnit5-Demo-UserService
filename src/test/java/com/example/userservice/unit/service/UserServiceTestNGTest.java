package com.example.userservice.unit.service;

import com.example.userservice.model.User;
import com.example.userservice.model.UserDto;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.services.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserServiceTestNGTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;
    private AutoCloseable closeable;

    // 1. Lifecycle Hook in TestNG: @BeforeMethod (Tương đương @BeforeEach của JUnit 5)
    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this); // Khởi tạo Mockito cho TestNG
        mockUser = new User(1L, "Khanh", "khanhcute@gmail.com", "k-123");
    }

    @AfterMethod
    public void tearDown() throws Exception {
        closeable.close(); // Giải phóng tài nguyên Mockito
    }

    // 2. Test thông thường trong TestNG
    @Test
    public void interface_getAllUsers_shouldCallFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(mockUser));
        userService.getAllUsers();
        verify(userRepository, times(1)).findAll();
    }

    // So sánh thứ tự Assert: TestNG nhận (actual, expected) ngược lại so với JUnit 5 (expected, actual)
    @Test
    public void localData_getAllUsers_shouldReturnCorrectData() {
        when(userRepository.findAll()).thenReturn(List.of(mockUser));
        List<User> result = userService.getAllUsers();

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getName(), "Khanh");
        Assert.assertEquals(result.get(0).getEmail(), "khanhcute@gmail.com");
    }

    // 3. Parameterized Test trong TestNG sử dụng @DataProvider (Điểm khác biệt lớn so với JUnit 5)
    @DataProvider(name = "invalidEmails")
    public Object[][] invalidEmailProvider() {
        return new Object[][] {
            {"invalidemail"},
            {"test@"},
            {"@gmail.com"},
            {""},
            {"   "}
        };
    }

    @Test(dataProvider = "invalidEmails")
    public void boundary_createUser_invalidEmail_shouldThrow(String invalidEmail) {
        UserDto dto = new UserDto("Khanh", invalidEmail, "k-123");

        // Sử dụng Assert.assertThrows của TestNG
        Assert.assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));
    }

    // 4. Test độc lập kịch bản rẽ nhánh
    @Test
    public void independentPaths_getUserById_foundAndNotFound() {
        // Path A: Tìm thấy
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        User found = userService.getUserById(1L);
        Assert.assertEquals(found.getName(), "Khanh");

        // Path B: Không tìm thấy (Sử dụng cách thức bắt Exception của TestNG)
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        Assert.assertThrows(IllegalArgumentException.class, () -> userService.getUserById(99L));
    }
}
