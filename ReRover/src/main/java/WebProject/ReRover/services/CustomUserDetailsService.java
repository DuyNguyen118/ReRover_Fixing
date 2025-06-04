package WebProject.ReRover.services;

import WebProject.ReRover.model.User;
import WebProject.ReRover.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository; // Your repository that accesses the User entity

    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        User user = userRepository.findByStudentId(studentId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with student ID: " + studentId));
        
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getStudentId())  // Use studentId as the username
            .password(user.getPassword())
            .authorities("ROLE_USER")  // Set appropriate authorities
            .build();
    }
}