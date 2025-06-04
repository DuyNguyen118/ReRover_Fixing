package WebProject.ReRover.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import WebProject.ReRover.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    
}