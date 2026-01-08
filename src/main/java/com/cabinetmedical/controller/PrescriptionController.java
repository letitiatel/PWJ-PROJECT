package com.cabinetmedical.controller;

import com.cabinetmedical.model.Prescription;
import com.cabinetmedical.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@Tag(name = "Prescriptions", description = "API pentru gestionarea rețetelor medicale")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    @Operation(summary = "Emite o rețetă nouă",
            description = "Creează o rețetă medicală pentru un pacient")
    public ResponseEntity<Prescription> createPrescription(@Valid @RequestBody Prescription prescription) {
        Prescription createdPrescription = prescriptionService.createPrescription(prescription);
        return new ResponseEntity<>(createdPrescription, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listează toate rețetele",
            description = "Returnează lista cu toate rețetele emise")
    public ResponseEntity<List<Prescription>> getAllPrescriptions(
            @RequestParam(required = false) String status) {
        List<Prescription> prescriptions;
        if (status != null && !status.isEmpty()) {
            prescriptions = prescriptionService.getPrescriptionsByStatus(status);
        } else {
            prescriptions = prescriptionService.getAllPrescriptions();
        }
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obține o rețetă după ID",
            description = "Returnează detaliile unei rețete specifice")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        Prescription prescription = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Obține rețetele unui pacient",
            description = "Returnează toate rețetele unui pacient specific")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatientId(@PathVariable Long patientId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Obține rețetele emise de un medic",
            description = "Returnează toate rețetele emise de un medic specific")
    public ResponseEntity<List<Prescription>> getPrescriptionsByDoctorId(@PathVariable Long doctorId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorId(doctorId);
        return ResponseEntity.ok(prescriptions);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizează o rețetă",
            description = "Modifică detaliile unei rețete existente")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id,
                                                           @Valid @RequestBody Prescription prescription) {
        Prescription updatedPrescription = prescriptionService.updatePrescription(id, prescription);
        return ResponseEntity.ok(updatedPrescription);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizează statusul unei rețete",
            description = "Schimbă statusul unei rețete (ACTIVA, ELIBERATA, EXPIRATA)")
    public ResponseEntity<Prescription> updatePrescriptionStatus(@PathVariable Long id,
                                                                 @RequestParam String status) {
        Prescription updatedPrescription = prescriptionService.updatePrescriptionStatus(id, status);
        return ResponseEntity.ok(updatedPrescription);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Șterge o rețetă", description = "Elimină o rețetă din sistem")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}