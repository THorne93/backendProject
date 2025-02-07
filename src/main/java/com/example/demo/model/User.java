package com.example.demo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * The persistent class for the users database table.
 *
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Temporal(TemporalType.DATE)
	@Column(name="date_created")
	private Date dateCreated;

	private String email;

	private String name;

	private String pass;

	@Enumerated(EnumType.STRING)  // This tells JPA to store it as a string
	@Column(name = "role")
	private Role role;

	private String surnames;

	private String username;

	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="user")
	private List<Comment> comments;

	//bi-directional many-to-one association to UserComment
	@OneToMany(mappedBy="user")
	private List<UserComment> userComments;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Role getRole() {
	    return this.role;
	}

	public void setRole(Role role) {
	    this.role = role;
	}

	public String getSurnames() {
		return this.surnames;
	}

	public void setSurnames(String surnames) {
		this.surnames = surnames;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setUser(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setUser(null);

		return comment;
	}

	public List<UserComment> getUserComments() {
		return this.userComments;
	}

	public void setUserComments(List<UserComment> userComments) {
		this.userComments = userComments;
	}

	public UserComment addUserComment(UserComment userComment) {
		getUserComments().add(userComment);
		userComment.setUser(this);

		return userComment;
	}

	public UserComment removeUserComment(UserComment userComment) {
		getUserComments().remove(userComment);
		userComment.setUser(null);

		return userComment;
	}

	public User(int id, Date dateCreated, String email, String name, String pass, Role role, String surnames, String username) {
	    this.id = id;
	    this.dateCreated = dateCreated;
	    this.email = email;
	    this.name = name;
	    this.pass = pass;
	    this.role = role;
	    this.surnames = surnames;
	    this.username = username;
	}



}