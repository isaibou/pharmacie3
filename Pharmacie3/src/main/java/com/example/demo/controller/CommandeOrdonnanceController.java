package com.example.demo.controller;

import org.springframework.stereotype.Controller;

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


@Controller
public class CommandeOrdonnanceController {

	
		
		@Autowired
		private CommandeRepository commandeRepository;
		@Autowired
		private ClientRepository clientRepository;
		@Autowired
		CommandeOrdonnanceRepository commandeOrdonnanceRepository;
		
		@Autowired
		private AmazonS3 amazonS3;
		
		@Value("${aws.s3.bucket}")
		private String s3bucket;
		
	
		@Autowired
		private Notification notification;
		
		@Autowired
		private ServiceUploadAWS uploadtoS3;
		
		@RequestMapping("/myOrdonnance")
		private String myOrdonnance(Authentication auth, Model model) {
			
		String login = auth.getName();
		Client user = clientRepository.getOne(login);
		
		System.out.println(user.getEmail());
		 List<CommandeOrdonnance> nouvelle = commandeOrdonnanceRepository.findByClientAndStatus(user, "Nouvelle");
		 List<CommandeOrdonnance> confirm = commandeOrdonnanceRepository.findByClientAndStatus(user, "Confirmée");
		 List<CommandeOrdonnance> livree = commandeOrdonnanceRepository.findByClientAndStatus(user, "Livrée");
		 List<CommandeOrdonnance> rejetee = commandeOrdonnanceRepository.findByClientAndStatus(user, "Rejetée");
		 List<CommandeOrdonnance> toutes = commandeOrdonnanceRepository.findByClient(user);
		 
		 model.addAttribute("nouvelle", nouvelle);
		 model.addAttribute("confirmee", confirm);
		 model.addAttribute("livree", livree);
		 model.addAttribute("toutes", toutes);
		 model.addAttribute("rejetee", rejetee);
		 
		 model.addAttribute("standardDate", new Date());
		 model.addAttribute("localDateTime", LocalDateTime.now());
		 model.addAttribute("localDate", LocalDate.now());
		 model.addAttribute("timestamp", Instant.now());
			return "myOrdonnance";
	}
		
		@RequestMapping("/allCommandeOrdonnance")
		private String allCommande(Model model) {
			 List<CommandeOrdonnance> nouvelle = commandeOrdonnanceRepository.findByStatus("Nouvelle");
			 List<CommandeOrdonnance> confirm = commandeOrdonnanceRepository.findByStatus("Confirmée");
			 List<CommandeOrdonnance> livree = commandeOrdonnanceRepository.findByStatus("Livrée");
			 List<CommandeOrdonnance> rejetee = commandeOrdonnanceRepository.findByStatus("Rejetée");
			 List<CommandeOrdonnance> toutes = commandeOrdonnanceRepository.findAll();
			 
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
			 
			return "Ordonnance";
		}
		
		
				
		

		@RequestMapping("/saveOrdonnance")
		private String saveOrdonnance(CommandeOrdonnance com,Model model, Authentication auth, @RequestParam("file") MultipartFile file) throws IOException {
			String gard = auth.getName();
			Client client = clientRepository.getOne(gard);
			com.setClient(client);
			com.setStatus("Nouvelle");
			com.setDateCommande(new Date());;
			commandeOrdonnanceRepository.save(com);
			uploadtoS3.uploadFileToS3Bucet(com ,file);
			String message = "  Nous avons bien recu votre commande : "+"  Voici votre numéro de commande :  "+com.getId()+"  à présenter  " 
							+ " lors de la récupération de votre commande .  Un  de nos agents vous contactera dans les plus bref délais sur ce numéro"
							+ ". Nous vous remercion d'avoir passer votre commande avec nous. Merci et à bientot.   "  ;
				model.addAttribute("message", message); 
				return "Confirmation";
		}
		
		
		
		

		@RequestMapping("/addOrdonnance")
		private String addOrdonnance(Model model) {
		model.addAttribute("commande", new CommandeOrdonnance());
		return "addOrdonnance";
		}
		
		
		
		
		@RequestMapping("/confirmerCommandeOrdonnance")
		private String traiterCommande(Model model, Long id) {
		CommandeOrdonnance com =   commandeOrdonnanceRepository.getOne(id);
		model.addAttribute("com", com);
		model.addAttribute("idcom", com.getId());
		System.out.println(com.getId());
		Date d = com.getDateCommande();
		model.addAttribute("d", d);

		
		//com.setStatus("Confirmée");
		//affectTo.htmlcommandeRepository.save(com);
		return "confirmOrdonnance";
		}
		
		

		
	
