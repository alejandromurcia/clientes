package com.uniandes.clientes.repositories;

import com.uniandes.clientes.models.ClientModel;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


@EnableScan
public interface ClientRepository extends CrudRepository<ClientModel, String> {

    ClientModel findByCedula(int cedula);

}