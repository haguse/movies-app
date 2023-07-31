package com.haguse.movies.service;

import com.haguse.movies.model.Movie;
import com.haguse.movies.repository.MovieRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    private Movie avatarMovie;
    private Movie titanicMovie;

    @BeforeEach
    void init() {
        this.avatarMovie = new Movie();
        this.avatarMovie.setId(1L);
        this.avatarMovie.setName("Avatar");
        this.avatarMovie.setGenera("Action");
        this.avatarMovie.setReleaseDate(LocalDate.of(1998, Month.FEBRUARY, 11));

        this.titanicMovie = new Movie();
        this.titanicMovie.setId(2L);
        this.titanicMovie.setName("Titanic");
        this.titanicMovie.setGenera("Romance");
        this.titanicMovie.setReleaseDate(LocalDate.of(1986, Month.FEBRUARY, 11));
    }

    @Test
    @DisplayName("Should save the movie object to database")
    void save() {
        when(this.movieRepository.save(any(Movie.class))).thenReturn(this.avatarMovie);

        Movie newMovie = this.movieService.save(this.avatarMovie);

        assertNotNull(newMovie);
        assertEquals(newMovie.getName(), this.avatarMovie.getName());
    }

    @Test
    @DisplayName("Should return list of movies with size 2")
    void getMovies() {
        List<Movie> newMovies = new ArrayList<>();
        newMovies.add(this.titanicMovie);
        newMovies.add(this.avatarMovie);

        when(this.movieRepository.findAll()).thenReturn(newMovies);

        List<Movie> existingMovies = this.movieService.getAllMovies();

        assertNotNull(existingMovies);
        assertEquals(2, existingMovies.size());
    }

    @Test
    @DisplayName("Should return the movie object")
    void getMovieById() {
        when(this.movieRepository.findById(anyLong())).thenReturn(Optional.of(this.avatarMovie));

        Movie existingMovie = this.movieService.getMovieById(1L);

        assertNotNull(existingMovie);
        assertEquals(this.avatarMovie.getId(), existingMovie.getId());
    }

    @Test
    @DisplayName("Should throw the exception for no exist item in database")
    void getMovieByIdForException() {
        when(movieRepository.findById(this.avatarMovie.getId())).thenReturn(Optional.of(this.avatarMovie));

        assertThrows(RuntimeException.class, () -> {
            this.movieService.getMovieById(9999L);
        });
    }

    @Test
    @DisplayName("Should update the movie into the database")
    void updateMovie() {
        when(this.movieRepository.findById(anyLong())).thenReturn(Optional.of(this.avatarMovie));
        when(this.movieRepository.save(any(Movie.class))).thenReturn(this.avatarMovie);

        String newMovieName = "Titanic";

        this.avatarMovie.setName(newMovieName);

        Movie updatedMovie = this.movieService.updateMovie(this.avatarMovie, this.avatarMovie.getId());

        assertNotNull(updatedMovie);
        assertEquals(newMovieName, updatedMovie.getName());
    }

    @Test
    @DisplayName("Should delete the movie from database")
    void deleteMovie() {
        when(this.movieRepository.findById(anyLong())).thenReturn(Optional.of(this.avatarMovie));

        doNothing().when(this.movieRepository).delete(any(Movie.class));

        this.movieService.deleteMovie(1L);

        verify(this.movieRepository, times(1)).delete(this.avatarMovie);
    }

}

