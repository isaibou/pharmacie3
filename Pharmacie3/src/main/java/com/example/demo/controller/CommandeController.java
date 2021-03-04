package com.example.demo.controller;

import java.awt.color.CMMException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.thymeleaf.expression.Dates;
import org.w3c.dom.ls.LSInput;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.CommandeOrdonnanceRepository;
import com.example.demo.dao.CommandeRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Commande;
import com.example.demo.entities.CommandeOrdonnance;
import com.example.demo.service.Notification;
import com.example.demo.service.ServiceUploadAWS;
import com.example.demo.service.Vonage;


import com.telesign.MessagingClient;
import com.telesign.RestClient;

import com.nexmo.client.*;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.SmsSubmissionResponseMessage;
import com.nexmo.client.sms.messages.TextMessage;
import com.nexmo.*;

@Controller
public class CommandeController {
	
	@Autowired
	private CommandeRepository commandeRepository;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	CommandeOrdonnanceRepository commandeOrdonnanceRepository;
	
	@Autowired
	private AmazonS3 amazonS3;
	@Autowired
	private Vonage sendSMS;
	
	@Value("${aws.s3.bucket}")
	private String s3bucket;
	
	@RequestMapping("/commander")
	private String Commander(Model model) {
		model.addAttribute("commande",new Commande());
		return "commande";
	}
	
	@Autowired
	private Notification notification;
	
	@Autowired
	private ServiceUploadAWS uploadtoS3;
	@Autowired
	private Vonage vonage;
	
	@RequestMapping("/allCommande")
	private String allCommande(Model model) {
		 List<Commande> nouvelle = commandeRepository.findByStatus("Nouvelle");
		 List<Commande> confirm = commandeRepository.findByStatus("Confirmée");
		 List<Commande> livree = commandeRepository.findByStatus("Livrée");
		 List<Commande> rejetee = commandeRepository.findByStatus("Rejetée");
		 List<Commande> toutes = commandeRepository.findAll();
		 
		 model.addAttribute("nouvelle", nouvelle);
		 model.addAttribute("confirmee", confirm);
		 model.addAttribute("livree", livree);
		 model.addAttribute("toutes", toutes);
		 model.addAttribute("rejetee", rejetee);
		 model.addAttribute("nbreNew", nouvelle.size());
		 model.addAttribute("nbrConfirm", confirm.size());
		 model.addAttribute("nbreLivree", livree.size());
		 model.addAttribute("nbreTt" , toutes.size());
		 model.addAttribute("nbreRejette", rejetee.size());
		 
		 model.addAttribute("standardDate", new Date());
		 model.addAttribute("localDateTime", LocalDateTime.now());
		 model.addAttribute("localDate", LocalDate.now());
		 model.addAttribute("timestamp", Instant.now());
		 
		return "allCommande";
	}
	
	
	@RequestMapping("/saveCommande")
	private String saveCommande(Commande com,Model model, Authentication auth ) throws IOException {
		String gard = auth.getName();
		Client client = clientRepository.getOne(gard);
		com.setClient(client);
		com.setStatus("Nouvelle");
		com.setDateCommande(new Date());;
		
		
		commandeRepository.save(com);

		String avecEmail = "  Nous avons bien recu votre commande : "+com.getOrdonnance()+ ". Un email de confirmation a été envoyé "
				+ "à l'adresse :  "+com.getClient().getEmail()+ ".  Voici votre numéro de commande :  "+com.getId()+"  à présenter  " 
						+ " lors de la récupération de votre commande .  Un  de nos agents vous contactera dans les plus bref délais "
						+ ". Nous vous remercion d'avoir passer votre commande avec nous. Merci et à bientot.   "  ;
		
	
		 	vonage.sendSmsCom(com);
			model.addAttribute("message", avecEmail); 
			
		
			return "Confirmation";
	}
	
	

	
	
	

