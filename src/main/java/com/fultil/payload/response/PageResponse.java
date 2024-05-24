package com.fultil.payload.response;

import com.fultil.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageResponse<T> {
    private int totalElements;
    private int totalPages;
    private boolean hasNext;
    private T contents;
}
