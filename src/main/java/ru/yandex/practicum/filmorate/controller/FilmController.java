package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private int generatedId = 1;
    private Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        isValid(film);
        film.setId(generatedId++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        isValid(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Validation Exception!");
        }
        films.remove(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public void isValid(Film film) {
        LocalDate acceptReleaseDate = LocalDate.parse("1895-12-28");

        if (film.getName() == null || film.getName().isEmpty()
                || film.getDescription().length() > 200
                || film.getDuration() < 0
                || film.getReleaseDate().isBefore(acceptReleaseDate)) {
            throw new ValidationException("Validation Exception!");
        }
    }

    public Film getFilmById(int id) {
        return films.get(id);
    }
}
