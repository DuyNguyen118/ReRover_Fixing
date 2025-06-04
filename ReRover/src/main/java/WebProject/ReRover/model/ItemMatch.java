package WebProject.ReRover.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item_matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemMatch {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Integer id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_item_id", nullable = false)
    private LostItem lostItem;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "found_item_id", nullable = false)
    private FoundItem foundItem;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_by_user")
    private User matchedByUser;
    
    @Column(name = "lost_item_user_confirmed", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean lostItemUserConfirmed = false;
    
    @Column(name = "found_item_user_confirmed", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean foundItemUserConfirmed = false;
    
    @Column(name = "admin_approved", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean adminApproved = false;
    
    @Column(nullable = false, length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'pending'")
    private String status = "pending";
    
    @Column(name = "match_date", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime matchDate;
    
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AdminAction> adminActions = new ArrayList<>();
    
    // Helper methods for bidirectional relationships
    public void setLostItem(LostItem lostItem) {
        this.lostItem = lostItem;
    }
    
    public void setFoundItem(FoundItem foundItem) {
        this.foundItem = foundItem;
    }
    
    public void addAdminAction(AdminAction action) {
        adminActions.add(action);
        action.setMatch(this);
    }
    
    public void removeAdminAction(AdminAction action) {
        adminActions.remove(action);
        action.setMatch(null);
    }
}
