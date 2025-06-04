package WebProject.ReRover.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import WebProject.ReRover.model.User;
import WebProject.ReRover.services.UserService;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {
    
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<User> getUserByStudentId(@PathVariable String studentId) {
        return userService.getUserByStudentId(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user, HttpServletRequest request) {
    System.out.println("Received update request for user ID: " + id);
    System.out.println("Request body: " + user.toString());
    System.out.println("Auth header: " + request.getHeader("Authorization"));
    
    User updatedUser = userService.updateUser(id, user);
    System.out.println("Updated user: " + updatedUser);
    
    return updatedUser;
}
    
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
