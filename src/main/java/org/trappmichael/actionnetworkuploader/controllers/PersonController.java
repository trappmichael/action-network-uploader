package org.trappmichael.actionnetworkuploader.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.trappmichael.actionnetworkuploader.services.PersonService;

import java.io.IOException;

@Controller
@RequestMapping("/person")
@Log4j2
public class PersonController {

    private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/add")
    public String displayPersonImportForm() {
        return "person/add";
    }

    @PostMapping("/add")
    public String processPersonImportForm(@RequestParam String apiEndpointInput, @RequestParam MultipartFile csvFile, Errors errors, Model model) {
        log.info("API Endpoint: " + apiEndpointInput);
        log.info("File name: " + csvFile.getOriginalFilename());
        log.info("File size: " + csvFile.getSize());

        if (!errors.hasErrors()) {
            try {
                personService.importCSV(csvFile.getInputStream());
                return "redirect:person/add";
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMsg", "system is currently unable to accept file");
            }
        }
        return "person/add";
    }
}
