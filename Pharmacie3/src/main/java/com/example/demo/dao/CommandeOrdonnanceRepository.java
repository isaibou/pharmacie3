package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Client;
import com.example.demo.entities.Commande;
import com.example.demo.entities.CommandeOrdonnance;

public interface CommandeOrdonnanceRepository extends JpaRepository<CommandeOrdonnance, Long> {
	

	public List<CommandeOrdonnance> findByStatus(String status);
	
	public List<CommandeOrdonnance> findByClientAndStatus(Client client, String status);
 
	public		List<CommandeOrdonnance> findByClient(Client client);
	 

}
