/*package co.edu.cue.contactManager.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Tag {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String name;

        @ManyToMany(mappedBy = "tags")
        private Set<Contact> contacts = new HashSet<>();
}
*/