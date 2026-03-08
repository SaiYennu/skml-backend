package com.skml.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    public void sendLeadMail(String subject, String body) {

        try {
            // JSON safe body
            body = body.replace("\\", "\\\\")
                       .replace("\"", "\\\"")
                       .replace("\n", "\\n")
                       .replace("\r", "");

            URL url = new URL("https://api.brevo.com/v3/smtp/email");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("api-key", apiKey);
            con.setDoOutput(true);

            String jsonPayload =
                    "{"
                  + "\"sender\":{\"name\":\"SKML Website\",\"email\":\"srikanakamahalakshmifabricatio@gmail.com\"},"
                  + "\"to\":[{\"email\":\"vinaychukka3@gmail.com\"},{\"email\":\"saiyennu67@gmail.com\"}],"
                  + "\"subject\":\"" + subject + "\","
                  + "\"textContent\":\"" + body + "\""
                  + "}";

            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("Brevo response code: " + con.getResponseCode());

        } catch (Exception e) {
            throw new RuntimeException("Brevo email send failed", e);
        }
    }
}
