package com.cabinetmedical.controller;

import com.cabinetmedical.model.Doctor;
import com.cabinetmedical.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DoctorService doctorService;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setNume("Ionescu");
        doctor.setPrenume("Maria");
        doctor.setSpecializare("Cardiologie");
        doctor.setTelefon("0721111222");
        doctor.setEmail("dr.ionescu@cabinet.ro");
    }

    @Test
    void testCreateDoctor_Success() throws Exception {
        // Arrange
        when(doctorService.createDoctor(any(Doctor.class))).thenReturn(doctor);

        // Act & Assert
        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Ionescu"))
                .andExpect(jsonPath("$.specializare").value("Cardiologie"));
    }

    @Test
    void testGetAllDoctors_Success() throws Exception {
        // Arrange
        List<Doctor> doctors = Arrays.asList(doctor);
        when(doctorService.getAllDoctors()).thenReturn(doctors);

        // Act & Assert
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nume").value("Ionescu"));
    }

    @Test
    void testGetAllDoctors_WithSpecializare() throws Exception {
        // Arrange
        List<Doctor> doctors = Arrays.asList(doctor);
        when(doctorService.getDoctorsBySpecializare("Cardiologie")).thenReturn(doctors);

        // Act & Assert
        mockMvc.perform(get("/api/doctors?specializare=Cardiologie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specializare").value("Cardiologie"));
    }

    @Test
    void testGetDoctorById_Success() throws Exception {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);

        // Act & Assert
        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Ionescu"));
    }

    @Test
    void testUpdateDoctor_Success() throws Exception {
        // Arrange
        when(doctorService.updateDoctor(eq(1L), any(Doctor.class))).thenReturn(doctor);

        // Act & Assert
        mockMvc.perform(put("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Ionescu"));
    }

    @Test
    void testDeleteDoctor_Success() throws Exception {
        // Arrange
        doNothing().when(doctorService).deleteDoctor(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isNoContent());
    }
}