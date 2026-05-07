package com.nima.tempconv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nima.tempconv.model.TemperatureLog;
import com.nima.tempconv.service.TemperatureService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/temperatures")
public class TemperatureController {

    private final TemperatureService temperatureService;

    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @PostMapping("/convert")
    public TemperatureLog convert(@RequestParam double value, @RequestParam String unit) {
        return temperatureService.convertAndSave(value, unit);
    }

    @GetMapping("/history")
    public List<TemperatureLog> history() {
        return temperatureService.getHistory();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public String handleInvalidUnit(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
