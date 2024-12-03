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
    @NotBlank(message = "Title is required")
    private String title;

    //    @NotNull(message = "Rating is required")
//    @Min(value = 1, message = "Rating must be at least 1")
//    @Max(value = 5, message = "Rating must be at most 5")
//    private Integer rating;
    @Pattern(regexp = "^[1-5]$", message = "Rating must be a number between 1 and 5")
    private int rating;

    @NotBlank(message = "Message is required")
    private String message;

    private Product product;
}
