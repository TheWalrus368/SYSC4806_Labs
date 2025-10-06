package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AddressBookApplication {
    public static void main(String[] args) {
        SpringApplication.run(AddressBookApplication.class);
    }

    @Bean
    public CommandLineRunner demo(AddressBookRepository repository) {
        return (args) -> {
            AddressBook ab = new AddressBook();
            ab.addBuddy(new BuddyInfo("Alice", "123 Main St"));
            ab.addBuddy(new BuddyInfo("Bob", "456 Side St"));
            ab.addBuddy(new BuddyInfo("Charlie", "789 Oak Ave"));

            repository.save(ab);
        };
    }
}
