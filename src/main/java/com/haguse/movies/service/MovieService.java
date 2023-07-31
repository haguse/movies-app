package com.haguse.movies.service;

import com.haguse.movies.model.Movie;
import com.haguse.movies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public Movie saveMovie(Movie movie) {
        try {
            return this.movieRepository.save(movie);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save movie");
        }
    }

    public List<Movie> fetchAllMovies() {
        try {
            return this.movieRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Unable to get movies");
        }
    }

    public Movie fetchMovieById(Long id) {
        try {
            return this.movieRepository.findById(id).orElseThrow(()-> new RuntimeException("Movie is not exist"));
        } catch (Exception e) {
            throw new RuntimeException("Unable to get movie");
        }
    }

    public Movie updateMovie(Movie movie, Long id) {
        try {
            Movie existingMovie = this.movieRepository.findById(id).orElseThrow(()-> new RuntimeException("Movie is not exist"));
            existingMovie.setCategory(movie.getCategory());
            existingMovie.setName(movie.getName());
            existingMovie.setReleaseDate(movie.getReleaseDate());
            return this.movieRepository.save(existingMovie);
        } catch (Exception e) {
            throw new RuntimeException("Unable to update movie");
        }
    }

    public void deleteMovie(Long id) {
        try {
            Movie existingMovie = this.movieRepository.findById(id).orElseThrow(()-> new RuntimeException("Movie is not exist"));
            this.movieRepository.delete(existingMovie);
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete movie");
        }
    }

}