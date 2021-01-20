package com.example.demo.dao;

import java.util.List;
import com.example.demo.entities.Client;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Commande;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

	public List<Commande> findByStatus(String status);
	
	public List<Commande> findByClientAndStatus(Client client, String status);
 
	public		List<Commande> findByClient(Client client);
	 
	
	
}
