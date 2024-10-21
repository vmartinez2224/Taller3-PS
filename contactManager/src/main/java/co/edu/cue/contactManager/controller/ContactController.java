package co.edu.cue.contactManager.controller;

import co.edu.cue.contactManager.domain.enums.ContactTag;
import co.edu.cue.contactManager.repositories.specs.ContactSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.edu.cue.contactManager.domain.model.Contact;
import co.edu.cue.contactManager.repositories.ContactRepository;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Contact> contacts = contactRepository.findAll();
        model.addAttribute("contacts", contacts);
        return "index.html";
    }

    @GetMapping("/newContact")
    public String newContact(Model model) {
        model.addAttribute("contact", new Contact());
        return "newContact.html";
    }

    @PostMapping("/newContact")
    public String createContact(
            @Validated Contact contact,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("contact", contact);
            return "newContact.html";
        }

        contactRepository.save(contact);
        redirectAttributes.addFlashAttribute("message", "Contact created successfully");
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String editContact(@PathVariable Integer id, Model model) {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact == null) {
            return "redirect:/";
        }
        model.addAttribute("contact", contact);
        return "newContact.html";
    }

    @PostMapping("/{id}/edit")
    public String updateContact(
            @PathVariable Integer id,
            @Validated Contact contact,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("contact", contact);
            return "newContact.html";
        }

        // Obtener el contacto existente por el ID
        Contact contactFromDB = contactRepository.findById(id).orElse(null);

        if (contactFromDB != null) {
            // Actualizar los datos del contacto existente
            contactFromDB.setName(contact.getName());
            contactFromDB.setPhone(contact.getPhone());
            contactFromDB.setEmail(contact.getEmail());
            contactFromDB.setDateOfBirth(contact.getDateOfBirth());

            // Guardar el contacto actualizado en la base de datos
            contactRepository.save(contactFromDB);
            ra.addFlashAttribute("message", "Contact updated.");
        } else {
            ra.addFlashAttribute("error", "Contact not found.");
        }

        return "redirect:/";
    }

    @PostMapping("/{id}/delete")
    public String deleteContact(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact != null) {
            contactRepository.delete(contact);
            redirectAttributes.addFlashAttribute("message", "Contact deleted successfully");
        }
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchContacts(@RequestParam("query") String query, Model model) {
        List<Contact> contacts = contactRepository.findByNameContainingIgnoreCase(query);
        contacts.addAll(contactRepository.findByEmailContainingIgnoreCase(query));
        contacts.addAll(contactRepository.findByPhoneContainingIgnoreCase(query));

        // Eliminar duplicados en la lista
        contacts = contacts.stream().distinct().toList();

        model.addAttribute("contacts", contacts);
        model.addAttribute("query", query);
        return "index.html";
    }

    @GetMapping("/filter")
    public String filterContacts(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "dateOfBirth", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(value = "sortField", defaultValue = "name") String sortField,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            Model model) {

        Specification<Contact> spec = Specification.where(ContactSpecification.hasName(name))
                .and(ContactSpecification.hasEmail(email))
                .and(ContactSpecification.hasDateOfBirth(dateOfBirth));

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        List<Contact> contacts = contactRepository.findAll(spec, sort);

        model.addAttribute("contacts", contacts);
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("dateOfBirth", dateOfBirth);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "index.html";
    }

    @GetMapping("/favorites")
    public String filterFavorites(Model model) {
        List<Contact> favoriteContacts = contactRepository.findByFavoriteTrue();
        model.addAttribute("contacts", favoriteContacts);
        return "index.html";
    }

    /*@GetMapping("/filterByTag")
    public String filterByTag(@RequestParam("tag") String tagName, Model model) {
        List<Contact> contacts = contactRepository.findByTagsName(tagName);
        model.addAttribute("contacts", contacts);
        return "index.html";
    }*/

    @GetMapping("/filterByTag")
    public String filterByTag(@RequestParam("tag") ContactTag tag, Model model) {
        List<Contact> contacts = contactRepository.findByTagsContaining(tag);
        model.addAttribute("contacts", contacts);
        return "index.html";
    }

}
