package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.jwSecurity.AutenticadorJWT;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserRepository userRep;

	@Autowired
	// TipoUsuariaRepositorio usuTipo;

	@GetMapping("/all")
	public List<DTO> getUsuarios() {
		List<DTO> userListDTO = new ArrayList<>();
		List<User> users = userRep.findAll();
		for (User u : users) {
			DTO userDTO = new DTO();
			userDTO.put("id", u.getId());
			userDTO.put("name", u.getName());
			userDTO.put("surnames", u.getSurnames());
			userDTO.put("username", u.getUsername());
			userDTO.put("pass", u.getPass());
			userDTO.put("email", u.getEmail());
			userDTO.put("date_created", u.getDateCreated().toString());
			userListDTO.add(userDTO);
		}
		return userListDTO;
	}
	
	@PutMapping(path = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DTO editUser(@RequestBody UserRegisterData u, HttpServletRequest request) {
	    DTO dto = new DTO();
	    
	    // Find the user by ID
	    User user = userRep.findById(u.id);
	    
	    if (user != null) {
	        user.setName(u.name);
	        user.setSurnames(u.surnames);
	        user.setEmail(u.email);
	        user.setUsername(u.username);
	        
	        if (u.pass != null && !u.pass.isEmpty()) {
	            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	            String hashedPassword = encoder.encode(u.pass);
	            user.setPass(hashedPassword);
	        }

	        userRep.save(user);
	        dto.put("result", "ok");
	    } else {
	        dto.put("result", "fail");
	        dto.put("message", "User not found");
	    }
	    
	    return dto;
	}


	@PostMapping(path = "/getone", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DTO getUser(@RequestBody DTO soloid, HttpServletRequest request) {
		DTO userDTO = new DTO();
		User u = userRep.findById(Integer.parseInt(soloid.get("id").toString()));
		if (u != null) {
			userDTO.put("id", u.getId());
			userDTO.put("name", u.getName());
			userDTO.put("surnames", u.getSurnames());
			userDTO.put("username", u.getUsername());
			userDTO.put("pass", u.getPass());
			userDTO.put("email", u.getEmail());
			userDTO.put("date_created", u.getDateCreated().toString());
		} else {
			userDTO.put("result", "fail");
		}
		return userDTO;
	}

	@DeleteMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DTO deleteUser(@RequestBody DTO soloid, HttpServletRequest request) {
		DTO userDTO = new DTO();
		User u = userRep.findById(Integer.parseInt(soloid.get("id").toString()));
		if (u != null) {
			userRep.delete(u);
			userDTO.put("delete", "ok");
		} else {
			userDTO.put("delete", "fail");
		}
		return userDTO;
	}

	
	@PostMapping(path = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addUsuario(@RequestBody UserRegisterData u, HttpServletRequest request) {
	    // Hash the password using BCrypt
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    String hashedPassword = encoder.encode(u.pass);
	    
	    // Save the user with the hashed password
	    Role role = u.role != null ? Role.valueOf(u.role.toString()) : Role.user;
	    userRep.save(new User(u.id, new Date(), u.email, u.name, hashedPassword, role, u.surnames, u.username));
	}


	@PostMapping(path = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DTO autenticaUsuario(@RequestBody UserAuthenticationData input, HttpServletRequest request,
	        HttpServletResponse response) {
	    DTO dto = new DTO();
	    dto.put("result", "fail");

	    // Find the user by username
	    User authenticatedUser = userRep.findByUsername(input.username);
	    
	    if (authenticatedUser != null) {
	        // Use BCrypt to check if the passwords match
	        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        if (encoder.matches(input.pass, authenticatedUser.getPass())) {
	            dto.put("result", "ok");
	            dto.put("jwt", AutenticadorJWT.codifyJWT(authenticatedUser));

	            // Create the JWT cookie
	            Cookie c = new Cookie("jwt", AutenticadorJWT.codifyJWT(authenticatedUser));
				c.setHttpOnly(true);  
            	c.setSecure(false);  
            	c.setPath("/");   
	            c.setMaxAge(-1);  
	            response.addCookie(c);
	        }
	    }

	    return dto;
	}

	@GetMapping(path = "/who")
	public DTO getAuthenticated(HttpServletRequest request) {

		DTO userDTO = new DTO();
		userDTO.put("result", "fail");
		Cookie[] c = request.getCookies();
		int authUserId = -1;

		for (Cookie cookie : c) {
			if (cookie.getName().equals("jwt")) {
				authUserId = AutenticadorJWT.getUserIdFromJWT(cookie.getValue());
			}
		}
		User u = userRep.findById(authUserId);
		if (u != null) {
			userDTO.put("result", "ok");
			userDTO.put("id", u.getId());
			userDTO.put("name", u.getName());
			userDTO.put("surnames", u.getSurnames());
			userDTO.put("username", u.getUsername());
			userDTO.put("email", u.getEmail());
			userDTO.put("role",u.getRole());
			userDTO.put("date_created", u.getDateCreated().toString());
		} else {
			userDTO.put("result", "fail");
		}

		return userDTO;
	}

	static class UserAuthenticationData {
		String pass;
		String username;

		public UserAuthenticationData(String pass, String username) {
			super();
			this.pass = pass;
			this.username = username;
		}
	}

	static class UserRegisterData {

		int id;
		String username;
		String pass;
		String name;
		String surnames;
		String email;
		Date date_created;
		Role role;

		public UserRegisterData(int id, String username, String pass, String name, String surnames, String email,
				Date date_created, Role role) {
			super();
			this.id = id;
			this.username = username;
			this.pass = pass;
			this.name = name;
			this.surnames = surnames;
			this.email = email;
			this.date_created = date_created;
			this.role = role;
		}

	}
}
