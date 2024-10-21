package co.edu.cue.contactManager;

import co.edu.cue.contactManager.domain.model.Contact;
import co.edu.cue.contactManager.repositories.ContactRepository;
import co.edu.cue.contactManager.services.ContactService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ContactManagerApplicationTests {

    @Autowired
    private ContactRepository contactRepository;
    int id = 0;

    @BeforeEach
        // Configurar datos iniciales si es necesario
    void setUp() {
        Contact contact = new Contact();
        contact.setEmail("test@test.com");
        contact.setPhone("3211478863");
        contact.setName("Test");
        contact.setName("Javier");
        contact.setId(1);
        contact.setDateOfBirth(LocalDate.now());

        id = contactRepository.save(contact).getId();
    }

    @AfterEach
        // Limpia todos los datos
    void tearDown() {
        contactRepository.deleteAll();
    }

    @Test
    //Prueba para añadir un contacto
    void addContact() {

        List<Contact> contactList = contactRepository.findAll();

        assertEquals(1, contactList.size());
    }

    @Test
    //Prueba para buscar un contacto por id
    void getByIdContact() {
        Contact contact = contactRepository.findById(id).orElse(null);
        assertThat(contact).isNotNull();
    }

    @Test
    //Prueba para eliminar de un contacto por id
    void deleteByIdContact() {
        contactRepository.deleteById(id);
        Contact contact = contactRepository.findById(id).orElse(null);
        assertThat(contact).isNull();
    }

    @Test
        //Prueba para editar un contacto por id
    void editByIdContact() {
        Optional<Contact> getContact = contactRepository.findById(id);
        if (getContact.isPresent()) {
            getContact.get().setName("Felipe");
            contactRepository.save(getContact.get());
        }
        getContact = contactRepository.findById(id);
        assertThat(getContact).isNotNull();
        assertThat(getContact.get().getName()).isEqualTo("Felipe");
    }
}
