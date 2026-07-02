# 🚀 Dự Án Demo So Sánh Kiểm Thử: JUnit 5 vs TestNG (Spring Boot & H2 DB)

Dự án này là một ứng dụng Spring Boot nhỏ gọn về quản lý người dùng (User Service) được thiết kế đặc biệt để thực hành và thuyết trình về kiểm thử phần mềm. Dự án tập trung demo các kịch bản kiểm thử đơn vị (Unit Test) và kiểm thử tích hợp (Integration Test) sử dụng đồng thời hai thư viện kiểm thử phổ biến nhất hiện nay: **JUnit 5** và **TestNG**.

---

## 🛠️ Công Nghệ Sử Dụng
*   **Java 21** & **Spring Boot 3.3.1**
*   **Spring Data JPA** & **H2 In-Memory Database** (Cơ sở dữ liệu lưu trên RAM phục vụ test nhanh)
*   **Lombok** (Giúp tinh gọn mã nguồn, tự động sinh Getters/Setters/Constructors)
*   **Mockito** (Thư viện tạo đối tượng giả lập - Mock/Stub phục vụ Unit Test độc lập)
*   **JUnit 5 (JUnit Jupiter)** & **TestNG 7.8.0**

---

## 📂 Cấu Trúc Thư Mục Dự Án
```text
e:\JUnit5
├── src
│   ├── main
│   │   ├── java/com/example/userservice
│   │   │   ├── config/DataInitializer.java      <-- Tự động nạp dữ liệu mẫu khi chạy app
│   │   │   ├── controller/UserController.java    <-- REST API endpoints (/api/users)
│   │   │   ├── model/User.java & UserDto.java   <-- Thực thể Entity và DTO trao đổi dữ liệu
│   │   │   ├── repository/UserRepository.java   <-- Kết nối cơ sở dữ liệu H2
│   │   │   └── services/UserService.java        <-- Chứa logic nghiệp vụ chính (nơi được test)
│   │   └── resources
│   │       └── application.yml                  <-- Cấu hình H2 DB & Console
│   └── test
│       ├── java/com/example/userservice
│       │   ├── unit/service
│       │   │   ├── UserServiceTest.java         <-- Kiểm thử JUnit 5 (5 tiêu chí cốt lõi)
│       │   │   ├── UserServiceTestNGTest.java   <-- Kiểm thử TestNG (Đối chiếu tương đương)
│       │   │   └── AdvancedJUnit5FeaturesTest.java <-- Demo JUnit 5 nâng cao (Parallel, Nested...)
│       │   └── UserServiceApplicationTests.java <-- Integration Test tích hợp cơ sở dữ liệu thật H2
│       └── resources
│           └── junit-platform.properties        <-- Cấu hình chạy song song đa luồng cho JUnit 5
├── pom.xml                                      <-- Quản lý dependencies & Build (Maven)
└── mvnw & mvnw.cmd                              <-- Maven Wrapper chạy không cần cài sẵn Maven
```

---

## ⚡ Hướng Dẫn Chạy Ứng Dụng (Web API)
Khi chạy ứng dụng, hệ thống sẽ tự động khởi chạy Web Server tại cổng `8080` và nạp sẵn 3 người dùng mẫu vào cơ sở dữ liệu H2 (nhờ tệp `DataInitializer`).

1.  Mở dự án trên **IntelliJ IDEA**.
2.  Mở file `UserServiceApplication.java` và nhấn nút **Run** (hoặc tổ hợp phím `Shift + F10`).
3.  **Kiểm tra dữ liệu**:
    *   Truy cập APIs xem danh sách JSON: [http://localhost:8080/api/users](http://localhost:8080/api/users)
    *   Truy cập H2 Console quản trị DB: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:userdb`, Username: `sa`, Password: `password`).

---

## 🧪 Hướng Dẫn Chạy Kiểm Thử (Testing)

### 1. Chạy trên giao diện IntelliJ IDEA (Khuyên dùng khi thuyết trình)
*   **JUnit 5**: Nhấp chuột phải vào file `UserServiceTest.java` hoặc `AdvancedJUnit5FeaturesTest.java` và chọn **Run**.
*   **TestNG**: Nhấp chuột phải vào file `UserServiceTestNGTest.java` và chọn **Run**.

### 2. Chạy qua dòng lệnh (Terminal)
Nếu chạy qua Terminal (PowerShell), bạn cần gán biến môi trường `JAVA_HOME` tạm thời như sau:

*   **Chạy các kiểm thử JUnit 5**:
    ```powershell
    $env:JAVA_HOME="C:\Users\ASUS\.jdks\openjdk-23.0.2"
    ./mvnw test -Dtest=UserServiceTest
    ```
*   **Chạy các kiểm thử TestNG**:
    ```powershell
    $env:JAVA_HOME="C:\Users\ASUS\.jdks\openjdk-23.0.2"
    ./mvnw test -Dtest=UserServiceTestNGTest
    ```
*   **Chạy kiểm thử đa luồng (song song)**:
    ```powershell
    $env:JAVA_HOME="C:\Users\ASUS\.jdks\openjdk-23.0.2"
    ./mvnw test -Dtest=AdvancedJUnit5FeaturesTest
    ```

---

## 📊 Bảng Đối Chiếu Tính Năng Thuyết Trình

| Tính năng | JUnit 5 | TestNG |
| :--- | :--- | :--- |
| **Cú pháp chạy** | `@BeforeEach` / `@AfterEach` | `@BeforeMethod` / `@AfterMethod` |
| **Thứ tự Assert** | `assertEquals(expected, actual)` | `Assert.assertEquals(actual, expected)` |
| **Tham số hóa** | `@ParameterizedTest` + `@ValueSource` | `@DataProvider` trả về mảng 2 chiều |
| **Chạy song song** | Cấu hình qua file Properties | Cấu hình qua tệp tin `testng.xml` |
| **Độ phụ thuộc** | Các test độc lập tuyệt đối | Hỗ trợ thuộc tính `dependsOnMethods` |
