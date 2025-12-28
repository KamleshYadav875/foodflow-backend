package com.foodflow.order.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MyOrdersPageResponseDto {

    private List<MyOrderResponseDto> orders;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
}
