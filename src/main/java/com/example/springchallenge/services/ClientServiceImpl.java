package com.example.springchallenge.services;

import com.example.springchallenge.dtos.*;
import com.example.springchallenge.exceptions.*;
import com.example.springchallenge.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<ClientDTO> getClients() {
        return clientRepository.getClientList();
    }

    @Override
    public void createClient(NewClientDTO client) throws Exception {

        validateClientData(client);
        clientRepository.addClient(client);
    }

    private void validateClientData(NewClientDTO newClient) throws Exception {

        if (newClient.getName() == null
        || newClient.getEmail() == null
        || newClient.getProvince() == null) {
            throw new InvalidClientDateException("Client information incomplete");
        }
        //ToDo validate email format

        List<ClientDTO> clientList = getClients();

        for (ClientDTO client : clientList) {
            if (client.getEmail().equals(newClient.getEmail())) {
                throw new EmailConflictException("User already exists");
            }
        }
    }
}
