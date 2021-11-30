package web.controllers;

import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String printUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());

        return "/admin";
    }

    @GetMapping("/new")
    public String addNewUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());

        return "new";
    }

    @PostMapping(value = "")
    public String createUser(@RequestParam String name, @RequestParam String lastName,
                             @RequestParam String email, @RequestParam String username,
                             @RequestParam String password, @RequestParam List<Long> roles) {
        Set<Role> userRoles = new HashSet<>();

        for(Long roleId: roles){
            userRoles.add(roleService.getRole(roleId));
        }

        User user = new User(name, lastName, email, username, password);
        user.setRoles(userRoles);
        userService.addUser(user);

        return "redirect:/admin";
    }

    @GetMapping(value = "/{id}/update")
    public String update(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("roles", roleService.getAllRoles());

        return "/update";
    }

    @PutMapping("/update/{id}")
    public String updateUser( @ModelAttribute("user") User user, @RequestParam List<Long> roles) {
        Set<Role> userRoles = new HashSet<>();

        for(Long roleId: roles){
            userRoles.add(roleService.getRole(roleId));
        }

        user.setRoles(userRoles);
        userService.updateUser(user);

        return "redirect:/admin";
    }

    @DeleteMapping(value = "/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.deleteUser(id);

        return "redirect:/admin";
    }
}
