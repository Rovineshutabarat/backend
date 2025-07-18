package com.chelly.backend.service;

import com.chelly.backend.models.enums.WalletName;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class MidtransService {
    private final String MIDTRANS_SERVER_KEY;
    private final String MIDTRANS_ENVIRONMENT;
    private final String MIDTRANS_PAYOUT_API_URL;
    private final ObjectMapper objectMapper;
    private final OkHttpClient okHttpClient;

    public MidtransService(
            @Value("${midtrans.server-key}") String MIDTRANS_SERVER_KEY,
            @Value("${midtrans.environment}") String MIDTRANS_ENVIRONMENT,
            @Value("${midtrans.payouts.api-url-sandbox}") String MIDTRANS_PAYOUT_API_URL,
            ObjectMapper objectMapper,
            OkHttpClient okHttpClient
    ) {
        this.MIDTRANS_SERVER_KEY = MIDTRANS_SERVER_KEY;
        this.MIDTRANS_ENVIRONMENT = MIDTRANS_ENVIRONMENT;
        this.MIDTRANS_PAYOUT_API_URL = MIDTRANS_PAYOUT_API_URL;
        this.objectMapper = objectMapper;
        this.okHttpClient = okHttpClient;
    }

    public Map<String, Object> createGopayCharge(String orderId, Double amount) throws IOException {
        String credentials = MIDTRANS_SERVER_KEY + ":";
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

        Map<String, Object> payload = new HashMap<>();
        payload.put("payment_type", "gopay");

        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", orderId);
        transactionDetails.put("gross_amount", amount);
        payload.put("transaction_details", transactionDetails);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                objectMapper.writeValueAsString(payload)
        );

        Request request = new Request.Builder()
                .url("https://api.sandbox.midtrans.com/v2/charge")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", basicAuth)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Midtrans Charge Failed: " + responseBody);
            }
            return objectMapper.readValue(responseBody, Map.class);
        }
    }

}
