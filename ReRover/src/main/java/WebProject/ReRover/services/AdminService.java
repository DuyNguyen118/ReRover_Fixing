package WebProject.ReRover.services;

import WebProject.ReRover.model.Admin;

public interface AdminService {
    public Admin getAdminById(int id);
    public Admin saveAdmin(Admin admin);
    public void deleteAdmin(int id);
}
