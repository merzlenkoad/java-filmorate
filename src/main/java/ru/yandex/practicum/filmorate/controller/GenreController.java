package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
    private GenreService genreService;

    @GetMapping
    public Collection<Genre> getGenres() {
        log.info("GET - запрос: /genres - получение списка жанров");
        return  genreService.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        log.info("GET - запрос: /genres/{id} - получение жанра по id: " + id);
        return genreService.getGenreById(id);
    }

}
