package com.usdtolkr.currencyconvertor.repository;

import com.usdtolkr.currencyconvertor.model.CurrencyLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends MongoRepository<CurrencyLog, String> {

    // Exercise 2: Spring Data derived query method to find logs by input currency
    List<CurrencyLog> findByInputCurrency(String inputCurrency);
}
