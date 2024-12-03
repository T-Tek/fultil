package com.fultil.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageResponse<T>  {
    private int totalElements;
    private int totalPages;
    private boolean hasNext;
    private Map<?, ?> content;
}
