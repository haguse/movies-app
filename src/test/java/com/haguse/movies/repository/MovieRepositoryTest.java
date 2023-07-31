package com.haguse.movies.repository;

import com.haguse.movies.model.Movie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    private Movie avatarMovie;
    private Movie titanicMovie;

    @BeforeEach
    void init() {
        this.avatarMovie = new Movie();
        this.avatarMovie.setName("Avatar");
        this.avatarMovie.setCategory("Action");
        this.avatarMovie.setReleaseDate(LocalDate.of(1998, Month.FEBRUARY, 11));

        this.titanicMovie = new Movie();
        this.titanicMovie.setName("Titanic");
        this.titanicMovie.setCategory("Romance");
        this.titanicMovie.setReleaseDate(LocalDate.of(1986, Month.FEBRUARY, 11));
    }

    @Test
    @DisplayName("It should save the movie to the database")
    void save() {
        Movie newMovie = this.movieRepository.save(this.avatarMovie);

        assertNotNull(newMovie);
    }

    @Test
    @DisplayName("It should return the movies list with size of 2")
    void getAllMovies() {
        this.movieRepository.save(this.avatarMovie);
        this.movieRepository.save(this.titanicMovie);

        List<Movie> movies = movieRepository.findAll();

        assertNotNull(movies);
        assertEquals(2, movies.size());
    }

    @Test
    @DisplayName("It should return the movie by its id")
    void getMovieById() {
        this.movieRepository.save(this.avatarMovie);

        Movie existingMovie = this.movieRepository.findById(this.avatarMovie.getId()).get();

        assertNotNull(existingMovie);
        assertEquals(this.avatarMovie.getCategory(), existingMovie.getCategory());
    }

    @Test
    @DisplayName("It should update the movie with new category")
    void updateMovie() {
        this.movieRepository.save(this.avatarMovie);

        Movie existingMovie = this.movieRepository.findById(this.avatarMovie.getId()).get();

        String newCategory = "Fantasy";
        existingMovie.setCategory(newCategory);

        Movie newMovie = this.movieRepository.save(existingMovie);

        assertEquals(newCategory, newMovie.getCategory());
        assertEquals(this.avatarMovie.getName(), newMovie.getName());
    }

    @Test
    @DisplayName("It should delete existing movie")
    void deleteMovie() {
        this.movieRepository.save(this.avatarMovie);
        Long avatarMovieId = this.avatarMovie.getId();

        this.movieRepository.save(this.titanicMovie);
        this.movieRepository.delete(this.avatarMovie);

        Optional<Movie> existingAvatarMovie = this.movieRepository.findById(avatarMovieId);
        List<Movie> movies = this.movieRepository.findAll();

        assertEquals(1, movies.size());
        assertTrue(existingAvatarMovie.isEmpty());
    }

    @Test
    @DisplayName("It should return the movies list with category")
    void getMoviesByCategory() {
        this.movieRepository.save(this.avatarMovie);
        this.movieRepository.save(this.titanicMovie);

        List<Movie> movies = this.movieRepository.findByCategory(this.titanicMovie.getCategory());

        assertNotNull(movies);
        assertEquals(movies.size(), 1);
    }

}
