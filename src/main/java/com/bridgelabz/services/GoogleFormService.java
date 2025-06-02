package com.bridgelabz.services;

import com.bridgelabz.dto.FormRequest;
import com.bridgelabz.entity.*;
import com.bridgelabz.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleFormService {


    private String baseFormUrl = "https://docs.google.com/forms/d/1YD4uhdnXY7pHAUJjEe62hHhkj5qkAZ-YR1PKbgwUOcU/viewform?edit_requested=true#responses";

    private final BatchRepo batchRepo;

    private final COERepo coeRepo;

    private final LabRepo labRepo;

    private final GoogleFormRepo googleFormRepo;

    private final LearnerRepo learnerRepo;

    private final EmailService emailService;

    public String generateGoogleForm(FormRequest request){

            CenterOfExcellence coe = coeRepo.findByName(request.coeName).orElseGet(() -> {
                CenterOfExcellence c = new CenterOfExcellence();
                c.setName(request.coeName);
                return coeRepo.save(c);
            });

            Lab lab = labRepo.findByName(request.labname).orElseGet(() -> {
                Lab l = new Lab();
                l.setName(request.labname);
                l.setCenter(coe);
                return labRepo.save(l);
            });

            Batch batch = batchRepo.findByNameAndLabIdAndCenterId(request.batchname, lab.getId(), coe.getId())
                    .orElseGet(() -> {
                        Batch b = new Batch();
                        b.setName(request.batchname);
                        b.setLab(lab);
                        b.setCenter(coe);
                        return batchRepo.save(b);
                    });

            Optional<GoogleForm> existing = googleFormRepo.findByBatch(batch);
            if (existing.isPresent()) return existing.get().getFormLink();

            String link = baseFormUrl +
                    "&entry.123=" + URLEncoder.encode(coe.getName(), StandardCharsets.UTF_8) +
                    "&entry.456=" + URLEncoder.encode(lab.getName(), StandardCharsets.UTF_8) +
                    "&entry.789=" + URLEncoder.encode(batch.getName(), StandardCharsets.UTF_8);

            GoogleForm form = new GoogleForm();
            form.setBatch(batch);
            form.setFormLink(link);
            form.setCreatedAt(LocalDateTime.now());
            googleFormRepo.save(form);

            return link;
    }

     public String sendEmail(FormRequest request){
         CenterOfExcellence coe = coeRepo.findByName(request.coeName)
                 .orElseThrow(() -> new RuntimeException("CoE not found"));

         Lab lab = labRepo.findByName(request.getLabname())
                 .orElseThrow(() -> new RuntimeException("Lab not found"));

         Batch batch = batchRepo.findByNameAndLabIdAndCenterId(request.getBatchname(), lab.getId(), coe.getId())
                 .orElseThrow(() -> new RuntimeException("Batch not found"));

         GoogleForm form = googleFormRepo.findByBatch(batch)
                 .orElseThrow(() -> new RuntimeException("Form not generated for batch"));

         List<Learner> learners = learnerRepo.findByBatch(batch);
         for (Learner learner : learners) {
             emailService.sendEmail(
                     learner.getEmail(),
                     "NPS Feedback Request",
                     "Hi " + learner.getName() + ",\nPlease fill out your feedback here: " + form.getFormLink()
             );
         }

         return "Form sent to all learners of batch " + request.batchname;
     }

}
