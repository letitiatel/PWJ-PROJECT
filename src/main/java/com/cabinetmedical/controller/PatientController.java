package com.cabinetmedical.controller;

import com.cabinetmedical.model.Patient;
import com.cabinetmedical.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "API pentru gestionarea pacienților")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @Operation(summary = "Creează un pacient nou", description = "Înregistrează un pacient nou în sistem")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        Patient createdPatient = patientService.createPatient(patient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listează toți pacienții", description = "Returnează lista cu toți pacienții înregistrați")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obține un pacient după ID", description = "Returnează detaliile unui pacient specific")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizează un pacient", description = "Modifică informațiile unui pacient existent")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id,
                                                 @Valid @RequestBody Patient patient) {
        Patient updatedPatient = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Șterge un pacient", description = "Elimină un pacient din sistem")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}