package com.example.demo.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Serializable> {

		@Override
		@Bean
		public abstract List<Comment> findAll();
		public abstract Comment findById(int id);


		@Override
		@Transactional
		public abstract void delete(Comment u);

		@Transactional
		public abstract void deleteById(int id);

		@Override
		@Transactional
		public abstract Comment save(Comment u);
}
