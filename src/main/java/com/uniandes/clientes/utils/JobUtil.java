package com.uniandes.clientes.utils;

import com.amazonaws.services.sqs.AmazonSQS;
import com.uniandes.clientes.services.ClientSQSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JobUtil {
    private static final Logger log = LoggerFactory.getLogger(JobUtil.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private ClientSQSService clientSQSService;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        clientSQSService.recibirMensajes();
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}