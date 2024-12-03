package com.fultil.payload.response;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private ProductResponse productResponse;
    private String title;
    private Integer rating;
    private String message;
    private boolean hasOrderedProduct;
}
