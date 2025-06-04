package WebProject.ReRover.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import WebProject.ReRover.model.FoundItem;
import WebProject.ReRover.model.ItemMatch;
import WebProject.ReRover.model.LostItem;
import WebProject.ReRover.services.FoundItemService;
import WebProject.ReRover.services.ItemMatchService;
import WebProject.ReRover.services.LostItemService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api")
public class ItemMatchedController {
    private ItemMatchService itemMatchService;
    private LostItemService lostItemService;
    private FoundItemService foundItemService;

    public ItemMatchedController(ItemMatchService itemMatchService, LostItemService lostItemService, FoundItemService foundItemService) {
        this.itemMatchService = itemMatchService;
        this.lostItemService = lostItemService;
        this.foundItemService = foundItemService;
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<?> getUserPosts(
        @PathVariable int userId,
        @RequestParam String type) {
        
        if ("lost".equalsIgnoreCase(type)) {
            List<LostItem> lostItems = itemMatchService.getLostItemsByUserId(userId);
            return ResponseEntity.ok(lostItems);
        } else if ("found".equalsIgnoreCase(type)) {
            List<FoundItem> foundItems = itemMatchService.getFoundItemsByUserId(userId);
            return ResponseEntity.ok(foundItems);
        } else {
            return ResponseEntity.badRequest().body("Invalid type parameter. Use 'lost' or 'found'");
        }
    }

    @PostMapping("/item-matched")
    public ResponseEntity<?> saveItemMatch(@RequestBody ItemMatch itemMatch) {
        try {
            // Ensure the lostItem and foundItem are managed entities
            if (itemMatch.getLostItem() != null && itemMatch.getLostItem().getId() != null) {
                LostItem managedLostItem = lostItemService.getLostItemById(itemMatch.getLostItem().getId());
                if (managedLostItem == null) {
                    return ResponseEntity.badRequest().body("Lost item not found with id: " + itemMatch.getLostItem().getId());
                }
                itemMatch.setLostItem(managedLostItem);
            } else {
                return ResponseEntity.badRequest().body("Lost item ID is required");
            }
            
            if (itemMatch.getFoundItem() != null && itemMatch.getFoundItem().getId() != null) {
                FoundItem managedFoundItem = foundItemService.getFoundItemById(itemMatch.getFoundItem().getId());
                if (managedFoundItem == null) {
                    return ResponseEntity.badRequest().body("Found item not found with id: " + itemMatch.getFoundItem().getId());
                }
                itemMatch.setFoundItem(managedFoundItem);
            } else {
                return ResponseEntity.badRequest().body("Found item ID is required");
            }
            
            // Set the current timestamp
            itemMatch.setMatchDate(LocalDateTime.now());
            
            // Save the item match
            ItemMatch savedItemMatch = itemMatchService.saveItemMatch(itemMatch);
            return ResponseEntity.ok(savedItemMatch);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error saving item match: " + e.getMessage());
        }
    }

    @DeleteMapping("/item-matched/{id}")
    public void deleteItemMatch(@PathVariable int id) {
        itemMatchService.deleteItemMatch(id);
    }
}
