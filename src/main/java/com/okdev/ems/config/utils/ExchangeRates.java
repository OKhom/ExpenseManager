package com.okdev.ems.config.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ExchangeRates {

    @Value("${api.access.key}")
    private String apiAccessKey;

    private final String uri = "http://api.exchangeratesapi.io/v1/latest";

    public Double currentRate(String currencyDefault, String currencyCategory) throws IOException {
        ObjectMapper exchangeRates = new ObjectMapper();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String requestUrl = HttpUrl.parse(uri).newBuilder()
                .addQueryParameter("access_key", apiAccessKey)
                .addQueryParameter("symbols", currencyDefault + "," + currencyCategory)
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        JsonNode jn = exchangeRates.readTree(response.body().string());
        Double currentRate = jn.get("rates").get(currencyDefault).asDouble() / jn.get("rates").get(currencyCategory).asDouble();
        if (response != null) response.close();
        return currentRate;
    }

}