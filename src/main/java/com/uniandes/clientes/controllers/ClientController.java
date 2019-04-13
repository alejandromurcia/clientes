package com.uniandes.clientes.controllers;

import com.uniandes.clientes.models.ClientModel;
import com.uniandes.clientes.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller provides the access for the receiver end point
 *
 */
@Controller
@EnableAutoConfiguration
public class ClientController {
		
	@Autowired
	private ClientService clientService;
		
	@RequestMapping(value = "/cliente", method = RequestMethod.GET)
    @ResponseBody
    public ClientModel getClientById(@RequestParam int clientId) {

		return this.clientService.getClient(clientId);
    }


	@RequestMapping(value = "/cliente", method = RequestMethod.POST)
	@ResponseBody
	ClientModel createClient(@RequestBody ClientModel clienteModel)  {

		return this.clientService.saveClient(clienteModel);
	}
}