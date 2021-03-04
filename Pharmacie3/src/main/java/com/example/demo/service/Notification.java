
  package com.example.demo.service;
  
 

  import java.util.Collection;
  import java.util.List;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.mail.MailException;
  import org.springframework.mail.SimpleMailMessage;
  import org.springframework.mail.javamail.JavaMailSender;
  import org.springframework.stereotype.Service;

import com.example.demo.entities.Client;
import com.example.demo.entities.Commande;

 
  @Service
  public class Notification {


  private JavaMailSender javaMailSender;

  @Autowired
  public Notification(JavaMailSender javaMailSender) {
  	super();
  	this.javaMailSender = javaMailSender;
  } 


  public void sendConfirmation(Commande com) throws MailException {
  	SimpleMailMessage mail = new SimpleMailMessage();
  	mail.setTo(com.getClient().getEmail());
  	mail.setFrom("suptechmiage2018@gmail.com");
  	mail.setSubject("Confirmation de la commande "+ com.getId()+".");
  	mail.setText("Bonjour Mr/Mme "+com.getClient().getNom()+" "+ com.getClient().getPrenom() +". Votre commande Numéro : "+com.getId()+" a bien été enrégistré . Un de nos"
  			+ " agents prendrera contacte avec vous su ce numéro  : " +com.getClient().getPhoneClient()+" . Merci et à Bientot"  );
  	javaMailSender.send(mail);

  }

 public void sendMdp(Client client,String mdp) throws MailException{
	 SimpleMailMessage mail = new SimpleMailMessage();
	 mail.setTo(client.getEmail());
	 mail.setFrom("suptechmiage2018@gmail.com");
	 mail.setSubject("Mot de passe");
	 mail.setText("Bonjour Mr/Mme "+client.getNom()+" "+ client.getPrenom() +". Votre nouveau mot de passe est : " + mdp );
	  	javaMailSender.send(mail);

 }

 public void confirmeCommande(Commande com) throws MailException{
	 SimpleMailMessage mail = new SimpleMailMessage();
	 mail.setTo(com.getClient().getEmail());
	 mail.setFrom("suptechmiage2018@gmail.com");
	 mail.setSubject("Confirmation de votre commande");
	 mail.setText("Bonjour Mr/Mme "+com.getClient().getNom()+" "+ com.getClient().getPrenom() +". Votre commande N:"
	 +com.getId() +" a bien été confirmée" );
	  	javaMailSender.send(mail);

 }
 
 public void jesuisla(List<Commande> com , Client client) throws MailException{
	 
	 String comString = "" ;
	 for (Commande commande : com) {
	 	comString +=  commande.getId().toString() + ";";
		
	}
	 SimpleMailMessage mail = new SimpleMailMessage();
	 mail.setTo("saibouibrahim7@gmail.com");
	 mail.setFrom("suptechmiage2018@gmail.com");
	 mail.setSubject("Arrivée du client");
	 mail.setText("Le client " + client.getNom()+" "+client.getPrenom() + " est la  pour la/les commande(s) " + comString + "." );
	  	javaMailSender.send(mail);

 }



  }