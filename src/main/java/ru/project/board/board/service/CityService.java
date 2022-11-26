package ru.project.board.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.board.board.entity.City;
import ru.project.board.board.exception.CityNotFoundException;
import ru.project.board.board.repository.CityRepo;

import java.util.UUID;

@Service
public class CityService {

    @Autowired
    private CityRepo cityRepo;

    public City getCityById(UUID id) throws CityNotFoundException {
        return cityRepo.findById(id).orElseThrow(() -> new CityNotFoundException("City not found"));
    }

    public Iterable<City> getListOfCities() {
        return cityRepo.findAll();
    }
}
