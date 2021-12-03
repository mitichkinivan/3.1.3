package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.model.User;
import web.model.UserDto;
import web.service.RoleService;
import web.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/admin")
public class MyRestController {

	private final UserService userService;
	private final RoleService roleService;

	@Autowired
	public MyRestController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		final List<User> users = userService.getAllUsers();
		List<UserDto> userDtoList = new ArrayList<>();

		for (User u : users) {
			userDtoList.add(UserDto.fromUser(u));
		}

		return !userDtoList.isEmpty()
				? new ResponseEntity<>(userDtoList, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable("id") int id) {
		final User user = userService.getUser(id);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		UserDto userDto = UserDto.fromUser(user);

		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	@PostMapping("/new")
	public ResponseEntity<User> addNewUser(@RequestBody UserDto userDto) {
		User user = userDto.toUser();
		user.setRoles(Arrays.stream(userDto.getRoles()).map(roleService::getByRoleName).collect(Collectors.toSet()));
		userService.addUser(user);

		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") int id, @RequestBody UserDto userDto) {
		User user = userDto.toUser();
		user.setId(id);
		user.setRoles(Arrays.stream(userDto.getRoles()).map(roleService::getByRoleName).collect(Collectors.toSet()));
		final boolean result = userService.updateUser(user);

		return result
				? new ResponseEntity<>(user, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		final boolean result = userService.deleteUser(id);

		return result
				? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
	}
}