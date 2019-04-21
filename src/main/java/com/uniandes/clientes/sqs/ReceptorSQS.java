package com.uniandes.clientes.sqs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class ReceptorSQS {

	private static ReceptorSQS instancia;
	
	private AmazonSQS sqs;
	
    @Value("${amazon.sqs.region}")
    private static String amazonAWSRegion;

    @Value("${amazon.sqs.accesskey}")
    private static String amazonAWSAccessKey;

    @Value("${amazon.sqs.secretkey}")
    private static String amazonAWSSecretKey;
	
	private ReceptorSQS() {
        /*
         * Create a new instance of the builder with all defaults (credentials
         * and region) set automatically. For more information, see
         * Creating Service Clients in the AWS SDK for Java Developer Guide.
         */
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(
				amazonAWSAccessKey, //access_key_id
				amazonAWSSecretKey);//secret_key_id
		
		sqs = AmazonSQSClientBuilder.standard().withRegion(amazonAWSRegion).
				withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
	}
	
	public static ReceptorSQS getInstance() {
		if (instancia == null) {
			instancia = new ReceptorSQS();
		}
		return instancia;
	}
	
	public String getColaPorNombre(String nombreCola) {
        return sqs.getQueueUrl(nombreCola).getQueueUrl();
	}
	
	public List<Message> recibirMensajes(String myQueueUrl) {
		try {
			// Receive messages.
			System.out.println("[ReceptorSQS] Receiving messages from Queue " + myQueueUrl + "\n");
			StringBuffer sb = new StringBuffer();
			final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
			final List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
			for (Message message : messages) {
				sb.append("Message\n");
				sb.append("  MessageId:     " + message.getMessageId() + "\n");
				sb.append("  ReceiptHandle: " + message.getReceiptHandle() + "\n");
				sb.append("  MD5OfBody:     " + message.getMD5OfBody() + "\n");
				sb.append("  Body:          " + message.getBody() + "\n");

				for (java.util.Map.Entry<String, String> entry : message.getAttributes().entrySet()) {
					sb.append("Attribute\n");
					sb.append("  Name:  " + entry.getKey() + "\n");
					sb.append("  Value: " + entry.getValue() + "\n");
				}
			}
			if (messages != null && messages.size() > 0)
				System.out.println("[ReceptorSQS] " + sb.toString());
			return messages;
		} catch (AmazonServiceException ase) {
			StringBuffer sb = new StringBuffer();
			sb.append(
				"Caught an AmazonServiceException, which means your request made it to Amazon SQS, but was rejected with an error response for some reason.\n");
			sb.append("Error Message:    " + ase.getMessage() + "\n");
			sb.append("HTTP Status Code: " + ase.getStatusCode() + "\n");
			sb.append("AWS Error Code:   " + ase.getErrorCode() + "\n");
			sb.append("Error Type:       " + ase.getErrorType() + "\n");
			sb.append("Request ID:       " + ase.getRequestId() + "\n");
			System.out.println("[ReceptorSQS] " + sb.toString());
			
			return new ArrayList<Message>();
		} catch (final AmazonClientException ace) {
			System.out.println("[ReceptorSQS] Caught an AmazonClientException, which means "
					+ "the client encountered a serious internal problem while "
					+ "trying to communicate with Amazon SQS, such as not " 
					+ "being able to access the network. " + ace.getMessage());
			
			return new ArrayList<Message>();
		}
	}
	
	public static void main(String[] args) {
		ReceptorSQS.amazonAWSRegion = "us-east-1";
		ReceptorSQS.amazonAWSAccessKey = "AKIA3PJLLOCTTWGSE6JB";
		ReceptorSQS.amazonAWSSecretKey = "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";
		ReceptorSQS rec = ReceptorSQS.getInstance();
		
		String myQueueUrl = rec.getColaPorNombre("XYZqueue");
		while (true) {
			List<Message> mensajes = rec.recibirMensajes(myQueueUrl);
			if (!mensajes.isEmpty()) {
				for (Message message : mensajes) {
					System.out.println("Borrando mensaje con ID: " + message.getMessageId());
					//rec.borrarMensajeDeCola(myQueueUrl, message);
				}
			} else {
				System.out.println("No se recibió ningun mensaje");
			}
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
