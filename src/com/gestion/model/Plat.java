package com.gestion.model;

import javax.persistence.*;

@Entity
@Table(name = "plat")
public class Plat { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "categorie", nullable = false, length = 50)
    private String categorie;

    @Column(name = "prix", nullable = false)
    private double prix;

    @Column(name = "quantite", nullable = false)
    private int quantite;

    @Column(name = "disponible", nullable = false)
    private boolean disponible;

    public Plat() {}

    public Plat(String code, String nom, String categorie,
                double prix, int quantite, boolean disponible) {
        this.code      = code;
        this.nom       = nom;
        this.categorie = categorie;
        this.prix      = prix;
        this.quantite  = quantite;
        this.disponible = disponible;
    }

    public Integer getId()               { return id; }
    public void    setId(Integer id)     { this.id = id; }

    public String  getCode()             { return code; }
    public void    setCode(String code)  { this.code = code; }

    public String  getNom()              { return nom; }
    public void    setNom(String nom)    { this.nom = nom; }

    public String  getCategorie()                   { return categorie; }
    public void    setCategorie(String categorie)   { this.categorie = categorie; }

    public double  getPrix()             { return prix; }
    public void    setPrix(double prix)  { this.prix = prix; }

    public int     getQuantite()                { return quantite; }
    public void    setQuantite(int quantite)    { this.quantite = quantite; }

    public boolean isDisponible()                     { return disponible; }
    public void    setDisponible(boolean disponible)  { this.disponible = disponible; }

    @Override
    public String toString() {
        return "Plat{id=" + id + ", code=" + code + 
               ", nom=" + nom + ", prix=" + prix + "}";
    }
}