package com.example.demo.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Serializable> {

		@Override
		@Bean
		public abstract List<Review> findAll();
		public abstract Review findById(int id);

		@Override
		@Transactional
		public abstract void delete(Review u);

		@Transactional
		public abstract void deleteById(int id);

		@Override
		@Transactional
		public abstract Review save(Review u);
}
