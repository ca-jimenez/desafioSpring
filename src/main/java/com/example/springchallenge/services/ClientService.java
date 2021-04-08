package com.example.springchallenge.services;

import com.example.springchallenge.dtos.*;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getClients();

    void createClient(NewClientDTO client) throws Exception;

}
