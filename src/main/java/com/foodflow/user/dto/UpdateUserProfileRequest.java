package com.foodflow.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserProfileRequest {
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipcode;

}
