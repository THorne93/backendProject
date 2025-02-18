package com.example.demo.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.UserComment;

@Repository
public interface UserCommentRepository extends JpaRepository<UserComment, Serializable> {

		@Override
		@Bean
		public abstract List<UserComment> findAll();
		public abstract List<UserComment> findAllByComment_Id(int commentid);
		public abstract UserComment findById(int id);
		public abstract UserComment findByComment_IdAndUser_Id(Integer commentId, Integer userId);


		@Override
		@Transactional
		public abstract void delete(UserComment u);

		@Transactional
		public abstract void deleteById(int id);

		@Override
		@Transactional
		public abstract UserComment save(UserComment u);
}
