package com.example.springchallenge.repositories;

import com.example.springchallenge.dtos.ClientDTO;
import com.example.springchallenge.dtos.NewClientDTO;

import java.util.List;

public interface ClientRepository {

    List<ClientDTO> getClientList();

    void addClient(NewClientDTO client);

    void updateDatabase();
}