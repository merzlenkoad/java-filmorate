package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.controller.Constants.ACCEPT_RELEASE_DATE;

@Slf4j
@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {

    public final FilmStorage filmStorage;

    @Override
    public Film addFilm(Film film) {
        isValid(film);
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        isValid(film);
        filmExists(film.getId());
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAll() {
        ArrayList<Film> allUsers = new ArrayList<>(filmStorage.getFilms().values());
        return allUsers;
    }

    @Override
    public Film getFilm(Integer id) {
        filmExists(id);
        return filmStorage.getFilms().get(id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        filmExists(filmId);
        filmStorage.getFilms().get(filmId).setLikes(userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        filmExists(filmId);
        userLikeExists(filmId, userId);

        filmStorage.getFilms().get(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(String count) {
        List<Film> filmRatingCountList = new ArrayList<>();
        HashMap<Integer, Integer> filmRate = new HashMap<>();
        Integer countInt = Integer.parseInt(count);

        for (Film film: filmStorage.getFilms().values()) {
            filmRate.put(film.getId(), film.getLikes().size());
        }

        Map<Integer, Integer> sortedMap = filmRate.entrySet().stream()
                .sorted((a,b) -> b.getValue() - a.getValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> { throw new AssertionError(); },
                        LinkedHashMap::new
                ));

        List<Integer> sortedFilmRating = new ArrayList<>(sortedMap.keySet());

        if (countInt != 10) {
            for (int i = 0; i < countInt; i++) {
                filmRatingCountList.add(filmStorage.getFilms().get(sortedFilmRating.get(i)));
            }
        } else {
            if (sortedFilmRating.size() < 10) {
                for (int i = 0; i < sortedFilmRating.size(); i++) {
                    filmRatingCountList.add(filmStorage.getFilms().get(sortedFilmRating.get(i)));
                }
            }
        }

        return filmRatingCountList;
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

    public void filmExists(Integer id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Validation Exception!There is no such film.");
        }
    }

    public void userLikeExists(Integer filmId, Integer userId) {
        if (!filmStorage.getFilms().get(filmId).getLikes().contains(userId)) {
            throw new NotFoundException("Validation Exception!There is no such Like.");
        }
    }
}
