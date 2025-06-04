package WebProject.ReRover.services.Impl;

import WebProject.ReRover.model.FoundItem;
import WebProject.ReRover.repository.FoundItemRepository;
import WebProject.ReRover.services.FoundItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoundItemServiceImpl implements FoundItemService {
    private FoundItemRepository foundItemRepository;

    public FoundItemServiceImpl(FoundItemRepository foundItemRepository) {
        this.foundItemRepository = foundItemRepository;
    }

    @Override
    public List<FoundItem> getAllFoundItems() {
        return foundItemRepository.findAll();
    }

    @Override
    public List<FoundItem> getFoundItemsByUserId(int userId) {
        return foundItemRepository.findByUserId((long) userId);
    }

    @Override
    public FoundItem getFoundItemById(int id) {
        return foundItemRepository.findById((long) id).orElse(null);
    }

    @Override
    public FoundItem saveFoundItem(FoundItem foundItem) {
        return foundItemRepository.save(foundItem);
    }

    @Override
    public void deleteFoundItem(int id) {
        foundItemRepository.deleteById((long) id);
    }
}
