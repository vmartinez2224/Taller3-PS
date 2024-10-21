package co.edu.cue.contactManager.services;

import co.edu.cue.contactManager.domain.model.Contact;
import co.edu.cue.contactManager.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // Crear un nuevo contacto
    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    // Obtener un contacto por ID
    public Optional<Contact> getContactById(Integer id) {
        return contactRepository.findById(id);
    }

    // Obtener todos los contactos
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    // Actualizar un contacto existente
    public Contact updateContact(Integer id, Contact contactDetails) {
        return contactRepository.findById(id).map(contact -> {
            contact.setName(contactDetails.getName());
            contact.setEmail(contactDetails.getEmail());
            contact.setPhone(contactDetails.getPhone());
            return contactRepository.save(contact);
        }).orElseThrow(() -> new RuntimeException("Contact not found with id " + id));
    }

    // Eliminar un contacto por ID
    public void deleteContact(Integer id) {
        contactRepository.deleteById(id);
    }
}
