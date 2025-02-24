package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.controller.UserController.UserRegisterData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repositories.ReviewRepository;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/reviews")
public class ReviewController {
	@Autowired
	ReviewRepository reviewRep;

	@Autowired
	// TipoUsuariaRepositorio usuTipo;

	@GetMapping("/all")
	public List<DTO> getReviews() {
		List<DTO> reviewListDTO = new ArrayList<>();
		List<Review> reviews = reviewRep.findAll();
		for (Review r : reviews) {
			DTO reviewDto = new DTO();
			reviewDto.put("id", r.getId());
			reviewDto.put("title", r.getTitle());
			reviewDto.put("img", r.getImg());
			reviewDto.put("review", r.getReview());
			reviewDto.put("name", r.getName());
			reviewDto.put("address", r.getAddress());
			reviewDto.put("rating", r.getRating());
			reviewListDTO.add(reviewDto);
		}
		return reviewListDTO;
	}



	@PostMapping(path = "/getone", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DTO getReview(@RequestBody DTO soloid) {
		DTO reviewDto = new DTO();
		Review r = reviewRep.findById(Integer.parseInt(soloid.get("id").toString()));
		if (r != null) {
			reviewDto.put("result", "success");
			reviewDto.put("id", r.getId());
			reviewDto.put("title", r.getTitle());
			reviewDto.put("img", r.getImg());
			reviewDto.put("review", r.getReview());
			reviewDto.put("name", r.getName());
			reviewDto.put("address", r.getAddress());
			reviewDto.put("rating", r.getRating());
		} else {
			reviewDto.put("result", "fail");
		}
		return reviewDto;
	}

	@PutMapping(path = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public DTO editReview(@RequestPart("reviewData") ReviewData r, @RequestPart(value = "image", required = false) MultipartFile image) {
		DTO dto = new DTO();

		Review rN = reviewRep.findById(r.id);

		if (rN != null) {
			String uploadDir = "uploads/reviews/";

			try {
			// Delete the previous image if it exists
			if (image != null){
			if (rN.getImg() != null && !rN.getImg().isEmpty()) {
				Path oldImagePath = Paths.get(uploadDir, rN.getImg());
				Files.deleteIfExists(oldImagePath);
			}
	
			String timestamp = String.valueOf(System.currentTimeMillis());
			String fileName = timestamp + "_" + image.getOriginalFilename();
			Files.createDirectories(Paths.get(uploadDir));
			Files.write(Paths.get(uploadDir,fileName), image.getBytes());
			rN.setImg("/uploads/reviews/"+fileName);
		}
			rN.setName(r.name);
			rN.setAddress(r.address);

			rN.setRating(r.rating);
			rN.setReview(r.review);
			rN.setTitle(r.title);

			reviewRep.save(rN);
			dto.put("result", "ok");

		} catch (IOException e) {
            e.printStackTrace(); // Log the error
            dto.put("result", "error");
		}
		} else {
			dto.put("result", "fail");
		}

		return dto;
	}


	@PostMapping(path = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public DTO addReview(@RequestPart("reviewData") ReviewData r, @RequestPart("image") MultipartFile image) {

		DTO reviewDto = new DTO();

		try {
			String timestamp = String.valueOf(System.currentTimeMillis());
			String fileName = timestamp + "_" + image.getOriginalFilename();
			String uploadDir = "uploads/reviews/";
			Files.createDirectories(Paths.get(uploadDir));
			Files.write(Paths.get(uploadDir,fileName), image.getBytes());


			Review newReview = new Review(0, r.address, "/uploads/reviews/"+fileName,r.name,r.rating,r.review,r.title);
			reviewRep.save(newReview);
			reviewDto.put("result","success");
		}  catch (IOException e) {
			reviewDto.put("result","fail");
		}
		return reviewDto;

	}

	
	@DeleteMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
	public DTO deleteReview(@RequestBody DTO soloid, HttpServletRequest request) {
		DTO reviewDto = new DTO();
		Review r = reviewRep.findById(Integer.parseInt(soloid.get("id").toString()));
		if (r != null) {
			reviewRep.delete(r);
			reviewDto.put("delete", "ok");
		} else {
			reviewDto.put("delete", "fail");
		}
		return reviewDto;
	}

//

	static class ReviewData {
		int id;
		String address;
		String img;
		String name;
		Float rating;
		String review;
		String title;

		public ReviewData(int id, String address, String img, String name, Float rating, String review, String title) {
			super();
			this.id = id;
			this.address = address;
			this.img = img;
			this.name = name;
			this.rating = rating;
			this.review = review;
			this.title = title;
		}

	}
}
