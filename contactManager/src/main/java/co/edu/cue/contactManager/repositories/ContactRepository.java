package co.edu.cue.contactManager.repositories;

import co.edu.cue.contactManager.domain.enums.ContactTag;
import co.edu.cue.contactManager.domain.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ContactRepository extends JpaRepository <Contact, Integer>, JpaSpecificationExecutor<Contact> {
    List<Contact> findByNameContainingIgnoreCase(String name);
    List<Contact> findByEmailContainingIgnoreCase(String email);
    List<Contact> findByPhoneContainingIgnoreCase(String phone);
    List<Contact> findByFavoriteTrue();
    //List<Contact> findByTagsName(String name);
    List<Contact> findByTagsContaining(ContactTag tag);
}
