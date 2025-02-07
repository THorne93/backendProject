package com.example.demo.controller;

import java.util.ArrayList;
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
//
//	@PostMapping(path = "/insertar", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public void addUsuario(@RequestBody DatosAltaUsuario u, HttpServletRequest request) {
//
//		userRep.save(new Usuaria(u.id, u.fechaNac, null, DatatypeConverter.parseBase64Binary(u.img), u.nombre, u.pass,
//				u.username, usuTipo.findById(u.rol)));
//		// si la img la ponemos como byte.. tenemos que poner
//		// Datatypeconverter.parseBase64Binary(u.img)
//
//	}
//
//	@PostMapping(path = "/autentica", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public DTO autenticaUsuario(@RequestBody DatosAutenticaUsuario datos, HttpServletRequest request,
//			HttpServletResponse response) {
//		DTO dto = new DTO();
//		dto.put("result", "fail");
//
//		User usuarioAutenticado = userRep.findByUsernameAndPass(datos.username, datos.pass);
//		if (usuarioAutenticado != null) {
//			dto.put("result", "ok");
//			dto.put("jwt", AutenticadorJWT.codificaJWT(usuarioAutenticado));
//			Cookie cook = new Cookie("jwt", AutenticadorJWT.codificaJWT(usuarioAutenticado));
//			cook.setMaxAge(-1);
//			response.addCookie(cook);
//		}
//
//		return dto;
//	}
//
//	@GetMapping(path = "/quieneres")
//	public DTO getAutenticado(HttpServletRequest request) {
//
//		DTO dtoUsuaria = new DTO();
//		dtoUsuaria.put("result", "fail");
//		Cookie[] c = request.getCookies();
//		int idUsuarioAutenticado = -1;
//
//		for (Cookie cookie : c) {
//			if (cookie.getName().equals("jwt")) {
//				idUsuarioAutenticado = AutenticadorJWT.getIdUsuarioDesdeJWT(cookie.getValue());
//			}
//		}
//		User u = userRep.findById(idUsuarioAutenticado);
//		if (u != null) {
//			dtoUsuaria.put("result", "ok");
//			dtoUsuaria.put("id", u.getId());
//			dtoUsuaria.put("nombre", u.getNombre());
//			dtoUsuaria.put("fecha_nac", u.getFechaNac().toString());
//			if (u.getFechaElim() != null) {
//				dtoUsuaria.put("fecha_elim", u.getFechaElim().toString());
//			} else {
//				dtoUsuaria.put("fecha_elim", new Date(0));
//			}
//			dtoUsuaria.put("username", u.getUsername());
//
//			if (u.getUsuarioTipo() != null) {
//				dtoUsuaria.put("idDeRol", u.getUsuarioTipo().getRol());
//			}
//		} else {
//			dtoUsuaria.put("result", "fail");
//		}
//
//		return dtoUsuaria;
//	}
//
//	static class DatosAutenticaUsuario {
//		String pass;
//		String username;
//
//		public DatosAutenticaUsuario(String pass, String username) {
//			super();
//			this.pass = pass;
//			this.username = username;
//		}
//	}
//
//	static class DatosAltaUsuario {
//		int id;
//		Date fechaNac;
//		Date fechaElim;
//		String img;
//		// byte[] img;
//		String nombre;
//		String pass;
//		String username;
//		int rol;
//
//		public DatosAltaUsuario(int id, Date fechaNac, Date fechaElim, String img, String nombre, String pass,
//				String username, int rol) {
//			super();
//			this.id = id;
//			this.fechaNac = fechaNac;
//			this.fechaElim = fechaElim;
//			this.img = img;
//			this.nombre = nombre;
//			this.pass = pass;
//			this.username = username;
//			this.rol = rol;
//		}
//	}
}
