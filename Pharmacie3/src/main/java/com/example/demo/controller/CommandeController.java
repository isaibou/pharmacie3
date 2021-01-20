package com.example.demo.controller;

import java.awt.color.CMMException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.expression.Dates;
import org.w3c.dom.ls.LSInput;

import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.CommandeRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Commande;
import com.example.demo.service.Notification;

@Controller
public class CommandeController {
	
	@Autowired
	private CommandeRepository commandeRepository;
	@Autowired
	private ClientRepository clientRepository;
	
	@RequestMapping("/commander")
	private String Commander(Model model) {
		model.addAttribute("commande",new Commande());
		return "commande";
	}
	
	@Autowired
	private Notification notification;
	
	
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
	private String saveCommande(Commande com,Model model, Authentication auth) {
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
		
		
		
		
		
		  try { 
			  notification.sendConfirmation(com);   
		  }
		  catch (MailException e) 
		  {
		  System.out.println("pb de message"); 
		  }
		 
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

	
	//com.setStatus("Confirmée");
	//affectTo.htmlcommandeRepository.save(com);
	return "confirmCommande";
	}
	
	

	@RequestMapping("/sai")
	private String traiterCommande() {
	
	return "redirect:/myCommande";
	}
	
	
	@RequestMapping("/confirmeCommande")
	private String confirmeCommande(Model model, Commande com) {
	
	com.setStatus("Confirmée");
	//com.setOrdonnance(ordonnance);
	System.out.println("commande " +com.getStatus());

	commandeRepository.save(com);
	System.out.println("commande " + com.getClient().getEmail());
	
	try {	notification.confirmeCommande(com);
		
	} catch (MailException e) {
		System.out.println("Problème d'envoie de mail ");
	}

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
	
}
