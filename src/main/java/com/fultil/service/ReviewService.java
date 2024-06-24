package com.fultil.service;

import com.fultil.payload.request.ReviewRequest;
import com.fultil.payload.response.PageResponse;
import com.fultil.payload.response.ReviewResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {
    void reviewProduct(ReviewRequest reviewRequest);
    PageResponse<List<ReviewResponse>> getAllReview(int rating, int page, int size);
}
