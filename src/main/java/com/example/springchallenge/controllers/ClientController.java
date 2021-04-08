package com.example.springchallenge.controllers;

import com.example.springchallenge.dtos.*;
import com.example.springchallenge.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    public ResponseEntity<List<ClientDTO>> getClientList() {

        return new ResponseEntity<>(clientService.getClients(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<StatusDTO> makePurchase(@RequestBody NewClientDTO client) throws Exception {

        clientService.createClient(client);
        return new ResponseEntity<>(new StatusDTO(201, "Client created"), HttpStatus.CREATED);
    }
}

//ToDo improve validations and get by province
