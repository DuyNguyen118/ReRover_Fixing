package WebProject.ReRover.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import WebProject.ReRover.model.LostItem;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    List<LostItem> findByUser_Id(Long userId);
}