package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.UserComment;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.UserCommentRepository;
import com.example.demo.repositories.UserRepository;

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
	public List<DTO> getComments() {
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

//	@PostMapping(path = "/obtener1", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public DTO getUsuario(@RequestBody DTO soloid, HttpServletRequest request) {
//		DTO dtoUsuaria = new DTO();
//		User u = userRep.findById(Integer.parseInt(soloid.get("id").toString()));
//		if (u != null) {
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
//		return dtoUsuaria;
//	}
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
