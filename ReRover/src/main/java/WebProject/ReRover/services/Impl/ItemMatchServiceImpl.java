package WebProject.ReRover.services.Impl;

import java.util.List;

import WebProject.ReRover.model.FoundItem;
import WebProject.ReRover.model.ItemMatch;
import WebProject.ReRover.model.LostItem;
import WebProject.ReRover.repository.ItemMatchRepository;
import WebProject.ReRover.services.ItemMatchService;
import WebProject.ReRover.services.LostItemService;
import WebProject.ReRover.services.FoundItemService;

import org.springframework.stereotype.Service;

@Service
public class ItemMatchServiceImpl implements ItemMatchService {
    private ItemMatchRepository itemMatchRepository;
    private LostItemService lostItemService;
    private FoundItemService foundItemService;

    public List<LostItem> getLostItemsByUserId(int userId) {
        return itemMatchRepository.findLostItemsByUserId(userId);
    }

    public List<FoundItem> getFoundItemsByUserId(int userId) {
        return itemMatchRepository.findFoundItemsByUserId(userId);
    }

    public ItemMatchServiceImpl(ItemMatchRepository itemMatchRepository, LostItemService lostItemService, FoundItemService foundItemService) {
        this.itemMatchRepository = itemMatchRepository;
        this.lostItemService = lostItemService;
        this.foundItemService = foundItemService;
    }

    @Override
    public ItemMatch getItemMatchById(int id) {
        return itemMatchRepository.findById((Integer) id).orElse(null);
    }

    @Override
    public ItemMatch saveItemMatch(ItemMatch itemMatch) {
        // If the lostItem is not null and has an ID, fetch it from the database
        if (itemMatch.getLostItem() != null && itemMatch.getLostItem().getId() != null) {
            LostItem existingLostItem = lostItemService.getLostItemById(itemMatch.getLostItem().getId());
            itemMatch.setLostItem(existingLostItem);
        }
        
        // If the foundItem is not null and has an ID, fetch it from the database
        if (itemMatch.getFoundItem() != null && itemMatch.getFoundItem().getId() != null) {
            FoundItem existingFoundItem = foundItemService.getFoundItemById(itemMatch.getFoundItem().getId());
            itemMatch.setFoundItem(existingFoundItem);
        }
        
        return itemMatchRepository.save(itemMatch);
    }

    @Override
    public void deleteItemMatch(int id) {
        itemMatchRepository.deleteById((Integer) id);
    }
}
