package org.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AddressBookRepository extends CrudRepository<AddressBook, Long> {

    @Query("SELECT a FROM AddressBook a LEFT JOIN FETCH a.buddies")
    List<AddressBook> findAddBookWithBuddies();
}
