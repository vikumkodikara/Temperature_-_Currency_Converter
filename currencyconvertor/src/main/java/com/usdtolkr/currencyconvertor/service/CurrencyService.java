package com.usdtolkr.currencyconvertor.service;

import com.usdtolkr.currencyconvertor.exception.UnauthorizedException;
import com.usdtolkr.currencyconvertor.model.ApiKey;
import com.usdtolkr.currencyconvertor.model.CurrencyLog;
import com.usdtolkr.currencyconvertor.repository.ApiKeyRepository;
import com.usdtolkr.currencyconvertor.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final ApiKeyRepository apiKeyRepository;
    
    // Constant exchange rate for demonstration (e.g., 1 USD = 300 LKR)
    private static final double USD_TO_LKR_RATE = 300.0;

    public void validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new UnauthorizedException("Header X-API-KEY is missing");
        }

        ApiKey record = apiKeyRepository.findByKeyValue(apiKey.trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid API key"));

        if (!record.isActive()) {
            throw new UnauthorizedException("API key is inactive or expired");
        }
    }

    public CurrencyLog convertAndSave(double amount, String apiKey) {
        validateApiKey(apiKey);

        double result = amount * USD_TO_LKR_RATE;

        CurrencyLog log = new CurrencyLog();
        log.setInputAmount(amount);
        log.setInputCurrency("USD");
        log.setOutputAmount(result);
        log.setOutputCurrency("LKR");
        log.setExchangeRate(USD_TO_LKR_RATE);
        log.setTimestamp(LocalDateTime.now().toString());

        return currencyRepository.save(log);
    }

    public List<CurrencyLog> getAllLogs() {
        return currencyRepository.findAll();
    }

    // Exercise 1: Rate check business logic
    public String rateCheck(double amount) {
        double result = amount * USD_TO_LKR_RATE;

        if (amount >= 10000) {
            return "Warning: $" + amount + " USD (= " + result + " LKR) is a LARGE transaction! Consider splitting it.";
        } else if (amount <= 1) {
            return "Note: $" + amount + " USD (= " + result + " LKR) is a very SMALL amount. Fees may exceed the value.";
        } else {
            return "The conversion of $" + amount + " USD to " + result + " LKR is a standard transaction.";
        }
    }

    // Exercise 2: Get history filtered by input currency
    public List<CurrencyLog> getHistoryByCurrency(String currency) {
        String normalizedCurrency = currency.trim().toUpperCase();
        return currencyRepository.findByInputCurrency(normalizedCurrency);
    }
}
