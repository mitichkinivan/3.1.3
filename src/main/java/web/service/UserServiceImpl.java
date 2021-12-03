package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.model.User;
import web.repositories.RoleRepository;
import web.repositories.UserRepository;

import java.util.List;

@Service("userDetailsServiceImpl")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(long id) {
        return userRepository.getById(id);
    }

    @Override
    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        if (user == null) {
            return false;
        }

        if (!user.getPassword().equals(getUser(user.getId()).getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteUser(long id) {
        if (getUser(id) == null) {
            return false;
        }

        userRepository.deleteById(id);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.getByUsername(userName);
    }

    @Override
    @Transactional
    public void addInitUsers() {
        User admin = new User("Ivan", "Ivanov", "admin@mail.com", "admin", "admin");
        User user = new User("Petr", "Petrov", "user@mail.com", "user", "user");
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        addUser(admin);
        addUser(user);
        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        admin.addRole(adminRole);
        admin.addRole(userRole);
        user.addRole(userRole);

        adminRole.addUser(admin);
        userRole.addUser(admin);
        userRole.addUser(user);
    }
}