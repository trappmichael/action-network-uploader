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

@Controller
@RequestMapping("/person")
@Log4j2
public class PersonController {
    @GetMapping("/add")
    public String displayPersonImportForm() {
        return "person/add";
    }

    @PostMapping("/add")
    public String processPersonImportForm(@RequestParam String apiEndpointInput, @RequestParam MultipartFile csvFile) {
        log.info("API Endpoint: " + apiEndpointInput);
        log.info("File name: " + csvFile.getOriginalFilename());
        log.info("File size: " + csvFile.getSize());
        return "person/add";
    }
}
