package com.cabinetmedical.controller;

import com.cabinetmedical.model.Consultation;
import com.cabinetmedical.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
@Tag(name = "Consultations", description = "API pentru gestionarea consultațiilor")
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping
    @Operation(summary = "Programează o consultație nouă",
            description = "Creează o programare pentru o consultație")
    public ResponseEntity<Consultation> createConsultation(@Valid @RequestBody Consultation consultation) {
        Consultation createdConsultation = consultationService.createConsultation(consultation);
        return new ResponseEntity<>(createdConsultation, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listează toate consultațiile",
            description = "Returnează lista cu toate consultațiile")
    public ResponseEntity<List<Consultation>> getAllConsultations(
            @RequestParam(required = false) String status) {
        List<Consultation> consultations;
        if (status != null && !status.isEmpty()) {
            consultations = consultationService.getConsultationsByStatus(status);
        } else {
            consultations = consultationService.getAllConsultations();
        }
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obține o consultație după ID",
            description = "Returnează detaliile unei consultații specifice")
    public ResponseEntity<Consultation> getConsultationById(@PathVariable Long id) {
        Consultation consultation = consultationService.getConsultationById(id);
        return ResponseEntity.ok(consultation);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Obține consultațiile unui pacient",
            description = "Returnează toate consultațiile unui pacient specific")
    public ResponseEntity<List<Consultation>> getConsultationsByPatientId(@PathVariable Long patientId) {
        List<Consultation> consultations = consultationService.getConsultationsByPatientId(patientId);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Obține consultațiile unui medic",
            description = "Returnează toate consultațiile unui medic specific")
    public ResponseEntity<List<Consultation>> getConsultationsByDoctorId(@PathVariable Long doctorId) {
        List<Consultation> consultations = consultationService.getConsultationsByDoctorId(doctorId);
        return ResponseEntity.ok(consultations);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizează o consultație",
            description = "Modifică detaliile unei consultații existente")
    public ResponseEntity<Consultation> updateConsultation(@PathVariable Long id,
                                                           @Valid @RequestBody Consultation consultation) {
        Consultation updatedConsultation = consultationService.updateConsultation(id, consultation);
        return ResponseEntity.ok(updatedConsultation);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Șterge o consultație", description = "Anulează o consultație din sistem")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
        return ResponseEntity.noContent().build();
    }
}