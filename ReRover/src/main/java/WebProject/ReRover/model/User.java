package WebProject.ReRover.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    
    @NotBlank
    @Size(max = 20)
    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String fullname;
    
    @NotBlank
    @Size(max = 100)
    @Email
    @Column(unique = true, nullable = false)
    private String email;
    
    @Size(max = 20)
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "profile_picture", length = 255)
    private String profilePicture;
    
    @Column(name = "merit_point")
    private Integer meritPoint = 0;
    
    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<LostItem> lostItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FoundItem> foundItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "matchedByUser")
    @JsonIgnore
    private List<ItemMatch> matchedItems;
    
    // Helper methods for bidirectional relationships
    public void addLostItem(LostItem item) {
        lostItems.add(item);
        item.setUser(this);
    }
    
    public void removeLostItem(LostItem item) {
        lostItems.remove(item);
        item.setUser(null);
    }
    
    public void addFoundItem(FoundItem item) {
        foundItems.add(item);
        item.setUser(this);
    }
    
    public void removeFoundItem(FoundItem item) {
        foundItems.remove(item);
        item.setUser(null);
    }
}