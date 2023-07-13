package ru.yandex.practicum.filmorate.exeption;

public class NotFoundException extends RuntimeException {

    final int id;

    public NotFoundException(String message, int id) {
        super(message);
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
