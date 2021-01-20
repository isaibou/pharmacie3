package com.example.demo.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.RoleRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Roles;
import com.example.demo.service.Notification;

@Controller 
public class ClientController {

	@Autowired
	ClientRepository clientRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	private Notification notification;
	
	
	@RequestMapping(value = "/inscription")
	public String inscripton(Model model) {
		Client client = new Client();
		model.addAttribute("client", client);
		List<Roles> allRoles = roleRepository.findAll();
		model.addAttribute("allRoles", allRoles);
		

		return "Inscription";
		
	}
	
	
	
	  @RequestMapping(value = "/inscrire") public String inscrire(Model model,
	  Client addclient) { 
	String mdp = addclient.getMdp();
	  addclient.setMdp(encoder.encode(mdp));
	  addclient.setActived(true);
	
	  System.out.println(addclient.getEmail());
	  Roles role =  roleRepository.getOne("AA");
	  addclient.getRole().add(role);
	  clientRepository.save(addclient);

	  return "redirect:/LoginVrai?logout";
	  }
	 
	@RequestMapping(value = "/forgotPassword")
	public String forgtoPassword(Model model, String email) {
	//	Client client = clientRepository.getOne(email);
		//notification.sendMdp(client);
		Client client = new Client();
		model.addAttribute("client", client);
		
		return "forgotPassword";
	}
	
	@RequestMapping(value = "/resetPassword")
	public String resetPassword(Model model, String email) {
		Client client = clientRepository.getOne(email);
		String mdp = getRandomStr();
		client.setMdp(encoder.encode(mdp));
		
		System.out.println(client.getEmail());
		notification.sendMdp(client,mdp);
		
		return "LoginVrai";
	}
	
	public static String getRandomStr() 
    {
        //choisissez un caractére au hasard à partir de cette chaîne
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "abcdefghijklmnopqrstuvxyz" 
        		+"0123456789"; 
  
        StringBuilder s = new StringBuilder(8); 
  
        for (int i = 0; i < 8; i++) { 
            int index = (int)(str.length() * Math.random()); 
            s.append(str.charAt(index)); 
        } 
        return s.toString(); 
    }
	
	@RequestMapping(value="/LoginVrai")
	public String Login() {
		return "LoginVrai";
	}
	
	 @RequestMapping(value="/logout", method = RequestMethod.GET)
	 public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     if (auth != null){    
	         new SecurityContextLogoutHandler().logout(request, response, auth);
	     }
	     return "redirect:/LoginVrai?logout";
	 }
}
