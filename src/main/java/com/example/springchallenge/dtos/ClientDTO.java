package com.example.springchallenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private Long clientId;
    private String name;
    private String email;
    private String province;

    public String toCsvRow() {
        return String.join(",", name, email, province);
    }
}