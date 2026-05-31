package com.gestion.service;

import com.gestion.model.Plat;
import com.gestion.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class PlatService {

    public void ajouter(Plat p) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(p);
            tx.commit();
            System.out.println(" Plat ajouté : " + p.getNom());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println(" Erreur ajout : " + e.getMessage());
        }
    }

    public void modifier(Plat p) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(p);
            tx.commit();
            System.out.println(" Plat modifié : " + p.getNom());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println(" Erreur modification : " + e.getMessage());
        }
    }

    public void supprimer(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Plat p = session.get(Plat.class, id);
            if (p != null) {
                session.delete(p);
                tx.commit();
                System.out.println(" Plat supprimé ID : " + id);
            } else {
                System.err.println(" Plat introuvable ID : " + id);
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println(" Erreur suppression : " + e.getMessage());
        }
    }

    public List<Plat> listerTous() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Plat", Plat.class).list();
        } catch (Exception e) {
            System.err.println(" Erreur listage : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Plat> rechercher(String motCle) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Plat p WHERE lower(p.nom) LIKE :mc OR lower(p.categorie) LIKE :mc";
            Query<Plat> query = session.createQuery(hql, Plat.class);
            query.setParameter("mc", "%" + motCle.toLowerCase() + "%");
            return query.list();
        } catch (Exception e) {
            System.err.println(" Erreur recherche : " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
