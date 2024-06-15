package org.trappmichael.actionnetworkuploader.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.trappmichael.actionnetworkuploader.models.ActionNetworkEntity;
import org.trappmichael.actionnetworkuploader.services.ActionNetworkAPIService;
import org.trappmichael.actionnetworkuploader.services.PersonService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/person")
@Log4j2
public class PersonController {

    private final PersonService personService;

    private final ActionNetworkAPIService actionNetworkAPIService;

    private String formationSelection;

    private String typeSelection;


    public PersonController(PersonService personService, ActionNetworkAPIService actionNetworkAPIService) {
        this.personService = personService;
        this.actionNetworkAPIService = actionNetworkAPIService;
    }

    @GetMapping("/select")
    public String displaySelectEntityForm() {
        return "person/select";
    }

    @PostMapping("/select")
    public String processSelectEntityForm(@RequestParam String formation,
                                          @RequestParam String type) {
        formationSelection = formation;
        typeSelection = type;

        System.out.println(formationSelection);
        System.out.println(typeSelection);

        return "redirect:add";
    }

    @GetMapping("/add")
    public String displayPersonImportForm() throws JsonProcessingException {

        List<ActionNetworkEntity> entities = actionNetworkAPIService.getEntities(formationSelection,typeSelection);

        return "person/add";
    }

    @PostMapping("/add")
    public String processPersonImportForm(@RequestParam String apiEndpointInput,
                                          @RequestParam MultipartFile csvFile) throws IOException {

        log.info("API Endpoint: " + apiEndpointInput);
        log.info("File name: " + csvFile.getOriginalFilename());
        log.info("File size: " + csvFile.getSize());

        personService.importCSV(apiEndpointInput, csvFile.getInputStream());

        return "person/add";
    }
}
