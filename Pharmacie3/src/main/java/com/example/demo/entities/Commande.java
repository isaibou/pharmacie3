package com.example.demo.entities;

import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;



@Entity 
public class Commande {

	@javax.persistence.Id
	@GeneratedValue
	private Long Id;
	private String status;
	private String ordonnance;
	private String docName;
	private String docType;

	@DateTimeFormat(pattern= "yyyy-mm-dd")
	private Date dateCommande;
	@ManyToOne
	@JoinColumn(name = "Client_Commande")
	private Client client;
	
	private Long prix;
	
	private Boolean paye;
	
	public Commande() {
		super();
	}
	
	
	
	



	public Commande(String status, String ordonnance, String docName, String docType, byte[] data, Date dateCommande,
			Client client, Long prix) {
		super();
		this.status = status;
		this.ordonnance = ordonnance;
		this.docName = docName;
		this.docType = docType;
		this.dateCommande = dateCommande;
		this.client = client;
		this.prix = prix;
	}







	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrdonnance() {
		return ordonnance;
	}
	public void setOrdonnance(String ordonnance) {
		this.ordonnance = ordonnance;
	}
	public Date getDateCommande() {
		return dateCommande;
	}
	public void setDateCommande(Date dateCommande) {
		this.dateCommande = dateCommande;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Long getPrix() {
		return prix;
	}
	public void setPrix(Long prix) {
		this.prix = prix;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}







	public Boolean getPaye() {
		return paye;
	}







	public void setPaye(Boolean paye) {
		this.paye = paye;
	}



	
	
	
	
	
	
	
	
	
}
