package org.example;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

public class BuddyInfoJPATest {

    public void performJPA() {

        BuddyInfo buddy1 = new BuddyInfo();
        buddy1.setName("Alice");
        buddy1.setAddress("123 Main St");

        BuddyInfo buddy2 = new BuddyInfo();
        buddy2.setName("Bob");
        buddy2.setAddress("456 Oak Ave");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-test");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(buddy1);
        em.persist(buddy2);

        tx.commit();

        Query q = em.createQuery("SELECT b FROM BuddyInfo b");

        @SuppressWarnings("unchecked")
        List<BuddyInfo> results = q.getResultList();

        System.out.println("List of buddies\n----------------");

        for (BuddyInfo b : results) {
            System.out.println(b.getName() + " (Address=" + b.getAddress() + ")");
        }

        em.close();
        emf.close();
    }

    public static void main(String[] args) {
        BuddyInfoJPATest test = new BuddyInfoJPATest();
        test.performJPA();
    }
}
