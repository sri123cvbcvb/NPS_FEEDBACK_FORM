package com.bridgelabz.controller;

import com.bridgelabz.dto.FormRequest;
import com.bridgelabz.entity.Batch;
import com.bridgelabz.entity.Learner;
import com.bridgelabz.services.GoogleFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GoogleFormController {

    private final GoogleFormService googleFormService;


    @PostMapping("/generateForm")
    public ResponseEntity<String> GenerateLink(@RequestBody FormRequest formRequest){

        String link = googleFormService.generateGoogleForm(formRequest);
        return ResponseEntity.ok(link);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody FormRequest request){
        String s = googleFormService.sendEmail(request);
        return ResponseEntity.ok(s);
    }
}
