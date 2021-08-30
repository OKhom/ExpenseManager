package com.okdev.ems.config.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okdev.ems.exceptions.EmsBadRequestException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class ExchangeRates {

    @Value("${api.access.key}")
    private String apiAccessKey;

    private static final String URI = "http://api.exchangeratesapi.io/v1/latest";

    public Double currentRate(String currencyDefault, String currencyCategory) throws IOException {
        try {
            ObjectMapper exchangeRates = new ObjectMapper();
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            String requestUrl = Objects.requireNonNull(HttpUrl.parse(URI)).newBuilder()
                    .addQueryParameter("access_key", apiAccessKey)
                    .addQueryParameter("symbols", currencyDefault + "," + currencyCategory)
                    .build()
                    .toString();
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            JsonNode jn = exchangeRates.readTree(Objects.requireNonNull(response.body()).string());
            Double currentRate = jn.get("rates").get(currencyDefault).asDouble() / jn.get("rates").get(currencyCategory).asDouble();
            if (response != null) response.close();
            return currentRate;
        } catch (NullPointerException npe) {
            throw new EmsBadRequestException("Exchange Rate Request: bad request or response is null");
        }
    }

}
