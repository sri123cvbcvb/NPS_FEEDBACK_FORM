package com.bridgelabz.services;

import com.bridgelabz.Exception.GoogleFormNotFoundException;
import com.bridgelabz.Exception.LearnerNotFoundException;
import com.bridgelabz.Exception.TechStackNotFoundException;
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
    
    private final UrlShortenerService urlShortenerService;

    private final TechStackRepo techStackRepo;

   /* public String generateGoogleForm(FormRequest request){

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
                //        b.setCenter(coe);
                        return batchRepo.save(b);
                    });

            Optional<GoogleForm> existing = googleFormRepo.findByBatch(batch);
            if (existing.isPresent()) return existing.get().getFormLink();

            String link = baseFormUrl +
                    "&entry.1=" + URLEncoder.encode(coe.getName(), StandardCharsets.UTF_8) +
                    "&entry.2=" + URLEncoder.encode(lab.getName(), StandardCharsets.UTF_8) +
                    "&entry.3=" + URLEncoder.encode(batch.getName(), StandardCharsets.UTF_8);

        String shortLink = urlShortenerService.shortenUrl(link);

        GoogleForm form = new GoogleForm();
        //    form.setBatch(batch);
            form.setFormLink(shortLink);
            form.setCreatedAt(LocalDateTime.now());
            googleFormRepo.save(form);

            return shortLink;
    }
*/
    /* public String sendEmail(FormRequest request){
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
                     "Hi " + learner.getName() + ",\nPlease fill out your feedback here: "
                             + form.getFormLink()
             );
         }

         return "Form sent to all learners of batch " + request.batchname;
     }*/


     //After requirement changes, I am adding extra fields called techStack:


    public String assignFormToTechStack(FormRequest request) {

        CenterOfExcellence coe = coeRepo.findByName(request.coeName).orElseGet(() -> {
            CenterOfExcellence c = new CenterOfExcellence();
            c.setName(request.coeName);
            return coeRepo.save(c);
        });


        Lab lab = labRepo.findByNameAndCenterId(request.labname, coe.getId()).orElseGet(() -> {
            Lab l = new Lab();
            l.setName(request.labname);
            l.setCenter(coe);
            return labRepo.save(l);
        });


        Batch batch = batchRepo.findByNameAndLabId(request.batchname, lab.getId()).orElseGet(() -> {
            Batch b = new Batch();
            b.setName(request.batchname);
            b.setLab(lab);
            b.setCenter(coe);
            return batchRepo.save(b);
        });

        TechStack techStack = techStackRepo.findByNameAndBatchId(request.techStackName, batch.getId()).orElseGet(() ->{
            TechStack techStack1 = new TechStack();
            techStack1.setName(request.techStackName);
            techStack1.setBatch(batch);
            return techStackRepo.save(techStack1);

        });



        Optional<GoogleForm> existing = googleFormRepo.findByTechStack(techStack);
        if (existing.isPresent()) {
            return existing.get().getFormLink(); // Reuse existing form
        }


        String link = baseFormUrl +
                "&entry.1=" + URLEncoder.encode(coe.getName(), StandardCharsets.UTF_8) +
                "&entry.2=" + URLEncoder.encode(lab.getName(), StandardCharsets.UTF_8) +
                "&entry.3=" + URLEncoder.encode(batch.getName(), StandardCharsets.UTF_8) +
                "&entry.4=" + URLEncoder.encode(techStack.getName(), StandardCharsets.UTF_8);

        String shortLink = urlShortenerService.shortenUrl(link);


        GoogleForm form = new GoogleForm();
        form.setFormLink(shortLink);
        form.setCreatedAt(LocalDateTime.now());

        form.setTechStack(techStack);

        googleFormRepo.save(form);
        System.out.println(techStack.getLearnerList());

        return "Form assgined to all learners of batch " + request.batchname;

    }

    public String SendEmail(String techStackName) {

        TechStack techStack = techStackRepo.findByName(techStackName);

       // GoogleForm byTechStack = googleFormRepo.findByTechStack(techStack).get();
        if (techStack == null) {
            throw new TechStackNotFoundException("Tech stack not found: " + techStackName);
        }

        List<Learner> learners = techStack.getLearnerList();

        if (learners == null || learners.isEmpty()) {
            throw new LearnerNotFoundException("No learners found for tech stack: " + techStackName);
        }

        Optional<GoogleForm> optionalForm = googleFormRepo.findByTechStack(techStack);
        if (optionalForm.isEmpty()) {
            throw new GoogleFormNotFoundException("Google form not found for tech stack: " + techStackName);
        }

        GoogleForm byTechStack = optionalForm.get();
       // List<Learner> learners = techStack.getLearnerList();
        for (Learner learner : learners) {
            emailService.sendEmail(
                    learner.getEmail(),
                    "NPS Feedback Request",
                    "Hi " + learner.getName() + ",\nPlease fill out your feedback here: "
                            + byTechStack.getFormLink()
            );
        }

        return "Email Sent to all the person who are all in"+ techStackName;
    }





}
