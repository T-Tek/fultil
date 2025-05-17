package com.fultil.service.impl;

import com.fultil.model.Review;
import com.fultil.model.User;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.payload.request.ReviewRequest;
import com.fultil.payload.response.PageResponse;
import com.fultil.payload.response.ReviewResponse;
import com.fultil.repository.ReviewRepository;
import com.fultil.service.ProductService;
import com.fultil.service.ReviewService;
import com.fultil.service.UserService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;
    private final ProductService productService;
    private final UserService userService;

    @Override
    public void reviewProduct(ReviewRequest reviewRequest) {
        Long productId = productService.findProductById(reviewRequest.getProductId()).getId();
   //     String name = userService.getUserById(reviewRequest.get)

        User user = UserUtils.getCurrentUser();
        Review newReview = Review.builder()
                .title(reviewRequest.getTitle())
                .message(reviewRequest.getMessage())
                .rating(reviewRequest.getRating())
                .productId(productId)
                .userId(user.getId())
                .build();
        repository.save(newReview);
    }

    @Override
    public PageResponse<List<ReviewResponse>> getAllReview(int rating, int page, int size) {
        log.info("Request to get product review/s");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Review> reviewPage = repository.findAllByRating(rating, pageable);
        if (reviewPage.isEmpty()){
            throw new ResourceNotFoundException("No review found");
        }
        List<ReviewResponse> reviews = new ArrayList<>();
        for (Review review : reviewPage){
            reviews.add(mapToReviewResponse(review));
        }
        return new PageResponse<>(
                reviewPage.getNumberOfElements(),
                reviewPage.getTotalPages(),
                reviewPage.hasNext(),
                Map.of("reviews", reviews)
        );
    }

    public static ReviewResponse mapToReviewResponse(Review review){
        return ReviewResponse.builder()
                .title(review.getTitle())
                .message(review.getMessage())
                .rating(review.getRating())
                .build();
    }
}
