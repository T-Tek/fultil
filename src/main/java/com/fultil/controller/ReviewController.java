package com.fultil.controller;

import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.payload.request.ReviewRequest;
import com.fultil.payload.response.Response;
import com.fultil.service.ReviewService;
import com.fultil.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Response reviewProduct(@RequestBody ReviewRequest reviewRequest){
        reviewService.reviewProduct(reviewRequest);
        return UserUtils.generateResponse(ResponseCodeAndMessage.SUCCESS, null);
    }
}
