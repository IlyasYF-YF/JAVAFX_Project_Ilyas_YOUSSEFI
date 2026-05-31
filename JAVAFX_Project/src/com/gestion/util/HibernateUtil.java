 package com.gestion.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration()
                        .configure()
                        .buildSessionFactory();
                System.out.println("Connexion MySQL établie !");

                Session session = sessionFactory.openSession();
                Transaction tx = session.beginTransaction();
                System.out.println("Transaction ouverte !");
                tx.rollback();
                session.close();

            } catch (Exception e) {
                System.err.println("Erreur : " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("SessionFactory fermée.");
        }
    }
}