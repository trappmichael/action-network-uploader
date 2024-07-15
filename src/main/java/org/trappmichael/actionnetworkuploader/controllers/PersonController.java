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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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

    // Display the select.html view containing a form for selecting a formation and
    // Action Network entity type (i.e., form, event, petition).
    @GetMapping("/select")
    public String displaySelectEntityForm() {
        return "person/select";
    }

    // Processes form in the select.html view
    @PostMapping("/select")
    public String processSelectEntityForm(@RequestParam String formation,
                                          @RequestParam String type) {

        // Initializes formationSelection variable with the name of the formation selected in the form
        formationSelection = formation;

        // Initializes typeSelection variable with the name of the Action Network entity type
        // (i.e., form, event, or petition) selected in the form
        typeSelection = type;

        return "redirect:add";
    }

    // Display the add.html view containing a form for selecting a specific Action Network entity
    // and uploading a .csv file of person records.
    @GetMapping("/add")
    public String displayPersonImportForm(Model model) throws JsonProcessingException {

        // Searches the Action Network API for Action Network entities within the selected
        // formation and of the selected type and loads them into an array list.
        List<ActionNetworkEntity> entities = actionNetworkAPIService.getEntities(formationSelection,typeSelection);

        // Adds the entities array list and typeSelection as attributes to the model object used
        // to render the import form.
        model.addAttribute("actionNetworkEntities", entities);
        model.addAttribute("entityType", typeSelection.substring(0,typeSelection.length()-1));
        model.addAttribute("typeSelection", typeSelection);

        return "person/add";
    }

    // Processes form in the add.html view and imports records in .csv file to Action Network
    @PostMapping("/add")
    public String processPersonImportForm(@RequestParam("actionNetworkEntitySelector") String endpoint,
                                          @RequestParam MultipartFile csvFile) throws IOException {

        // Imports the .csv file of Action Network person records to the API endpoint of the Action Network
        // entity selected in the form.
        personService.importCSV(typeSelection, endpoint, csvFile.getInputStream());

        return "redirect:success";
    }

    @GetMapping("/success")
    public String displaySuccessForm() {
        return "person/success";
    }

    @PostMapping("/success")
    public String processSuccessForm() {
        return "redirect:select";
    }
}
