package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddressBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAddressBook_ShouldReturnNewAddressBook() throws Exception {
        MvcResult result = mockMvc.perform(post("/addressbooks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        AddressBook ab = objectMapper.readValue(response, AddressBook.class);

        assertThat(ab.getId()).isNotNull();
        assertThat(ab.bookSize()).isEqualTo(0);
    }

    @Test
    void getAllAddressBooks_ShouldReturnIterable() throws Exception {
        mockMvc.perform(get("/addressbooks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void addBuddy_ShouldAddBuddyToAddressBook() throws Exception {
        // Create an address book
        MvcResult abResult = mockMvc.perform(post("/addressbooks"))
                .andExpect(status().isOk())
                .andReturn();

        AddressBook ab = objectMapper.readValue(abResult.getResponse().getContentAsString(), AddressBook.class);

        BuddyInfo buddy = new BuddyInfo("Alice", "123 Main St");

        mockMvc.perform(post("/addressbooks/" + ab.getId() + "/buddies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buddy)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buddies[0].name").value("Alice"))
                .andExpect(jsonPath("$.buddies[0].address").value("123 Main St"));
    }

    @Test
    void removeBuddy_ShouldRemoveBuddyFromAddressBook() throws Exception {
        // Create an address book
        MvcResult abResult = mockMvc.perform(post("/addressbooks"))
                .andExpect(status().isOk())
                .andReturn();
        AddressBook ab = objectMapper.readValue(abResult.getResponse().getContentAsString(), AddressBook.class);

        // Add a buddy
        BuddyInfo buddy = new BuddyInfo("Bob", "456 Elm St");
        MvcResult addResult = mockMvc.perform(post("/addressbooks/" + ab.getId() + "/buddies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buddy)))
                .andExpect(status().isOk())
                .andReturn();

        AddressBook updated = objectMapper.readValue(addResult.getResponse().getContentAsString(), AddressBook.class);
        Long buddyId = updated.getBuddies().get(0).getId();

        // Remove the buddy
        mockMvc.perform(delete("/addressbooks/" + ab.getId() + "/buddies/" + buddyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buddies").isEmpty());
    }
}
