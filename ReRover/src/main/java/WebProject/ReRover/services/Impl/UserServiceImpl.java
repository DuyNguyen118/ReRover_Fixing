package WebProject.ReRover.services.Impl;

import java.util.Optional;

import WebProject.ReRover.model.User;
import WebProject.ReRover.repository.UserRepository;
import WebProject.ReRover.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getUserByStudentId(String studentId) {
        return userRepository.findByStudentId(studentId);
    }   

    @Override
    public User updateUser(int id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setFullname(user.getFullname());
            existingUser.setEmail(user.getEmail());
            existingUser.setStudentId(user.getStudentId());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setProfilePicture(user.getProfilePicture());
            return userRepository.save(existingUser);
        }
        return null;
    }
    
    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
