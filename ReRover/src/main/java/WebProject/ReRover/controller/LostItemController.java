package WebProject.ReRover.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import WebProject.ReRover.model.LostItem;
import WebProject.ReRover.model.User;
import WebProject.ReRover.repository.UserRepository;
import WebProject.ReRover.services.LostItemService;
import WebProject.ReRover.services.FileStorageService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/lost-item")
public class LostItemController {
    
    private final LostItemService lostItemService;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public LostItemController(LostItemService lostItemService, UserRepository userRepository, FileStorageService fileStorageService) {
        this.lostItemService = lostItemService;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public List<LostItem> getAllLostItems() {
        return lostItemService.getAllLostItems();
    }

    @GetMapping("/user/{userId}")
    public List<LostItem> getLostItemsByUserId(@PathVariable int userId) {
        return lostItemService.getLostItemsByUserId(userId);
    }

    @GetMapping("/{id}")
    public LostItem getLostItemById(@PathVariable int id) {
        return lostItemService.getLostItemById(id);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> saveLostItem(
            @RequestPart("data") Map<String, Object> requestBody,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // Get userId from the request body
            Integer userId = (Integer) requestBody.get("userId");
            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User ID is required"));
            }

            // Find the user by ID
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            // Handle file upload
            String fileName = null;
            if (file != null && !file.isEmpty()) {
                fileName = fileStorageService.storeFile(file);
            }

            // Create and save the lost item
            LostItem lostItem = new LostItem();
            lostItem.setTitle((String) requestBody.get("title"));
            lostItem.setDescription((String) requestBody.get("description"));
            lostItem.setLocation((String) requestBody.get("location"));
            lostItem.setCategory((String) requestBody.get("category"));
        
            String dateStr = (String) requestBody.get("lostDate");
            if (dateStr != null) {
                try {
                    lostItem.setLostDate(LocalDate.parse(dateStr.substring(0, 10)));
                } catch (Exception e) {
                    lostItem.setLostDate(LocalDate.now());
                }
            } else {
                lostItem.setLostDate(LocalDate.now());
            }
        
            lostItem.setImageUrl(fileName);
            lostItem.setUser(user);
        
            LostItem savedItem = lostItemService.saveLostItem(lostItem);
        
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedItem);
            
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to save found item: " + e.getMessage()));
        }
    }

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Resource resource = fileStorageService.loadFileAsResource(fileName);
            String contentType = determineContentType(fileName);
            
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }

    @DeleteMapping("/{id}")
    public void deleteLostItem(@PathVariable int id) {
        lostItemService.deleteLostItem(id);
    }
}
