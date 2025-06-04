package WebProject.ReRover.services;

import java.util.List;

import WebProject.ReRover.model.LostItem;

public interface LostItemService {
    public List<LostItem> getAllLostItems();
    public List<LostItem> getLostItemsByUserId(int userId);
    public LostItem getLostItemById(int id);
    public LostItem saveLostItem(LostItem lostItem);
    public void deleteLostItem(int id);
}
