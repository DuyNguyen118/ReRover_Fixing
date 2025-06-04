package WebProject.ReRover.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import WebProject.ReRover.model.FoundItem;
import WebProject.ReRover.model.ItemMatch;
import WebProject.ReRover.model.LostItem;

public interface ItemMatchRepository extends JpaRepository<ItemMatch, Integer> {
    
    @Query("SELECT li FROM LostItem li WHERE li.user.id = :userId")
    List<LostItem> findLostItemsByUserId(@Param("userId") int userId);
    
    @Query("SELECT fi FROM FoundItem fi WHERE fi.user.id = :userId")
    List<FoundItem> findFoundItemsByUserId(@Param("userId") int userId);
}
