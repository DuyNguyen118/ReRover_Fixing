package WebProject.ReRover.services;

import java.util.Optional;

import WebProject.ReRover.model.User;

public interface UserService {
    public Optional<User> getUserById(int id);
    public Optional<User> getUserByStudentId(String studentId);
    public User saveUser(User user);
    public void deleteUser(int id);
    public User updateUser(int id, User user);
}
