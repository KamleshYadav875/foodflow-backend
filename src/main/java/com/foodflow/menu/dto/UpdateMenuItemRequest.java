package com.foodflow.menu.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMenuItemRequest {
    private Long id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Boolean isVeg;
    private Boolean isAvailable;
}
