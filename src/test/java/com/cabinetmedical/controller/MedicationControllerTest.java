package com.cabinetmedical.controller;

import com.cabinetmedical.model.Medication;
import com.cabinetmedical.service.MedicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicationController.class)
class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MedicationService medicationService;

    private Medication medication;

    @BeforeEach
    void setUp() {
        medication = new Medication();
        medication.setId(1L);
        medication.setNume("Paracetamol");
        medication.setConcentratie("500mg");
        medication.setPret(new BigDecimal("12.50"));
        medication.setProducator("Farmacia Tei");
        medication.setRestrictii("Nu se administrează copiilor sub 6 ani");
    }

    @Test
    void testCreateMedication_Success() throws Exception {
        // Arrange
        when(medicationService.createMedication(any(Medication.class))).thenReturn(medication);

        // Act & Assert
        mockMvc.perform(post("/api/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medication)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Paracetamol"))
                .andExpect(jsonPath("$.pret").value(12.50));
    }

    @Test
    void testGetAllMedications_Success() throws Exception {
        // Arrange
        List<Medication> medications = Arrays.asList(medication);
        when(medicationService.getAllMedications()).thenReturn(medications);

        // Act & Assert
        mockMvc.perform(get("/api/medications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nume").value("Paracetamol"));
    }

    @Test
    void testGetMedicationById_Success() throws Exception {
        // Arrange
        when(medicationService.getMedicationById(1L)).thenReturn(medication);

        // Act & Assert
        mockMvc.perform(get("/api/medications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Paracetamol"));
    }

    @Test
    void testSearchMedications_Success() throws Exception {
        // Arrange
        List<Medication> medications = Arrays.asList(medication);
        when(medicationService.searchMedications("Paracetamol")).thenReturn(medications);

        // Act & Assert
        mockMvc.perform(get("/api/medications/search")
                        .param("query", "Paracetamol"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nume").value("Paracetamol"));
    }

    @Test
    void testUpdateMedication_Success() throws Exception {
        // Arrange
        when(medicationService.updateMedication(eq(1L), any(Medication.class))).thenReturn(medication);

        // Act & Assert
        mockMvc.perform(put("/api/medications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Paracetamol"));
    }

    @Test
    void testDeleteMedication_Success() throws Exception {
        // Arrange
        doNothing().when(medicationService).deleteMedication(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/medications/1"))
                .andExpect(status().isNoContent());
    }
}