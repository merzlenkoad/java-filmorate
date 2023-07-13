package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("GET - запрос: /films - получение списка фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        log.info("GET - запрос: /films/{id} - получение фильма по id:" + id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        log.info("GET - запрос: /films/popular - получение списка популярных фильмов в количестве:" + count);
        return filmService.getPopular(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("POST - запрос: /films - добавление фильма");
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT - запрос: /films - обновление фильма");
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("PUT - запрос: /films/{id}/like/{userId} - добавление лайка. filmId: " + id + " userId: " + userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("DELETE - запрос: /films/{id}/like/{userId} - удаление лайка. filmId: " + id + " userId: " + userId);
        filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        log.info("DELETE - запрос: /films/{id} - удаление фильма по id: " + id);
        return filmService.delete(id);
    }
}
