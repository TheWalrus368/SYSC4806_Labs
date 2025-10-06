package org.example;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AddressBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuddyInfo> buddies;

    public AddressBook() {
        buddies = new ArrayList<>();
    }

    // Add a buddy
    public void addBuddy(BuddyInfo buddy) {
        buddies.add(buddy);
    }

    // Remove by object
    public void removeBuddy(BuddyInfo buddy) {
        buddies.remove(buddy);
    }

    // Remove by index
    public void removeBuddy(int index) {
        buddies.remove(index);
    }

    // Remove by buddyId (helper for REST)
    public void removeBuddy(Long buddyId) {
        buddies.removeIf(b -> b.getId().equals(buddyId));
    }

    // Getters
    public Long getId() {
        return id;
    }

    public List<BuddyInfo> getBuddies() {
        return buddies;
    }

    public int bookSize() {
        return buddies.size();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (BuddyInfo buddy : buddies) {
            s.append(buddy.toString()).append("\n");
        }
        return s.toString();
    }
}
