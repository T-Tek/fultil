package com.fultil.payload.request;

import com.fultil.model.Product;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewRequest {
    private Long productId;

    @NotBlank(message = "Title is required")
    private String title;

    @Pattern(regexp = "^[1-5]$", message = "Rating must be a number between 1 and 5")
    private int rating;

    @NotBlank(message = "Message is required")
    private String message;
}
