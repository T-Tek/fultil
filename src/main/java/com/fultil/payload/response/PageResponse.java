package com.fultil.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int totalElements;
    private int totalPages;
    private boolean hasNext;
    private T contents;
}
