package com.haguse.movies.repository;

import com.haguse.movies.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByCategory(String category);

}
