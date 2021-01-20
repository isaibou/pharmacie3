package com.example.demo.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Client implements Serializable {

	@Id
	
	private String email;
	private String nom;
	private String prenom;
	private String phoneClient;
	private String mdp;
	private boolean actived;
	
	@OneToMany(mappedBy = "client")
	private Collection<Commande>  commande; 
	
	@ManyToMany
	private Collection<Roles>  role;
	
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getPhoneClient() {
		return phoneClient;
	}
	public void setPhoneClient(String phoneClient) {
		this.phoneClient = phoneClient;
	}
	public Collection<Commande> getCommande() {
		return commande;
	}
	public void setCommande(Collection<Commande> commande) {
		this.commande = commande;
	}
	
	public String getMdp() {
		return mdp;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public boolean isActived() {
		return actived;
	}
	public void setActived(boolean actived) {
		this.actived = actived;
	}
	public Collection<Roles> getRole() {
		return role;
	}
	public void setRole(Collection<Roles> role) {
		this.role = role;
	}
	

	
	
	
}
