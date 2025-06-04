package WebProject.ReRover.repository;

import WebProject.ReRover.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Existing methods
    Optional<User> findByStudentId(String studentId);
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);

    
    // Basic search by name
    List<User> findByFullnameContainingIgnoreCase(String name);
    
    // Find by email
    Optional<User> findByEmail(String email);
    
    // Find by phone number
    List<User> findByPhoneNumber(String phoneNumber);
    
    // Search users by name using native query (case-insensitive)
    @Query(value = "SELECT * FROM users WHERE LOWER(fullname) LIKE LOWER(CONCAT('%', :name, '%'))", nativeQuery = true)
    List<User> searchUsersByName(@Param("name") String name);
    
    // Check if phone number exists (excluding a specific user)
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Integer id);
}