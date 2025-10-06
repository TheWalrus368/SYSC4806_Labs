package org.example;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addressbooks")
public class AddressBookController {

    private final AddressBookRepository repository;

    public AddressBookController(AddressBookRepository repository) {
        this.repository = repository;
    }

    // Create a new AddressBook
    @PostMapping
    public AddressBook createAddressBook() {
        AddressBook ab = new AddressBook();
        return repository.save(ab);
    }

    // Get all AddressBooks
    @GetMapping
    public Iterable<AddressBook> getAllAddressBooks() {
        return repository.findAll();
    }

    // Add a Buddy to an AddressBook
    @PostMapping("/{id}/buddies")
    public AddressBook addBuddy(@PathVariable Long id, @RequestBody BuddyInfo buddy) {
        AddressBook ab = repository.findById(id).orElseThrow();
        ab.addBuddy(buddy);
        return repository.save(ab);
    }

    // Remove a Buddy by buddyId
    @DeleteMapping("/{id}/buddies/{buddyId}")
    public AddressBook removeBuddy(@PathVariable Long id, @PathVariable Long buddyId) {
        AddressBook ab = repository.findById(id).orElseThrow();
        ab.removeBuddy(buddyId);
        return repository.save(ab);
    }
}
