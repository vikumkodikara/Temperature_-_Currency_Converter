package com.usdtolkr.currencyconvertor.controller;

import com.usdtolkr.currencyconvertor.exception.UnauthorizedException;
import com.usdtolkr.currencyconvertor.model.CurrencyLog;
import com.usdtolkr.currencyconvertor.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    @PostMapping("/convert")
    public CurrencyLog convertCurrency(@RequestParam(name = "usdAmount", required = false) Double usdAmount,
                                       @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
                                       HttpServletRequest request) {
        // tolerate a common typo that was seen in Postman: "usdAmout"
        if (usdAmount == null) {
            String alt = request.getParameter("usdAmout");
            if (alt != null && !alt.isBlank()) {
                try {
                    usdAmount = Double.valueOf(alt);
                } catch (NumberFormatException ex) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid numeric value for 'usdAmount'");
                }
            }
        }

        if (usdAmount == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required request parameter 'usdAmount'");
        }

        return currencyService.convertAndSave(usdAmount, apiKey);
    }

    @GetMapping("/history")
    public List<CurrencyLog> getHistory() {
        return currencyService.getAllLogs();
    }

    // Clear all conversion history
    @DeleteMapping("/history")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearHistory() {
        currencyService.clearHistory();
    }

    // Exercise 1: Rate check endpoint - returns plain text
    @GetMapping("/rate-check")
    public String rateCheck(@RequestParam double usdAmount) {
        return currencyService.rateCheck(usdAmount);
    }

    // Exercise 2: Filter history by input currency - returns JSON array
    @GetMapping("/history/filter")
    public List<CurrencyLog> filterHistory(@RequestParam String currency) {
        return currencyService.getHistoryByCurrency(currency);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public String handleInvalidArgument(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorized(UnauthorizedException ex) {
        return ex.getMessage();
    }
}
