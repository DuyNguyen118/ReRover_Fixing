package WebProject.ReRover.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lost_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LostItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lost_item_id")
    private Integer id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    @NotBlank
    @Size(max = 150)
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "type", nullable = false)
    private String type;
    
    @Column(name = "category", nullable = false)
    private String category;
    
    @Size(max = 100)
    private String location;
    
    @Column(name = "lost_date")
    private LocalDate lostDate;
    
    @Column(name = "image_url", length = 255)
    private String imageUrl;
    
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "lostItem")
    @JsonIgnore
    private List<ItemMatch> itemMatches;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Helper methods for bidirectional relationship
    public void setUser(User user) {
        this.user = user;
        if (user != null && !user.getLostItems().contains(this)) {
            user.getLostItems().add(this);
        }
    }
}
