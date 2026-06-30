package com.nima.tempconv.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nima.tempconv.model.TemperatureLog;

public interface TemperatureRepository extends MongoRepository<TemperatureLog, String> {

    // Exercise 2: Spring Data derived query method to find logs by input unit
    List<TemperatureLog> findByInputUnit(String inputUnit);
}
