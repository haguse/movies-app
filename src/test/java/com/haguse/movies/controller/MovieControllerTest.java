package com.haguse.movies.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haguse.movies.model.Movie;
import com.haguse.movies.service.MovieService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest()
public class MovieControllerTest {

    @MockBean
    private MovieService movieService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Movie avatarMovie;
    private Movie titanicMovie;

    @BeforeEach
    void init() {
        this.avatarMovie = new Movie();
        this.avatarMovie.setName("Avatar");
        this.avatarMovie.setGenera("Action");
        this.avatarMovie.setReleaseDate(LocalDate.of(1998, Month.FEBRUARY, 11));

        this.titanicMovie = new Movie();
        this.titanicMovie.setName("Titanic");
        this.titanicMovie.setGenera("Romance");
        this.titanicMovie.setReleaseDate(LocalDate.of(1986, Month.FEBRUARY, 11));
    }

    @Test
    @DisplayName("Should create a new movie")
    void createMovie() throws Exception {
        this.avatarMovie.setId(1L);

        when(this.movieService.save(any(Movie.class))).thenReturn(avatarMovie);

        this.mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(avatarMovie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(avatarMovie.getName())))
                .andExpect(jsonPath("$.genera", is(avatarMovie.getGenera())))
                .andExpect(jsonPath("$.releaseDate", is(avatarMovie.getReleaseDate().toString())));
    }

    @Test
    @DisplayName("Should fetch all movies")
    void getAllMovies() throws Exception {
        List<Movie> movies= new ArrayList<>();
        movies.add(this.avatarMovie);
        movies.add(this.titanicMovie);

        when(this.movieService.getAllMovies()).thenReturn(movies);

        this.mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(movies.size())));
    }

    @Test
    @DisplayName("Should fetch movie by id")
    void getMovie() throws Exception {
        this.avatarMovie.setId(1L);

        when(this.movieService.getMovieById(anyLong())).thenReturn(this.avatarMovie);

        this.mockMvc.perform(get("/movies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(this.avatarMovie.getName())))
                .andExpect(jsonPath("$.genera", is(this.avatarMovie.getGenera())))
                .andExpect(jsonPath("$.releaseDate", is(this.avatarMovie.getReleaseDate().toString())));
    }

    @Test
    @DisplayName("Should delete movie")
    void deleteMovie() throws Exception {
        this.avatarMovie.setId(1L);

        doNothing().when(this.movieService).deleteMovie(anyLong());

        this.mockMvc.perform(delete("/movies/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should update movie")
    void updateMovie() throws Exception {
        this.avatarMovie.setId(1L);

        when(this.movieService.updateMovie(any(Movie.class), anyLong()))
                .thenReturn(this.avatarMovie);

        this.mockMvc.perform(put("/movies/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(this.avatarMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(this.avatarMovie.getName())))
                .andExpect(jsonPath("$.genera", is(this.avatarMovie.getGenera())))
                .andExpect(jsonPath("$.releaseDate", is(this.avatarMovie.getReleaseDate().toString())));
    }

}