	  @RequestMapping("/confirmeCommandeOrdonnance") private String
	  confirmeCommande(Model model, CommandeOrdonnance com) {
	  
	  com.setStatus("Confirmée"); //com.setOrdonnance(ordonnance);
	  System.out.println("commande " +com.getStatus());
	  
	  commandeOrdonnanceRepository.save(com); System.out.println("commande " +
	  com.getClient().getEmail());
	  
	  try { //=ù^)ànotification.confirmeCommande(com);
	  
	  } catch (MailException e) { System.out.println("Problème d'envoie de mail ");
	  }
	  
	  return "redirect:/allCommandeOrdonnance"; }
	 
		
		
		@RequestMapping("/confirmerOrdonnance")
		private String confirmerCommande(Long id) {
		CommandeOrdonnance com =   commandeOrdonnanceRepository.getOne(id);
		com.setStatus("Confirmée");
		commandeOrdonnanceRepository.save(com);
		return "redirect:/allCommandeOrdonnance";
		}

		@RequestMapping("/rejeterCommandeOrdonnance")
		private String rejeterCommande(Long id) {
		CommandeOrdonnance com =   commandeOrdonnanceRepository.getOne(id);
		com.setStatus("Rejetée");
		commandeOrdonnanceRepository.save(com);
		return "redirect:/allCommandeOrdonnance";
		}

		@RequestMapping("/livrerCommandeOrdonnance")
		private String livrerCommande(Long id) {
		CommandeOrdonnance	com = commandeOrdonnanceRepository.getOne(id);
		com.setStatus("Livrée");
		commandeOrdonnanceRepository.save(com);
			return "redirect:/allCommandeOrdonnance";
	    }
		
	
		
		
		@RequestMapping("/myCommandeOrdonnace")
		private String myCommande(Authentication auth, Model model) {
			
		String login = auth.getName();
		Client user = clientRepository.getOne(login);
		
		System.out.println(user.getEmail());
		 List<CommandeOrdonnance> nouvelle =commandeOrdonnanceRepository.findByClientAndStatus(user, "Nouvelle");
		 List<CommandeOrdonnance> confirm = commandeOrdonnanceRepository.findByClientAndStatus(user, "Confirmée");
		 List<CommandeOrdonnance> livree =  commandeOrdonnanceRepository.findByClientAndStatus(user, "Livrée");
		 List<CommandeOrdonnance> rejetee = commandeOrdonnanceRepository.findByClientAndStatus(user, "Rejetée");
		 List<CommandeOrdonnance> toutes =  commandeOrdonnanceRepository.findByClient(user);
		 
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
		
	/*
	 * @RequestMapping("/jesuisla") private String jesuisla(Authentication auth) {
	 * Client client = clientRepository.getOne(auth.getName());
	 * 
	 * List<Commande> com = commandeRepository.findByClientAndStatus(client,
	 * "Confirmée"); notification.jesuisla(com, client); return "jesuisla"; }
	 */
		

		
		
		 
	    @RequestMapping("/viewFile")
	    @ResponseBody
		public void viewFile(Long id,HttpServletResponse response) throws IOException {
			
		  	CommandeOrdonnance  com = commandeOrdonnanceRepository.getOne(id);
		  	System.out.println(id);
		  	String folderPath = com.getPath();
		  	String fileName = com.getFileName();
		  	
		  	 if (fileName.indexOf(".doc")>-1)
		  		  response.setContentType("application/msword"); if
		  		  (fileName.indexOf(".docx")>-1) response.setContentType("application/msword");
		  		  if (fileName.indexOf(".xls")>-1)
		  		  response.setContentType("application/vnd.ms-excel"); if
		  		  (fileName.indexOf(".csv")>-1)
		  		  response.setContentType("application/vnd.ms-excel"); if
		  		  (fileName.indexOf(".ppt")>-1) response.setContentType("application/ppt"); if
		  		  (fileName.indexOf(".pdf")>-1) response.setContentType("application/pdf"); if
		  		  (fileName.indexOf(".zip")>-1) response.setContentType("application/zip");
		  		  
		  		  response.setHeader("Content-Disposition","attachment; filename=" +fileName);
		  		  response.setHeader("Content-Transfer-Encoding", "binary");
	  		//response.setHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
	  		System.out.println(fileName);
	  		System.out.println(folderPath);
		 
	  		S3Object s3object = amazonS3.getObject(s3bucket, folderPath);
	  		S3ObjectInputStream inputStream = s3object.getObjectContent();

			IOUtils.copy(inputStream, response.getOutputStream());

		
	}
	
	    @RequestMapping("/payerCommandeOrdonnance")
		private String paye(Long id) {
			CommandeOrdonnance com = commandeOrdonnanceRepository.getOne(id);
			com.setPaye(true);
			commandeOrdonnanceRepository.save(com);
			return "redirect:/myOrdonnance";
		
			
		}
	
	
	
	
	
	
}
