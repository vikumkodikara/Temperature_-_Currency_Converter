package com.nima.tempconv.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nima.tempconv.model.TemperatureLog;

public interface TemperatureRepository extends MongoRepository<TemperatureLog, String> {
}
