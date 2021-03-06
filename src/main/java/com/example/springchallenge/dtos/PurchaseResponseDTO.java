package com.example.springchallenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponseDTO {

    private Long shoppingCartId;
    private TicketDTO ticket;
    private StatusDTO statusCode;
}
