package WebProject.ReRover.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import WebProject.ReRover.model.FoundItem;

public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
    List<FoundItem> findByUserId(Long userId);
}