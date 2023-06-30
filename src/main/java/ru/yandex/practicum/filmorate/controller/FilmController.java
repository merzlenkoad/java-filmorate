package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.Constants.ACCEPT_RELEASE_DATE;

@RestController
@RequestMapping("/films")
public class FilmController {

    private int generatedId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

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
            throw new ValidationException("Validation Exception!There is no such film.");
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

        if (film.getName() == null || film.getName().isEmpty()
                || film.getDescription().length() > 200
                || film.getDuration() < 0
                || film.getReleaseDate().isBefore(ACCEPT_RELEASE_DATE)) {
            throw new ValidationException("Validation Exception! The name cannot be empty;\n" +
                    "The maximum length of the description is 200 characters;\n" +
                    "Release date — no earlier than December 28, 1895;\n" +
                    "The duration of the film should be positive.");
        }
    }

    public Film getFilmById(int id) {
        return films.get(id);
    }
}
