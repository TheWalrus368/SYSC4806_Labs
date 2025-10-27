package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final AddressBookRepository repository;

    public WebController(AddressBookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/addressbooks/{id}")
    public String getAddressBook(@PathVariable Long id, Model model) {
        AddressBook ab = repository.findById(id).orElseThrow();
        model.addAttribute("addressBook", ab);
        return "addressbook";
    }
}
