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
        Assert.assertTrue(false);
    }

    @Test(dependsOnMethods = { "testLogin" })
    public void testViewDashboard() {
        System.out.println("Running: Xem trang Dashboard (Yêu cầu login thành công)");
        Assert.assertTrue(true);
    }

    // 2. Test Grouping (Phân nhóm kiểm thử)
    // Cho phép chạy riêng nhóm "smoke" hoặc "regression" thông qua cấu hình suite
    // XML
    @Test(groups = { "smoke", "fast" })
    public void testFastSmoke() {
        System.out.println("Running: Smoke test nhanh...");
        Assert.assertTrue(true);
    }

    @Test(groups = { "regression" })
    public void testComplexRegression() {
        System.out.println("Running: Regression test phức tạp...");
        Assert.assertTrue(true);
    }

    // 3. Parallel Execution & Multi-threading trực tiếp tại annotation
    // Giả lập hệ thống đặt vé tàu/vé phim: 3 khách hàng cùng nhấn đặt ghế A1 tại
    // một thời điểm
    private final java.util.concurrent.atomic.AtomicBoolean seatA1Booked = new java.util.concurrent.atomic.AtomicBoolean(
            false);
    private final java.util.concurrent.atomic.AtomicInteger bookingSuccessCount = new java.util.concurrent.atomic.AtomicInteger(
            0);
    private final java.util.concurrent.atomic.AtomicInteger bookingFailCount = new java.util.concurrent.atomic.AtomicInteger(
            0);

    // Chạy test case này 3 lần (invocationCount) trên 3 luồng đồng thời
    // (threadPoolSize)
    @Test(invocationCount = 3, threadPoolSize = 3)
    public void testTicketBookingConcurrency() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        System.out.println("Khách hàng trên Thread [" + threadName + "] bắt đầu nhấn nút ĐẶT GHẾ A1...");

        // Giả lập độ trễ mạng cực nhỏ để cả 3 luồng cùng hội tụ tại thời điểm đặt
        TimeUnit.MILLISECONDS.sleep(100);

        // Sử dụng compareAndSet (Atomic) để đảm bảo chỉ có 1 luồng duy nhất đổi trạng
        // thái ghế từ false -> true thành công
        if (seatA1Booked.compareAndSet(false, true)) {
            bookingSuccessCount.incrementAndGet();
            System.out.println(" -> KẾT QUẢ: Thread [" + threadName + "] đặt ghế A1 THÀNH CÔNG!");
        } else {
            bookingFailCount.incrementAndGet();
            System.out.println(" -> KẾT QUẢ: Thread [" + threadName + "] đặt ghế A1 THẤT BẠI (Ghế đã được đặt trước)!");
        }
    }

    // Kiểm tra xem kết quả đặt vé cuối cùng có đúng luật: chỉ 1 thành công và 2
    // thất bại hay không
    @Test(dependsOnMethods = { "testTicketBookingConcurrency" })
    public void verifyBookingResult() {
        System.out.println("=== KIỂM TRA KẾT QUẢ ĐẶT VÉ CUỐI CÙNG ===");
        System.out.println("Tổng số khách đặt thành công: " + bookingSuccessCount.get());
        System.out.println("Tổng số khách đặt thất bại: " + bookingFailCount.get());

        Assert.assertEquals(bookingSuccessCount.get(), 1, "Chỉ được phép có duy nhất 1 khách đặt thành công!");
        Assert.assertEquals(bookingFailCount.get(), 2, "Phải có đúng 2 khách đặt thất bại!");
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
