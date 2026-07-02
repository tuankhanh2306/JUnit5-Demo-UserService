package com.example.userservice.config;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        userRepository.deleteAll();

        // Tạo dữ liệu mẫu khởi tạo lúc chạy ứng dụng
        User user1 = new User(null, "Khanh", "khanhcute@gmail.com", "k-123");
        User user2 = new User(null, "Holy", "holydev@gmail.com", "kc-123");
        User user3 = new User(null, "Alex", "alex@gmail.com", "a-123");

        userRepository.saveAll(List.of(user1, user2, user3));

        System.out.println("--------------------------------------------------");
        System.out.println("Dữ liệu khởi tạo thành công (H2 In-Memory DB):");
        userRepository.findAll().forEach(user -> 
            System.out.println(" - ID: " + user.getId() + " | Tên: " + user.getName() + " | Email: " + user.getEmail())
        );
        System.out.println("--------------------------------------------------");
    }
}
