package com.haguse.movies.integration;

import com.haguse.movies.model.Movie;
import com.haguse.movies.repository.MovieRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    private Movie avatarMovie;
    private Movie titanicMovie;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void beforeSetup() {
         this.baseUrl = this.baseUrl + ":" + this.port + "/movies";

        this.avatarMovie = new Movie();
        this.avatarMovie.setName("Avatar");
        this.avatarMovie.setCategory("Action");
        this.avatarMovie.setReleaseDate(LocalDate.of(1998, Month.FEBRUARY, 11));

        this.titanicMovie = new Movie();
        this.titanicMovie.setName("Titanic");
        this.titanicMovie.setCategory("Romance");
        this.titanicMovie.setReleaseDate(LocalDate.of(1986, Month.FEBRUARY, 11));
    }

    @AfterEach
    public void afterSetup() {
        this.movieRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create movie")
    void createMovie() {
        Movie newMovie = restTemplate.postForObject(this.baseUrl, this.avatarMovie, Movie.class);

        assertNotNull(newMovie);
        assertNotEquals(null, newMovie.getId());
    }

    @Test
    @DisplayName("Should fetch movies")
    void getMovies() {
        restTemplate.postForObject(baseUrl, this.avatarMovie, Movie.class);
        restTemplate.postForObject(baseUrl, this.titanicMovie, Movie.class);

        List<Movie> movies = restTemplate.getForObject(baseUrl, List.class);

        assertNotNull(movies);
        assertEquals(2, movies.size());
    }

    @Test
    @DisplayName("Should fetch movie")
    void getMovie() {
        Movie newMovie = restTemplate.postForObject(baseUrl, this.avatarMovie, Movie.class);

        Movie exixtingMovie = restTemplate.getForObject(baseUrl + "/" + newMovie.getId(), Movie.class);

        assertNotNull(exixtingMovie);
        assertEquals(newMovie.getId(), exixtingMovie.getId());
        assertEquals(newMovie.getName(), exixtingMovie.getName());
    }

    @Test
    @DisplayName("Should delete movie")
    void deleteMovie() {
        Movie newAvatarMovie = restTemplate.postForObject(baseUrl, this.avatarMovie, Movie.class);
        restTemplate.postForObject(baseUrl, this.titanicMovie, Movie.class);

        restTemplate.delete(baseUrl + "/" + newAvatarMovie.getId());

        int count = this.movieRepository.findAll().size();

        assertEquals(1, count);
    }

    @Test
    @DisplayName("Should update movie")
    void updateMovie() {
        Movie newMovie = restTemplate.postForObject(baseUrl, this.avatarMovie, Movie.class);
        String newCategory = "Sports";

        newMovie.setCategory(newCategory);

        restTemplate.put(baseUrl + "/{id}", newMovie, newMovie.getId());
        Movie updatedMovie = restTemplate.getForObject(baseUrl + "/" + newMovie.getId(), Movie.class);

        assertNotNull(updatedMovie);
        assertEquals(newCategory, updatedMovie.getCategory());
    }

}
