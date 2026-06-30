package com.nima.tempconv.service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.nima.tempconv.model.TemperatureLog;
import com.nima.tempconv.repository.TemperatureRepository;

@Service
public class TemperatureService {

    private final TemperatureRepository temperatureRepository;

    public TemperatureService(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    public TemperatureLog convertAndSave(double value, String unit) {
        String normalizedUnit = normalize(unit);
        double outputValue;
        String outputUnit;

        switch (normalizedUnit) {
            case "celsius":
                outputValue = (value * 1.8) + 32;
                outputUnit = "Fahrenheit";
                break;
            case "fahrenheit":
                outputValue = (value - 32) / 1.8;
                outputUnit = "Celsius";
                break;
            case "kelvin":
                outputValue = value - 273.15;
                outputUnit = "Celsius";
                break;
            default:
                throw new IllegalArgumentException("Unsupported unit. Use Celsius, Fahrenheit, or Kelvin.");
        }

        TemperatureLog log = new TemperatureLog(
                null,
                value,
                formatUnit(normalizedUnit),
                outputValue,
                outputUnit,
                Instant.now().toString());

        return temperatureRepository.save(log);
    }

    public List<TemperatureLog> getHistory() {
        return temperatureRepository.findAll();
    }

    // Exercise 1: Safety check business logic
    public String safetyCheck(double value, String unit) {
        String normalizedUnit = normalize(unit);
        double fahrenheitValue;

        switch (normalizedUnit) {
            case "celsius":
                fahrenheitValue = (value * 1.8) + 32;
                break;
            case "fahrenheit":
                fahrenheitValue = value;
                break;
            case "kelvin":
                fahrenheitValue = ((value - 273.15) * 1.8) + 32;
                break;
            default:
                throw new IllegalArgumentException("Unsupported unit. Use Celsius, Fahrenheit, or Kelvin.");
        }

        String displayUnit = normalizedUnit.equals("fahrenheit") ? "°F"
                : normalizedUnit.equals("celsius") ? "°C" : "K";

        if (fahrenheitValue >= 100) {
            return "Warning: " + value + displayUnit + " is dangerously HOT! Stay hydrated.";
        } else if (fahrenheitValue <= 32) {
            return "Warning: " + value + displayUnit + " is dangerously COLD! Stay warm.";
        } else {
            return "The temperature is comfortable and safe.";
        }
    }

    // Exercise 2: Get history filtered by input unit
    public List<TemperatureLog> getHistoryByUnit(String unit) {
        String formattedUnit = formatUnit(normalize(unit));
        return temperatureRepository.findByInputUnit(formattedUnit);
    }

    private String normalize(String unit) {
        if (unit == null) {
            return "";
        }

        String value = unit.trim().toLowerCase(Locale.ROOT);
        return switch (value) {
            case "c", "celcius", "celsius" -> "celsius";
            case "f", "fahrenheit" -> "fahrenheit";
            case "k", "kelvin" -> "kelvin";
            default -> value;
        };
    }

    private String formatUnit(String normalizedUnit) {
        return switch (normalizedUnit) {
            case "celsius" -> "Celsius";
            case "fahrenheit" -> "Fahrenheit";
            case "kelvin" -> "Kelvin";
            default -> normalizedUnit;
        };
    }
}
