package com.cabinetmedical.controller;

import com.cabinetmedical.model.Doctor;
import com.cabinetmedical.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "API pentru gestionarea medicilor")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @Operation(summary = "Creează un medic nou", description = "Înregistrează un medic nou în sistem")
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody Doctor doctor) {
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listează toți medicii", description = "Returnează lista cu toți medicii înregistrați")
    public ResponseEntity<List<Doctor>> getAllDoctors(
            @RequestParam(required = false) String specializare) {
        List<Doctor> doctors;
        if (specializare != null && !specializare.isEmpty()) {
            doctors = doctorService.getDoctorsBySpecializare(specializare);
        } else {
            doctors = doctorService.getAllDoctors();
        }
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obține un medic după ID", description = "Returnează detaliile unui medic specific")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizează un medic", description = "Modifică informațiile unui medic existent")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id,
                                               @Valid @RequestBody Doctor doctor) {
        Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);
        return ResponseEntity.ok(updatedDoctor);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Șterge un medic", description = "Elimină un medic din sistem")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}