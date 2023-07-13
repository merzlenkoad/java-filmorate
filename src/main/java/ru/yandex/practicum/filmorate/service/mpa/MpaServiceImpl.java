package ru.yandex.practicum.filmorate.service.mpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MpaServiceImpl implements MpaService {
    private MpaStorage mpaStorage;

    @Override
    public Collection<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa().stream().sorted(Comparator.comparing(Mpa::getId)).collect(Collectors.toList());
    }

    @Override
    public Mpa getMpaById(Integer id) {
        return mpaStorage.getMpaById(id);
    }
}
