package com.cabinetmedical.controller;

import com.cabinetmedical.model.Medication;
import com.cabinetmedical.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
@Tag(name = "Medications", description = "API pentru gestionarea medicamentelor")
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    @Operation(summary = "Adaugă un medicament nou",
            description = "Înregistrează un medicament nou în sistem")
    public ResponseEntity<Medication> createMedication(@Valid @RequestBody Medication medication) {
        Medication createdMedication = medicationService.createMedication(medication);
        return new ResponseEntity<>(createdMedication, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listează toate medicamentele",
            description = "Returnează lista cu toate medicamentele")
    public ResponseEntity<List<Medication>> getAllMedications() {
        List<Medication> medications = medicationService.getAllMedications();
        return ResponseEntity.ok(medications);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obține un medicament după ID",
            description = "Returnează detaliile unui medicament specific")
    public ResponseEntity<Medication> getMedicationById(@PathVariable Long id) {
        Medication medication = medicationService.getMedicationById(id);
        return ResponseEntity.ok(medication);
    }

    @GetMapping("/search")
    @Operation(summary = "Caută medicamente",
            description = "Caută medicamente după nume")
    public ResponseEntity<List<Medication>> searchMedications(@RequestParam String query) {
        List<Medication> medications = medicationService.searchMedications(query);
        return ResponseEntity.ok(medications);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizează un medicament",
            description = "Modifică informațiile unui medicament existent")
    public ResponseEntity<Medication> updateMedication(@PathVariable Long id,
                                                       @Valid @RequestBody Medication medication) {
        Medication updatedMedication = medicationService.updateMedication(id, medication);
        return ResponseEntity.ok(updatedMedication);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Șterge un medicament", description = "Elimină un medicament din sistem")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}