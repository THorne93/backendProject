package com.example.demo.controller;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.model.Comment;
import com.example.demo.model.UserComment;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.UserCommentRepository;
import com.example.demo.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/usercomments")
public class UserCommentController {
	@Autowired
	UserCommentRepository userCommentRep;
	@Autowired
	UserRepository userRep;
	@Autowired
	CommentRepository commentRep;

	@GetMapping("/all")
	public List<DTO> getUserCommentsInteractions() {
		List<DTO> userCommentListDTO = new ArrayList<>();
		List<UserComment> userComments = userCommentRep.findAll();
		for (UserComment us : userComments) {
			DTO ucDTO = new DTO();
			ucDTO.put("id", us.getId());
			ucDTO.put("user", us.getUser().getName());
			ucDTO.put("comment", us.getComment().getComment());
			ucDTO.put("comment_by", us.getComment().getUser().getName());

			ucDTO.put("created_at", us.getCreatedAt());
			ucDTO.put("edited_at", us.getEditedAt());
			ucDTO.put("liked", us.getLiked());
			ucDTO.put("saved", us.getSaved());
			userCommentListDTO.add(ucDTO);
		}
		return userCommentListDTO;
	}
	@PostMapping(path = "/getallbycomment", consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<DTO> getUserCommentsInteractionsByComment(@RequestBody DTO soloid, HttpServletRequest request) {
		List<DTO> userCommentListDTO = new ArrayList<>();
		List<UserComment> userComments = userCommentRep.findAllByComment_Id(Integer.parseInt(soloid.get("commentId").toString()));
		for (UserComment us : userComments) {
			DTO ucDTO = new DTO();
			ucDTO.put("id", us.getId());
			ucDTO.put("user", us.getUser().getName());
			ucDTO.put("comment", us.getComment().getComment());
			ucDTO.put("comment_by", us.getComment().getUser().getName());
			ucDTO.put("id_user", us.getUser().getId());
			ucDTO.put("created_at", us.getCreatedAt());
			ucDTO.put("edited_at", us.getEditedAt());
			ucDTO.put("liked", us.getLiked());
			ucDTO.put("saved", us.getSaved());
			userCommentListDTO.add(ucDTO);
		}
		return userCommentListDTO;
	}

	@PostMapping(path = "/getbycommentanduser", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DTO getUserCommentsInteractionsByCommentAndUser(@RequestBody DTO soloid, HttpServletRequest request) {
		DTO ucDTO = new DTO();
		UserComment userComment = userCommentRep.findByComment_IdAndUser_Id(Integer.parseInt(soloid.get("commentId").toString()),Integer.parseInt(soloid.get("userId").toString()));
		if (userComment != null) {
			ucDTO.put("result", "true");
		} else {
			ucDTO.put("result", "false");
		}
		return ucDTO;
	}

	@PostMapping(path = "/getone", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DTO getUsuario(@RequestBody DTO soloid, HttpServletRequest request) {
		DTO ucDTO = new DTO();
		UserComment us = userCommentRep.findById(Integer.parseInt(soloid.get("id").toString()));
		if (us != null) {
			ucDTO.put("result", "success");
			ucDTO.put("id", us.getId());
			ucDTO.put("user", us.getUser().getName());
			ucDTO.put("comment", us.getComment().getComment());
			ucDTO.put("comment_by", us.getComment().getUser().getName());

			ucDTO.put("created_at", us.getCreatedAt());
			ucDTO.put("edited_at", us.getEditedAt());
			ucDTO.put("liked", us.getLiked());
			ucDTO.put("saved", us.getSaved());
		} else {
			ucDTO.put("result", "fail");
		}
		return ucDTO;
	}
//
//	@DeleteMapping(path = "/borrar1", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public DTO deleteUsuario(@RequestBody DTO soloid, HttpServletRequest request) {
//		DTO dtoUsuaria = new DTO();
//		User u = userRep.findById(Integer.parseInt(soloid.get("id").toString()));
//		if (u != null) {
//			userRep.delete(u);
//			dtoUsuaria.put("borrado", "ok");
//		} else {
//			dtoUsuaria.put("borrado", "fail");
//		}
//		return dtoUsuaria;
//	}
//
	@PostMapping(path = "/newlike", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addLikeNewInteraction(@RequestBody DTO jsonInfo, HttpServletRequest request) {

		User user = userRep.findById(Integer.parseInt(jsonInfo.get("id_user").toString()));
		Comment comment = commentRep.findById(Integer.parseInt(jsonInfo.get("id_comment").toString()));

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		UserComment userComment = new UserComment();
		userComment.setUser(user);
		userComment.setComment(comment);
		userComment.setLiked(true);
		userComment.setSaved(false);
		userComment.setCreatedAt(timestamp);
		userComment.setEditedAt(timestamp);
		
		userCommentRep.save(userComment);
		System.out.println("Like saved successfully for user: " + user.getId() + " on comment: " + comment.getId());
	}

	@PostMapping(path = "/newsave", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addSaveNewInteraction(@RequestBody DTO jsonInfo, HttpServletRequest request) {
		User user = userRep.findById(Integer.parseInt(jsonInfo.get("id_user").toString()));
		Comment comment = commentRep.findById(Integer.parseInt(jsonInfo.get("id_comment").toString()));


		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		UserComment userComment = new UserComment();
		userComment.setUser(user);
		userComment.setComment(comment);
		userComment.setLiked(false);
		userComment.setSaved(true);
		userComment.setCreatedAt(timestamp);
		userComment.setEditedAt(timestamp);
		
		userCommentRep.save(userComment);
		System.out.println("Like saved successfully for user: " + user.getId() + " on comment: " + comment.getId());
	}

	@PutMapping(path = "/changeinteraction", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DTO changeInteraction(@RequestBody DTO soloid, HttpServletRequest request) {
		DTO ucDTO = new DTO();
		String change = soloid.get("change").toString();  // Ensure it's a string
	
		// Find the existing interaction
		UserComment userComment = userCommentRep.findByComment_IdAndUser_Id(
			Integer.parseInt(soloid.get("commentId").toString()),
			Integer.parseInt(soloid.get("userId").toString())
		);
	
		if (userComment != null) {
			if ("liked".equals(change)) { // Proper string comparison
				userComment.setLiked(!userComment.getLiked()); // Toggle boolean
			} else if ("saved".equals(change)) {
				userComment.setSaved(!userComment.getSaved()); // Toggle boolean
			}
	
			// Save changes to the database
			userCommentRep.save(userComment);
	
			// Indicate success
			ucDTO.put("result", "true");
		} else {
			ucDTO.put("result", "false");
		}
	
		return ucDTO;
	}
	
}
