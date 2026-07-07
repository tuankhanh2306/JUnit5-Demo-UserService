package com.example.userservice.unit.service;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class AdvancedTestNGFeaturesTest {

    @BeforeMethod
    public void initEach() {
        System.out.println("Trước mỗi test case: Khởi tạo dữ liệu TestNG...");
    }

    // 1. Dependency Testing (Kiểm thử phụ thuộc)
    // Nếu method test trước bị FAIL, các method phụ thuộc sau sẽ tự động bị SKIP
    @Test
    public void testLogin() {
        System.out.println("Running: Đăng nhập hệ thống");
        // Giả lập login thành công
        Assert.assertTrue(true);
    }

    @Test(dependsOnMethods = {"testLogin"})
    public void testViewDashboard() {
        System.out.println("Running: Xem trang Dashboard (Yêu cầu login thành công)");
        Assert.assertTrue(true);
    }

    // 2. Test Grouping (Phân nhóm kiểm thử)
    // Cho phép chạy riêng nhóm "smoke" hoặc "regression" thông qua cấu hình suite XML
    @Test(groups = {"smoke", "fast"})
    public void testFastSmoke() {
        System.out.println("Running: Smoke test nhanh...");
        Assert.assertTrue(true);
    }

    @Test(groups = {"regression"})
    public void testComplexRegression() {
        System.out.println("Running: Regression test phức tạp...");
        Assert.assertTrue(true);
    }

    // 3. Parallel Execution & Multi-threading trực tiếp tại annotation
    // Chạy test case này 5 lần (invocationCount) trên 3 luồng đồng thời (threadPoolSize)
    @Test(invocationCount = 5, threadPoolSize = 3)
    public void testParallelExecution() throws InterruptedException {
        System.out.println("Running test song song trên Thread: " + Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        Assert.assertTrue(true);
    }

    // 4. DataProvider nâng cao của TestNG
    // Trả về dữ liệu đa dạng hơn, dễ cấu hình hơn JUnit 5
    @DataProvider(name = "userDataProvider")
    public Object[][] provideUsers() {
        return new Object[][] {
            { 1, "Alice", true },
            { 2, "Bob", false },
            { 3, "Charlie", true }
        };
    }

    @Test(dataProvider = "userDataProvider")
    public void testUserStatus(int id, String name, boolean isActive) {
        System.out.println("Checking User - ID: " + id + ", Name: " + name + ", Active: " + isActive);
        Assert.assertNotNull(name);
    }
}
