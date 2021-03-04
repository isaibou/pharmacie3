package com.example.demo.service;

import java.io.IOException;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.demo.entities.Commande;
import com.example.demo.entities.CommandeOrdonnance;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.SmsSubmissionResponseMessage;
import com.nexmo.client.sms.messages.TextMessage;

@Service
public class Vonage {
	
	 
	public void sendSmsCom(Commande com) {
	
	NexmoClient client2 = new NexmoClient.Builder()
  		  .apiKey("90cda218")
  		  .apiSecret("IdCIdgQGiEE9yjTy")
  		  .build();

  		String messageText = "Nous avons bien revu votre commande"+com.getId()+"Un de nos agenst vous contactera "
  				+"pour la validation ";
  		TextMessage message1 = new TextMessage("Pharmacie NIBIGHOLET", "212630901365", messageText);
  		
  		SmsSubmissionResponse response;
			try {
				response = client2.getSmsClient().submitMessage(message1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NexmoClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
}
	
	 
		public void sendSmsOrd(CommandeOrdonnance com) {
		
		NexmoClient client2 = new NexmoClient.Builder()
	  		  .apiKey("90cda218")
	  		  .apiSecret("IdCIdgQGiEE9yjTy")
	  		  .build();

	  		String messageText = "Nous avons bien revu votre commande"+com.getId()+"Un de nos agenst vous contactera "
	  				+"pour la validation ";
	  		TextMessage message1 = new TextMessage("Pharmacie NIBIGHOLET", "212630901365", messageText);
	  		
	  		SmsSubmissionResponse response;
				try {
					response = client2.getSmsClient().submitMessage(message1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NexmoClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	}
	
	
	
}

