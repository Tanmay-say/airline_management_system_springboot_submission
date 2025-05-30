package com.example.airline_management_system.data;


import lombok.Builder;
import lombok.Data;


@Data
@Builder(toBuilder = true)
public class UserInfo {
    private String id;
    private String name;
    private String email;
    private String address;
    private String phone;
}
