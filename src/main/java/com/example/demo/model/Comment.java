package com.example.demo.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


/**
 * The persistent class for the comments database table.
 *
 */
@Entity
@Table(name="comments")
@NamedQuery(name="Comment.findAll", query="SELECT c FROM Comment c")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Lob
	private String comment;

	@Column(name="created_at")
	private Timestamp createdAt;

	//bi-directional many-to-one association to Review
	@ManyToOne
	@JoinColumn(name="id_review")
	private Review review;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="id_user")
	private User user;

	//bi-directional many-to-one association to UserComment
	@OneToMany(mappedBy="comment")
	private List<UserComment> userComments;

	public Comment() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Review getReview() {
		return this.review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<UserComment> getUserComments() {
		return this.userComments;
	}

	public void setUserComments(List<UserComment> userComments) {
		this.userComments = userComments;
	}

	public UserComment addUserComment(UserComment userComment) {
		getUserComments().add(userComment);
		userComment.setComment(this);

		return userComment;
	}

	public UserComment removeUserComment(UserComment userComment) {
		getUserComments().remove(userComment);
		userComment.setComment(null);

		return userComment;
	}

	public Comment(int id, String comment, Timestamp createdAt, Review review, User user) {
		super();
		this.id = id;
		this.comment = comment;
		this.createdAt = createdAt;
		this.review = review;
		this.user = user;
	}

}