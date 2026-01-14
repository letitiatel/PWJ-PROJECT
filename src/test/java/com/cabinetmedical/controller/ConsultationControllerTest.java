package com.cabinetmedical.controller;

import com.cabinetmedical.model.Consultation;
import com.cabinetmedical.model.Doctor;
import com.cabinetmedical.model.Patient;
import com.cabinetmedical.service.ConsultationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultationController.class)
class ConsultationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ConsultationService consultationService;

    private Consultation consultation;
    private Patient patient;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);

        doctor = new Doctor();
        doctor.setId(1L);

        consultation = new Consultation();
        consultation.setId(1L);
        consultation.setData(LocalDate.of(2025, 1, 10));
        consultation.setOra(LocalTime.of(10, 0));
        consultation.setMotiv("Control general");
        consultation.setStatus("PROGRAMATA");
        consultation.setPatient(patient);
        consultation.setDoctor(doctor);
    }

    @Test
    void testCreateConsultation_Success() throws Exception {
        // Arrange
        when(consultationService.createConsultation(any(Consultation.class))).thenReturn(consultation);

        // Act & Assert
        mockMvc.perform(post("/api/consultations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consultation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.motiv").value("Control general"))
                .andExpect(jsonPath("$.status").value("PROGRAMATA"));
    }

    @Test
    void testGetAllConsultations_Success() throws Exception {
        // Arrange
        List<Consultation> consultations = Arrays.asList(consultation);
        when(consultationService.getAllConsultations()).thenReturn(consultations);

        // Act & Assert
        mockMvc.perform(get("/api/consultations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].motiv").value("Control general"));
    }

    @Test
    void testGetAllConsultations_WithStatus() throws Exception {
        // Arrange
        List<Consultation> consultations = Arrays.asList(consultation);
        when(consultationService.getConsultationsByStatus("PROGRAMATA")).thenReturn(consultations);

        // Act & Assert
        mockMvc.perform(get("/api/consultations?status=PROGRAMATA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PROGRAMATA"));
    }

    @Test
    void testGetConsultationById_Success() throws Exception {
        // Arrange
        when(consultationService.getConsultationById(1L)).thenReturn(consultation);

        // Act & Assert
        mockMvc.perform(get("/api/consultations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.motiv").value("Control general"));
    }

    @Test
    void testGetConsultationsByPatientId_Success() throws Exception {
        // Arrange
        List<Consultation> consultations = Arrays.asList(consultation);
        when(consultationService.getConsultationsByPatientId(1L)).thenReturn(consultations);

        // Act & Assert
        mockMvc.perform(get("/api/consultations/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetConsultationsByDoctorId_Success() throws Exception {
        // Arrange
        List<Consultation> consultations = Arrays.asList(consultation);
        when(consultationService.getConsultationsByDoctorId(1L)).thenReturn(consultations);

        // Act & Assert
        mockMvc.perform(get("/api/consultations/doctor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testUpdateConsultation_Success() throws Exception {
        // Arrange
        when(consultationService.updateConsultation(eq(1L), any(Consultation.class))).thenReturn(consultation);

        // Act & Assert
        mockMvc.perform(put("/api/consultations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consultation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteConsultation_Success() throws Exception {
        // Arrange
        doNothing().when(consultationService).deleteConsultation(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/consultations/1"))
                .andExpect(status().isNoContent());
    }
}