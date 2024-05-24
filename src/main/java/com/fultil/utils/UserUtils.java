package com.fultil.utils;

import com.fultil.entity.User;
import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.payload.response.Response;
import com.fultil.payload.response.UserProductResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.Year;
import java.util.UUID;

@Slf4j
public class UserUtils {
        /**
         * Generates a unique ID for users.
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

    public static String generateSku() {
        log.info("Generating product ID");
        try {
            UUID uuid = UUID.randomUUID();
            String uniqueId = uuid.toString().toUpperCase().replaceAll("-", "").substring(0, 4);
            String prefix = "PROD-";
            return prefix + uniqueId;
        } catch (Exception e) {
            log.error("Error generating product ID: {}", e.getMessage());
            throw new RuntimeException("Error generating product ID", e);
        }
    }

        /**
         * Constructs a response object with the provided response code, message, and data.
         * @param responseCodeAndMessage The response code and message.
         * @param data The data to include in the response.
         * @return The constructed response object.
         */
        public static Response generateResponse(ResponseCodeAndMessage responseCodeAndMessage, Object data){
            Response response =  Response.builder()
                    .responseCode(responseCodeAndMessage.code)
                    .message(responseCodeAndMessage.message)
                    .data(data)
                    .build();
            log.info("Generated response: Response Code --> {}, Message --> {}, Data --> {}", response.getResponseCode(), response.getMessage(), response.getData());
            return response;
        }

    public static UserProductResponse generateUserResponse(ResponseCodeAndMessage responseCodeAndMessage, String userEmail, Object data) {
        UserProductResponse userProductResponse = UserProductResponse.builder()
                .userEmail(userEmail)
                .response(Response.builder()
                        .responseCode(responseCodeAndMessage.code)
                        .message(responseCodeAndMessage.message)
                        .data(data)
                        .build())
                .build();
        log.info("Generated response: Response Code --> {}, Message --> {}, Data --> {}", userProductResponse.getResponse().getResponseCode(), userProductResponse.getResponse().getMessage(), userProductResponse.getResponse().getData());
        return userProductResponse;
    }

    }


