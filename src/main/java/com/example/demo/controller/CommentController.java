package com.example.demo.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.model.Review;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.ReviewRepository;
import com.example.demo.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/comments")
public class CommentController {
	@Autowired
	CommentRepository commentRep;
	@Autowired
	UserRepository userRep;
	@Autowired
	ReviewRepository reviewRep;

	@GetMapping("/all")
	public List<DTO> getComments() {
		List<DTO> userListDTO = new ArrayList<>();
		List<Comment> comments = commentRep.findAll();
		for (Comment c : comments) {
			DTO commentDto = new DTO();
			commentDto.put("id", c.getId());
			commentDto.put("user", c.getUser().getName());
			commentDto.put("review", c.getReview().getName());
			commentDto.put("created_at", c.getCreatedAt());
			commentDto.put("comment", c.getComment());
			userListDTO.add(commentDto);
		}
		return userListDTO;
	}

	@PostMapping(path = "/getone", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DTO getComment(@RequestBody DTO soloid, HttpServletRequest request) {
		DTO commentDTO = new DTO();
		Comment c = commentRep.findById(Integer.parseInt(soloid.get("id").toString()));
		if (c != null) {
			commentDTO.put("result", "success");
			commentDTO.put("id", c.getId());
			commentDTO.put("user", c.getUser().getName());
			commentDTO.put("review", c.getReview().getName());
			commentDTO.put("created_at", c.getCreatedAt());
			commentDTO.put("comment", c.getComment());
		} else {
			commentDTO.put("result", "fail");
		}
		return commentDTO;
	}

	@DeleteMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DTO deleteComment(@RequestBody DTO soloid, HttpServletRequest request) {
		DTO commentDTO = new DTO();
		Comment c = commentRep.findById(Integer.parseInt(soloid.get("id").toString()));
		if (c != null) {
			commentRep.delete(c);
			commentDTO.put("delete", "ok");
		} else {
			commentDTO.put("delete", "fail");
		}
		return commentDTO;
	}

	@PostMapping(path = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addComment(@RequestBody CommentData c, HttpServletRequest request) {
		Review review = reviewRep.findById(c.id_review);  // Retrieve the review by ID
	    User user = userRep.findById(c.id_user);  // Retrieve the user by ID

	    if (review != null && user != null) {
	        // Create a new Timestamp for the current time
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	        // Save the new comment
	        commentRep.save(new Comment(c.id, c.comment, timestamp, review, user));
	    } 
	}

	static class CommentData {
		int id;
		int id_user;
		int id_review;
		Timestamp created_at;
		String comment;

		public CommentData(int id, int id_user, int id_review, Timestamp created_at, String comment) {
			super();
			this.id = id;
			this.id_user = id_user;
			this.id_review = id_review;
			this.created_at = created_at;
			this.comment = comment;
		}

	}
}
