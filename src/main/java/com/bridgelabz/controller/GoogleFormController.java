package com.bridgelabz.controller;

import com.bridgelabz.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse> AssignLink(@RequestBody FormRequest formRequest){

        String link = googleFormService.assignFormToTechStack(formRequest);
        return ResponseEntity.ok(new ApiResponse(true, link));
    }

    @PostMapping("/send/{techStack}")
    public ResponseEntity<ApiResponse> sendEmail(@PathVariable String techStack){
        String result = googleFormService.SendEmail(techStack);
        return ResponseEntity.ok(new ApiResponse(true, result));
    }
}
