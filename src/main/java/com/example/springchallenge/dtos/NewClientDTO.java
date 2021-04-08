package com.example.springchallenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewClientDTO {

    private String name;
    private String email;
    private String province;
}
