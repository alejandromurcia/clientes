package com.uniandes.clientes.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.uniandes.clientes.models.ClientModel;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientSQSService {

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private ClientService clientService;

    private String getColaPorNombre(String nombreCola) {

        return amazonSQS.getQueueUrl(nombreCola).getQueueUrl();
    }

    private void borrarMensajeDeCola(String myQueueUrl, Message message) {
        amazonSQS.deleteMessage(new DeleteMessageRequest(myQueueUrl, message.getReceiptHandle()));
    }

    public List<Message> recibirMensajes() {
        try {
            String myQueueUrl = getColaPorNombre("XYZqueue");
            System.out.println("[ReceptorSQS] Receiving messages from Queue " + myQueueUrl + "\n");
            final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            final List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
            for (Message message : messages) {

                ClientModel clientModel = new ClientModel();
                Object obj = new org.json.simple.parser.JSONParser().parse(message.getBody());
                org.json.simple.JSONObject jo = (org.json.simple.JSONObject) obj;
                Long cedula = (Long) jo.get("cedula");
                clientModel.setCedula(cedula.intValue());
                clientModel.setName((String) jo.get("name"));
                clientModel.setPhone((String) jo.get("phone"));
                clientModel.setEmail((String) jo.get("email"));

                clientService.saveClient(clientModel);
                borrarMensajeDeCola(myQueueUrl, message);
            }

            if (messages != null && messages.size() > 0)
                System.out.println("[ReceptorSQS] null");
            return messages;
        } catch (AmazonServiceException ase) {
            System.out.println("[ReceptorSQS] fail");

            return new ArrayList<Message>();
        } catch (final AmazonClientException ace) {

            System.out.println("[ReceptorSQS] fail amazon client");
            return new ArrayList<Message>();
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<Message>();
        }
    }
}