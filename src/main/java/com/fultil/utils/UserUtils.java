package com.fultil.utils;

import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.model.User;
import com.fultil.payload.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Year;
import java.util.UUID;

@Slf4j
public class UserUtils {
    /**
     * Generates a unique ID for users.
     *
     * @return The generated unique ID.
     */
    public static String generateUniqueId() {
        log.info("Generating unique ID");
        try {
            UUID uuid = UUID.randomUUID();
            String uniqueId = uuid.toString().toUpperCase().replaceAll("-", "").substring(0, 4);
            Year currentYear = Year.now();
            String prefix = "FTL" + currentYear;
            return prefix + uniqueId;
        } catch (Exception e) {
            log.error("Error generating unique ID: {}", e.getMessage());
            throw new RuntimeException("Error generating unique ID", e);
        }
    }

    public static String generateSku(String productName) {
        log.info("Generating product skuCode");
        try {
            String sku = productName.replace(" ", "-");

            if (!productName.contains(" ")) {
                UUID uuid = UUID.randomUUID();
                String uniqueId = uuid.toString().toUpperCase().replace("-", "").substring(0, 4);
                sku += uniqueId;
                log.info("UUID generated and appended: {}", uniqueId);
            }

            return sku;
        } catch (Exception e) {
            log.error("Error generating product ID: {}", e.getMessage(), e);
            throw new RuntimeException("Error generating product ID", e);
        }
    }

    /**
     * Constructs a response object with the provided response code, message, and data.
     *
     * @param responseCodeAndMessage The response code and message.
     * @param data                   The data to include in the response.
     * @return The constructed response object.
     */
    public static Response generateResponse(ResponseCodeAndMessage responseCodeAndMessage, Object data) {
        Response response = Response.builder()
                .responseCode(responseCodeAndMessage.code)
                .message(responseCodeAndMessage.message)
                .data(data)
                .build();
        log.info("Generated response: Response Code --> {}, Message --> {}, Data --> {}", response.getResponseCode(), response.getMessage(), response.getData());
        return response;
    }
    public static User getAuthenticatedUser() {
        log.info("Checking if user is authenticated");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User is not authenticated, cannot perform operation");
        }
        return (User) authentication.getPrincipal();
    }
}


