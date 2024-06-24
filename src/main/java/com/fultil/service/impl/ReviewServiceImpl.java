package com.fultil.service.impl;

import com.fultil.entity.Review;
import com.fultil.entity.User;
import com.fultil.exceptions.ResourceNotFoundException;
import com.fultil.payload.request.ReviewRequest;
import com.fultil.payload.response.PageResponse;
import com.fultil.payload.response.ReviewResponse;
import com.fultil.repository.ReviewRepository;
import com.fultil.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;
    @Override
    public void reviewProduct(ReviewRequest reviewRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User is not authenticated, can not review product");
        }
        User user = (User) authentication.getPrincipal();
        Review newReview = Review.builder()
                .title(reviewRequest.getTitle())
                .message(reviewRequest.getMessage())
                .rating(reviewRequest.getRating())
                .product(reviewRequest.getProduct())
                .user(user)
                .build();
        repository.save(newReview);
    }

    @Override
    public PageResponse<List<ReviewResponse>> getAllReview(int rating, int page, int size) {
        log.info("Request to get product review/s");

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC);

        Page<Review> reviewPage = repository.findAllByRating(rating, pageable);
        if (reviewPage.isEmpty()){
            throw new ResourceNotFoundException("No review found");
        }
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Review review : reviewPage){
            reviewResponses.add(mapToReviewResponse(review));
        }
        return new PageResponse<>(
                reviewPage.getNumberOfElements(),
                reviewPage.getTotalPages(),
                reviewPage.hasNext(),
                reviewResponses
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
