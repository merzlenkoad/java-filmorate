package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    private void setUP() {
        filmController = new FilmController();
    }

    @Test
    public void createFilm() {
        Film film = new Film("name","description",
                LocalDate.of(1996,12,05),150L);
        filmController.createFilm(film);

        Film expectedFilm = new Film(1,"name", "description",
                LocalDate.of(1996,12,05), 150L);

        Assertions.assertEquals(expectedFilm, filmController.getFilmById(1));
    }

    @Test
    public void createFilmNameEmpty() {
        Film film = new Film("","description",
                LocalDate.of(1996,12,05),150L);

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> filmController.createFilm(film));

        Assertions.assertEquals("Validation Exception! The name cannot be empty;\n" +
                "The maximum length of the description is 200 characters;\n" +
                "Release date — no earlier than December 28, 1895;\n" +
                "The duration of the film should be positive.", e.getMessage());
    }

    @Test
    public void createFilmMaxLengthDescription200() {
        Film film = new Film("name","description-description-description-description-description-" +
                "description-description-description-description-description-description-description-description-" +
                "description-description-description-description-description-description-description-description-" +
                "description-description-description-description-description-description-description-description",
                LocalDate.of(1996,12,05),150L);

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> filmController.createFilm(film));

        Assertions.assertEquals("Validation Exception! The name cannot be empty;\n" +
                "The maximum length of the description is 200 characters;\n" +
                "Release date — no earlier than December 28, 1895;\n" +
                "The duration of the film should be positive.", e.getMessage());
    }

    @Test
    public void createFilmReleaseDateMin() {
        Film film = new Film("name","description",
                LocalDate.of(1700,12,05),150L);

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> filmController.createFilm(film));

        Assertions.assertEquals("Validation Exception! The name cannot be empty;\n" +
                "The maximum length of the description is 200 characters;\n" +
                "Release date — no earlier than December 28, 1895;\n" +
                "The duration of the film should be positive.", e.getMessage());
    }

    @Test
    public void createFilmDurationPositive() {
        Film film = new Film("name","description",
                LocalDate.of(1996,12,05),-80L);

        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> filmController.createFilm(film));

        Assertions.assertEquals("Validation Exception! The name cannot be empty;\n" +
                "The maximum length of the description is 200 characters;\n" +
                "Release date — no earlier than December 28, 1895;\n" +
                "The duration of the film should be positive.", e.getMessage());
    }

    @Test
    public void updateFilm() {
        Film film = new Film("name","description",
                LocalDate.of(1996,12,05),150L);
        filmController.createFilm(film);
        filmController.updateFilm(new Film(1,"name","description",
                LocalDate.of(1996,12,05),100L));

        Film expectedFilm = new Film(1,"name", "description",
                LocalDate.of(1996,12,05), 100L);

        Assertions.assertEquals(expectedFilm, filmController.getFilmById(1));
    }

    @Test
    public void updateFilmNameEmpty() {
        Film film = new Film("name","description",
                LocalDate.of(1996,12,05),150L);
        filmController.createFilm(film);
        Film updateFilm = new Film(1,"","description",
                LocalDate.of(1996,12,05),150L);


        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> filmController.updateFilm(updateFilm));

        Assertions.assertEquals("Validation Exception! The name cannot be empty;\n" +
                "The maximum length of the description is 200 characters;\n" +
                "Release date — no earlier than December 28, 1895;\n" +
                "The duration of the film should be positive.", e.getMessage());
    }

    @Test
    public void updateFilmMaxLengthDescription200() {
        Film film = new Film("name","description",
                LocalDate.of(1996,12,05),150L);
        filmController.createFilm(film);
        Film updateFilm = new Film(1,"","description-description-description-description-" +
                "description-description-description-description-description-description-description-description-" +
                "description-description-description-description-description-description-description-description-" +
                "description-description-description-description-description-description-description-description",
                LocalDate.of(1996,12,05),150L);


        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> filmController.updateFilm(updateFilm));

        Assertions.assertEquals("Validation Exception! The name cannot be empty;\n" +
                "The maximum length of the description is 200 characters;\n" +
                "Release date — no earlier than December 28, 1895;\n" +
                "The duration of the film should be positive.", e.getMessage());
    }

    @Test
    public void updateFilmReleaseDateMin() {
        Film film = new Film("name","description",
                LocalDate.of(1996,12,05),150L);
        filmController.createFilm(film);
        Film updateFilm = new Film(1,"","description",
                LocalDate.of(1700,12,05),150L);


        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> filmController.updateFilm(updateFilm));

        Assertions.assertEquals("Validation Exception! The name cannot be empty;\n" +
                "The maximum length of the description is 200 characters;\n" +
                "Release date — no earlier than December 28, 1895;\n" +
                "The duration of the film should be positive.", e.getMessage());
    }

    @Test
    public void updateFilmDurationPositive() {
        Film film = new Film("name","description",
                LocalDate.of(1996,12,05),150L);
        filmController.createFilm(film);
        Film updateFilm = new Film(1,"","description",
                LocalDate.of(1700,12,05),-80L);


        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> filmController.updateFilm(updateFilm));

        Assertions.assertEquals("Validation Exception! The name cannot be empty;\n" +
                "The maximum length of the description is 200 characters;\n" +
                "Release date — no earlier than December 28, 1895;\n" +
                "The duration of the film should be positive.", e.getMessage());
    }

    @Test
    public void updateFilmNotExist() {
        Film film = new Film("name","description",
                LocalDate.of(1996,12,05),150L);
        filmController.createFilm(film);
        Film updateFilm = new Film(2,"","description",
                LocalDate.of(1700,12,05),-150L);


        final ValidationException e = Assertions.assertThrows(ValidationException.class,
                () -> filmController.updateFilm(updateFilm));

        Assertions.assertEquals("Validation Exception! The name cannot be empty;\n" +
                "The maximum length of the description is 200 characters;\n" +
                "Release date — no earlier than December 28, 1895;\n" +
                "The duration of the film should be positive.", e.getMessage());
    }

    @Test
    public void getAllFilms() {
        Film film = new Film("name","description",
                LocalDate.of(1996,12,05),150L);
        filmController.createFilm(film);

        Film newFilm = new Film("newName","newDescription",
                LocalDate.of(1996,12,06),100L);
        filmController.createFilm(film);

        List<Film> expectedFilms = Arrays.asList(filmController.getFilmById(1),filmController.getFilmById(2));

        Assertions.assertEquals(expectedFilms, filmController.getAllFilms());
    }
}