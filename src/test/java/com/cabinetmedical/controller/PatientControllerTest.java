package com.cabinetmedical.controller;

import com.cabinetmedical.model.Patient;
import com.cabinetmedical.service.PatientService;
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

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setNume("Popescu");
        patient.setPrenume("Ion");
        patient.setCnp("1234567890123");
        patient.setDataNasterii(LocalDate.of(1990, 5, 15));
        patient.setTelefon("0721234567");
        patient.setEmail("ion.popescu@email.com");
        patient.setAdresa("Str. Exemplu nr. 1");
        patient.setAlergii("Polen");
    }

    @Test
    void testCreatePatient_Success() throws Exception {
        // Arrange
        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        // Act & Assert
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Popescu"))
                .andExpect(jsonPath("$.prenume").value("Ion"));
    }

    @Test
    void testGetAllPatients_Success() throws Exception {
        // Arrange
        List<Patient> patients = Arrays.asList(patient);
        when(patientService.getAllPatients()).thenReturn(patients);

        // Act & Assert
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nume").value("Popescu"));
    }

    @Test
    void testGetPatientById_Success() throws Exception {
        // Arrange
        when(patientService.getPatientById(1L)).thenReturn(patient);

        // Act & Assert
        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Popescu"));
    }

    @Test
    void testUpdatePatient_Success() throws Exception {
        // Arrange
        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(patient);

        // Act & Assert
        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Popescu"));
    }

    @Test
    void testDeletePatient_Success() throws Exception {
        // Arrange
        doNothing().when(patientService).deletePatient(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());
    }
}