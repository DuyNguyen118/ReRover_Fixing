package WebProject.ReRover.services;

import java.util.List;

import WebProject.ReRover.model.FoundItem;
import WebProject.ReRover.model.ItemMatch;
import WebProject.ReRover.model.LostItem;

public interface ItemMatchService {
    ItemMatch getItemMatchById(int id);
    ItemMatch saveItemMatch(ItemMatch itemMatch);
    void deleteItemMatch(int id);
    List<LostItem> getLostItemsByUserId(int userId);
    List<FoundItem> getFoundItemsByUserId(int userId);
}
