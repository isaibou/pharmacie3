package com.example.demo.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class CommandeOrdonnance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String path;
	private String fileName;
	private String type;
	private String status;
	private Long prix;
	private Boolean paye;
	
	@DateTimeFormat(pattern= "yyyy-mm-dd")
	private Date dateCommande;
	@ManyToOne
	@JoinColumn(name = "Client_Commande")
	private Client client;
	
	public CommandeOrdonnance(String path, String fileName, String type) {
		super();
		this.path = path;
		this.fileName = fileName;
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDateCommande() {
		return dateCommande;
	}
	public void setDateCommande(Date dateCommande) {
		this.dateCommande = dateCommande;
	}
	public CommandeOrdonnance() {
		super();
	}
	public Long getPrix() {
		return prix;
	}
	public void setPrix(Long prix) {
		this.prix = prix;
	}
	public Boolean getPaye() {
		return paye;
	}
	public void setPaye(Boolean paye) {
		this.paye = paye;
	}
	
	
	
	
}
