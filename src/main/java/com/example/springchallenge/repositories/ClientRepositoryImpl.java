package com.example.springchallenge.repositories;

import com.example.springchallenge.dtos.ClientDTO;
import com.example.springchallenge.dtos.NewClientDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private List<ClientDTO> clientList;

    private final AtomicLong idCounter = new AtomicLong(1);

    public ClientRepositoryImpl() {
        clientList = loadDatabase();
    }

    @Override
    public List<ClientDTO> getClientList() {
        return clientList;
    }

    @Override
    public void addClient(NewClientDTO client) {
        clientList.add(new ClientDTO(idCounter.getAndIncrement(), client.getName(), client.getEmail(), client.getProvince()));
        updateDatabase();
    }

    @Override
    public void updateDatabase() {

        String recordAsCsv = clientList.stream()
                .map(ClientDTO::toCsvRow)
                .collect(Collectors.joining(System.getProperty("line.separator")));

        try {
            // File path
            FileWriter writer = new FileWriter("src/main/resources/dbClientes.csv");

            // Add headers
            writer.append("name,email,province\n");

            // Add content
            writer.append(recordAsCsv);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Parse csv file data
    private List<ClientDTO> loadDatabase() {

        List<ClientDTO> records = new ArrayList<>();

        try {

            Resource resource = new ClassPathResource("dbClientes.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String row;

            // Skip headers
            reader.readLine();

            while ((row = reader.readLine()) != null) {
                String[] data = row.split(",");

                String name = data[0];
                String email = data[1];
                String province = data[2];

                records.add(new ClientDTO(idCounter.getAndIncrement(), name, email, province));
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }
}
