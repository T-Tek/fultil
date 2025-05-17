package com.fultil.utils;

import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.exceptions.BadRequestException;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.model.User;
import com.fultil.payload.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        return new Response(responseCodeAndMessage.status.value(), responseCodeAndMessage.name(), data);
    }

    public static ResponseEntity<Response> getResponse(ResponseCodeAndMessage responseCodeAndMessage, Object data) {
        Response response = new Response(responseCodeAndMessage.status.value(), responseCodeAndMessage.name(), data);
        return new ResponseEntity<>(response, responseCodeAndMessage.status);
    }

    public static void setCurrentUser(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("No authenticated user");
        }
        return (User) auth.getPrincipal();
    }

}


