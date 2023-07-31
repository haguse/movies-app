package com.haguse.movies.controller;

import com.haguse.movies.model.Movie;
import com.haguse.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Movie create(@RequestBody Movie movie) {
        return this.movieService.save(movie);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> read() {
        return this.movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Movie read(@PathVariable Long id) {
        return this.movieService.getMovieById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.movieService.deleteMovie(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Movie update(@PathVariable Long id, @RequestBody Movie movie) {
        return this.movieService.updateMovie(movie, id);
    }
}