	@RequestMapping("/addCommande")
	private String addCommande(Model model) {
	
		model.addAttribute("commande", new Commande());
	return "addCommande";
	}

	
	
	
	
	
	@RequestMapping("/confirmerCommande")
	private String traiterCommande(Model model, Long id) {
	Commande com =   commandeRepository.getOne(id);
	model.addAttribute("com", com);
	model.addAttribute("idcom", com.getId());
	System.out.println(com.getId());
	Date d = com.getDateCommande();
	model.addAttribute("d", d);
	model.addAttribute("idee", com.getId());
	Date a =  (Date) model.getAttribute("c");
	return "confirmCommande";
	}
	
	

	@RequestMapping("/sai")
	private String traiterCommande() {
	
	return "redirect:/myCommande";
	}
	
	
	@RequestMapping("/confirmeCommande")
	private String confirmeCommande(Model model, Commande com) {
	
	com.setStatus("Confirmée");
	System.out.println("commande " +com.getStatus());
	System.out.println("date :"+com.getDateCommande());
	commandeRepository.save(com);
	
	System.out.println(com.getStatus());
	
	Long idd = (Long) model.getAttribute("idee");
	System.out.println( "l'id "+idd);
	
	return "redirect:/allCommande";
	}
	
	

	@RequestMapping("/rejeterCommande")
	private String rejeterCommande(Long id) {
	Commande com =   commandeRepository.getOne(id);
	com.setStatus("Rejetée");
	commandeRepository.save(com);
	return "redirect:/allCommande";
	}

	@RequestMapping("/livrerCommande")
	private String livrerCommande(Long id) {
	Commande	com = commandeRepository.getOne(id);
	com.setStatus("Livrée");
	commandeRepository.save(com);
		return "redirect:/allCommande";
}
	
	@RequestMapping("/payerCommande")
	private String paye(Long id) {
		Commande com = commandeRepository.getOne(id);
		com.setPaye(true);
		commandeRepository.save(com);
		return "redirect:/myCommande";
		
	}
	@RequestMapping("/reCommander")
	private String reCommander(Model model, Long id) {
	Commande com =   commandeRepository.getOne(id);
	 Commande commande = new Commande();
	 commande.setClient(com.getClient()); 
	 commande.setOrdonnance(com.getOrdonnance());
	 commande.setPrix(null);
	 commande.setDateCommande(new Date());
	 commande.setStatus("Nouvelle");
	 commandeRepository.save(commande);
	
	
	//com.setStatus("Confirmée");
	//affectTo.htmlcommandeRepository.save(com);
		return "redirect:/myCommande";
	}
	
	
	
	@RequestMapping("/myCommande")
	private String myCommande(Authentication auth, Model model) {
		
	String login = auth.getName();
	Client user = clientRepository.getOne(login);
	
	System.out.println(user.getEmail());
	 List<Commande> nouvelle = commandeRepository.findByClientAndStatus(user, "Nouvelle");
	 List<Commande> confirm = commandeRepository.findByClientAndStatus(user, "Confirmée");
	 List<Commande> livree = commandeRepository.findByClientAndStatus(user, "Livrée");
	 List<Commande> rejetee = commandeRepository.findByClientAndStatus(user, "Rejetée");
	 List<Commande> toutes = commandeRepository.findByClient(user);
	 
	 model.addAttribute("nouvelle", nouvelle);
	 model.addAttribute("confirmee", confirm);
	 model.addAttribute("livree", livree);
	 model.addAttribute("toutes", toutes);
	 model.addAttribute("rejetee", rejetee);
	 
	 model.addAttribute("standardDate", new Date());
	 model.addAttribute("localDateTime", LocalDateTime.now());
	 model.addAttribute("localDate", LocalDate.now());
	 model.addAttribute("timestamp", Instant.now());
		return "myCommande";
}
	
	@RequestMapping("/jesuisla")
	private String jesuisla(Authentication auth) {
	Client client = clientRepository.getOne(auth.getName());
	
	List<Commande>	com = commandeRepository.findByClientAndStatus(client, "Confirmée");
	notification.jesuisla(com, client);
		return "jesuisla";
}
	
	

}