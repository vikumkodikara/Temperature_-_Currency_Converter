package com.usdtolkr.currencyconvertor.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.usdtolkr.currencyconvertor.model.ApiKey;
import com.usdtolkr.currencyconvertor.repository.ApiKeyRepository;

@Component
public class ApiKeySeeder implements ApplicationRunner {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeySeeder(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (apiKeyRepository.count() > 0) {
            return;
        }

        apiKeyRepository.save(new ApiKey(null, "SUPER-SECRET-DEV-KEY-123", "Dev Team", true));
        apiKeyRepository.save(new ApiKey(null, "EXPIRED-HACKER-KEY-999", "Blocked User", false));
    }
}
