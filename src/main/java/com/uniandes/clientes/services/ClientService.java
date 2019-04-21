package com.uniandes.clientes.services;

import com.uniandes.clientes.repositories.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uniandes.clientes.models.ClientModel;

@Service
public class ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    public ClientModel getClient(int cedula) {
        return this.clientRepository.findByCedula(cedula);
    }

    public ClientModel saveClient(ClientModel clienteModel) {

        return this.clientRepository.save(clienteModel);
    }
}