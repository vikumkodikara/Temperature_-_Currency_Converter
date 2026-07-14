package com.nima.tempconv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nima.tempconv.exception.UnauthorizedException;
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
    public TemperatureLog convert(
            @RequestParam double value,
            @RequestParam String unit,
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey) {
        return temperatureService.convertAndSave(value, unit, apiKey);
    }

    @GetMapping("/history")
    public List<TemperatureLog> history() {
        return temperatureService.getHistory();
    }

    // Clear all conversion history
    @DeleteMapping("/history")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearHistory() {
        temperatureService.clearHistory();
    }

    // Exercise 1: Safety check endpoint - returns plain text
    @GetMapping("/safety-check")
    public String safetyCheck(@RequestParam double value, @RequestParam String unit) {
        return temperatureService.safetyCheck(value, unit);
    }

    // Exercise 2: Filter history by input unit - returns JSON array
    @GetMapping("/history/filter")
    public List<TemperatureLog> filterHistory(@RequestParam String unit) {
        return temperatureService.getHistoryByUnit(unit);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public String handleInvalidUnit(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorized(UnauthorizedException ex) {
        return ex.getMessage();
    }
}
