package co.edu.cue.contactManager.domain.model;

import co.edu.cue.contactManager.domain.enums.ContactTag;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone is mandatory")
    //@NumberFormat(message = "Only numbers are allowed")
    private String phone;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent(message = "Date of birth must be in the past or present")
    @NotNull(message = "Date of birth is mandatory")
    private LocalDate dateOfBirth;

    private LocalDateTime timeOfRegistration;

    private boolean favorite;

    @ElementCollection(targetClass = ContactTag.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "contact_tags")
    @Column(name = "tag")
    private Set<ContactTag> tags; // Conjunto de tags

    @PrePersist
    void assignTimeOfRegistration() {
        timeOfRegistration = LocalDateTime.now();
    }

    /*@ManyToMany
    @JoinTable(
            name = "contact_tag",
            joinColumns = @JoinColumn(name = "contact_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();*/

}
