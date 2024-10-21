package co.edu.cue.contactManager.repositories.specs;

import co.edu.cue.contactManager.domain.model.Contact;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ContactSpecification {

    public static Specification<Contact> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Contact> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Contact> hasDateOfBirth(LocalDate dateOfBirth) {
        return (root, query, criteriaBuilder) ->
                dateOfBirth == null ? null : criteriaBuilder.equal(root.get("dateOfBirth"), dateOfBirth);
    }

}
