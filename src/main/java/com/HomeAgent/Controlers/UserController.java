package com.HomeAgent.Controlers;

import com.HomeAgent.DTO.UserDTO;
import com.HomeAgent.Model.User;
import com.HomeAgent.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ REGISTER USER (EMAIL + PASSWORD)
    @PostMapping("/adduser")
    public String addUser(@ModelAttribute UserDTO userDTO) {
        userService.addUser(userDTO);
        return "redirect:/Login";
    }


    // ✅ GET ALL USERS
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getallusers();
    }

    // ✅ GET USER BY ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.GetUsersbyId(id);
    }

    // ✅ DELETE USER
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
