package com.HomeAgent.Services;

import com.HomeAgent.DTO.UserDTO;
import com.HomeAgent.Model.User;
import com.HomeAgent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 🔐 REQUIRED

    // ✅ REGISTER USER (EMAIL + PASSWORD)
    public void addUser(UserDTO userDTO) {

        // check if email already exists
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setCreatedAt(LocalDateTime.now());

        // 🔐 ENCODE PASSWORD
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        user.setProvider("LOCAL");
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }

    // ✅ GET ALL USERS
    public List<User> getallusers() {
        return userRepository.findAll();
    }

    // ✅ GET USER BY ID
    public User GetUsersbyId(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    public User getOrCreateUserByEmail(String email, String name, String provider) {

        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name != null ? name : "User");
                    user.setProvider(provider); // GOOGLE or LOCAL
                    user.setRole("ROLE_USER");
                    user.setCreatedAt(LocalDateTime.now());

                    // password null for Google users
                    user.setPassword(null);

                    return userRepository.save(user);
                });
    }


    // ✅ DELETE USER
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    // In UserService.java - Add this method:
    public void updateUserPassword(Long userId, String rawPassword) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow();
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }

}
