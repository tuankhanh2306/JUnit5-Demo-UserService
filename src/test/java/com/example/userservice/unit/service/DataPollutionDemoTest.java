package com.example.userservice.unit.service;

import com.example.userservice.model.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Demo Lỗi Nhiễm Bẩn Dữ Liệu (Thiếu @BeforeEach)")
@TestMethodOrder(MethodOrderer.MethodName.class)
class DataPollutionDemoTest {

    // Biến static dùng chung giữa các test case để lưu trạng thái
    private static User sharedUser = new User(1L, "Khanh", "khanhcute@gmail.com", "k-123");

    @Test
    @DisplayName("Bước 1: Sửa đổi tên người dùng thành Tuan")
    void test1_updateUserName() {

        sharedUser.setName("Tuan");
        assertEquals("Tuan", sharedUser.getName());
        System.out.println("Test 1 kết thúc -> PASS thành công!");
    }

    @Test
    @DisplayName("Bước 2: Kiểm tra tên mặc định (Mong đợi: Khanh)")
    void test2_checkDefaultName() {

        System.out.println("Thực tế tên hiện tại của sharedUser trong bộ nhớ: " + sharedUser.getName());

        assertEquals("Khanh", sharedUser.getName());
    }
}
