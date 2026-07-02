package com.example.userservice.unit.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Advanced JUnit 5 Features Demo")
class AdvancedJUnit5FeaturesTest {

    // 1. Lifecycle Hooks (@BeforeEach & @AfterEach)
    @BeforeEach
    void initEach() {
        System.out.println("Trước mỗi test case: Dọn dẹp tài nguyên hoặc chuẩn bị Mock...");
    }

    @AfterEach
    void tearDownEach() {
        System.out.println("Sau mỗi test case: Giải phóng tài nguyên hoặc reset Mock...");
    }

    // 2. Parameterized Test (Kiểm thử tham số hóa)
    @ParameterizedTest
    @ValueSource(strings = {"JUnit5", "TestNG", "Mockito"})
    @DisplayName("Parameterized Test - Chạy 1 hàm test với nhiều chuỗi khác nhau")
    void testWithStringInputs(String input) {
        System.out.println("Testing input: " + input);
        assertTrue(input.length() > 0);
    }

    // 3. Nested Test (Kiểm thử lồng nhau)
    // Giúp phân cấp kiểm thử theo nhóm tính năng cực kỳ logic và dễ nhìn trên IDE
    @Nested
    @DisplayName("Nhóm tính năng: Quản lý giỏ hàng")
    class CartFeatures {

        @Test
        @DisplayName("Test thêm sản phẩm vào giỏ")
        void testAddProduct() {
            System.out.println("Running: Test thêm sản phẩm");
            assertTrue(true);
        }

        @Test
        @DisplayName("Test xóa sản phẩm khỏi giỏ")
        void testRemoveProduct() {
            System.out.println("Running: Test xóa sản phẩm");
            assertTrue(true);
        }
    }

    // 4. Chạy song song (Parallel Execution)
    // Nhóm test này giả lập chạy tốn thời gian. Khi kích hoạt chế độ song song, 
    // chúng sẽ chạy đồng thời giúp tiết kiệm thời gian đáng kể.
    @Nested
    @DisplayName("Nhóm tính năng: Chạy song song")
    class ParallelTests {

        @Test
        @DisplayName("Test song song 1 (giả lập tốn 2 giây)")
        void parallelTest1() throws InterruptedException {
            System.out.println("Test 1 bắt đầu chạy trên Thread: " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(2);
            System.out.println("Test 1 kết thúc");
            assertTrue(true);
        }

        @Test
        @DisplayName("Test song song 2 (giả lập tốn 2 giây)")
        void parallelTest2() throws InterruptedException {
            System.out.println("Test 2 bắt đầu chạy trên Thread: " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(2);
            System.out.println("Test 2 kết thúc");
            assertTrue(true);
        }
    }
}
