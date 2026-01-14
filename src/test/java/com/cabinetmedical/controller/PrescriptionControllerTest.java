package com.cabinetmedical.controller;

import com.cabinetmedical.model.Consultation;
import com.cabinetmedical.model.Doctor;
import com.cabinetmedical.model.Patient;
import com.cabinetmedical.model.Prescription;
import com.cabinetmedical.service.PrescriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrescriptionController.class)
class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PrescriptionService prescriptionService;

    private Prescription prescription;
    private Patient patient;
    private Doctor doctor;
    private Consultation consultation;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);

        doctor = new Doctor();
        doctor.setId(1L);

        consultation = new Consultation();
        consultation.setId(1L);

        prescription = new Prescription();
        prescription.setId(1L);
        prescription.setDataEmitere(LocalDate.of(2025, 1, 8));
        prescription.setObservatii("A se lua după masă");
        prescription.setStatus("ACTIVA");
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setConsultation(consultation);
    }

    @Test
    void testCreatePrescription_Success() throws Exception {
        // Arrange
        when(prescriptionService.createPrescription(any(Prescription.class))).thenReturn(prescription);

        // Act & Assert
        mockMvc.perform(post("/api/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prescription)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("ACTIVA"))
                .andExpect(jsonPath("$.observatii").value("A se lua după masă"));
    }

    @Test
    void testGetAllPrescriptions_Success() throws Exception {
        // Arrange
        List<Prescription> prescriptions = Arrays.asList(prescription);
        when(prescriptionService.getAllPrescriptions()).thenReturn(prescriptions);

        // Act & Assert
        mockMvc.perform(get("/api/prescriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("ACTIVA"));
    }

    @Test
    void testGetAllPrescriptions_WithStatus() throws Exception {
        // Arrange
        List<Prescription> prescriptions = Arrays.asList(prescription);
        when(prescriptionService.getPrescriptionsByStatus("ACTIVA")).thenReturn(prescriptions);

        // Act & Assert
        mockMvc.perform(get("/api/prescriptions?status=ACTIVA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ACTIVA"));
    }

    @Test
    void testGetPrescriptionById_Success() throws Exception {
        // Arrange
        when(prescriptionService.getPrescriptionById(1L)).thenReturn(prescription);

        // Act & Assert
        mockMvc.perform(get("/api/prescriptions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("ACTIVA"));
    }

    @Test
    void testGetPrescriptionsByPatientId_Success() throws Exception {
        // Arrange
        List<Prescription> prescriptions = Arrays.asList(prescription);
        when(prescriptionService.getPrescriptionsByPatientId(1L)).thenReturn(prescriptions);

        // Act & Assert
        mockMvc.perform(get("/api/prescriptions/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetPrescriptionsByDoctorId_Success() throws Exception {
        // Arrange
        List<Prescription> prescriptions = Arrays.asList(prescription);
        when(prescriptionService.getPrescriptionsByDoctorId(1L)).thenReturn(prescriptions);

        // Act & Assert
        mockMvc.perform(get("/api/prescriptions/doctor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testUpdatePrescription_Success() throws Exception {
        // Arrange
        when(prescriptionService.updatePrescription(eq(1L), any(Prescription.class))).thenReturn(prescription);

        // Act & Assert
        mockMvc.perform(put("/api/prescriptions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prescription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdatePrescriptionStatus_Success() throws Exception {
        // Arrange
        when(prescriptionService.updatePrescriptionStatus(1L, "ELIBERATA")).thenReturn(prescription);

        // Act & Assert
        mockMvc.perform(patch("/api/prescriptions/1/status")
                        .param("status", "ELIBERATA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeletePrescription_Success() throws Exception {
        // Arrange
        doNothing().when(prescriptionService).deletePrescription(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/prescriptions/1"))
                .andExpect(status().isNoContent());
    }
}