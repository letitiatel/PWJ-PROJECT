package com.cabinetmedical.service;

import com.cabinetmedical.model.Doctor;
import com.cabinetmedical.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
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
    void testCreateDoctor_Success() {
        // Arrange
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // Act
        Doctor result = doctorService.createDoctor(doctor);

        // Assert
        assertNotNull(result);
        assertEquals("Ionescu", result.getNume());
        assertEquals("Maria", result.getPrenume());
        assertEquals("Cardiologie", result.getSpecializare());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void testGetAllDoctors() {
        // Arrange
        List<Doctor> doctors = Arrays.asList(doctor);
        when(doctorRepository.findAll()).thenReturn(doctors);

        // Act
        List<Doctor> result = doctorService.getAllDoctors();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void testGetDoctorById_Success() {
        // Arrange
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        // Act
        Doctor result = doctorService.getDoctorById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ionescu", result.getNume());
        assertEquals("Cardiologie", result.getSpecializare());
        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDoctorById_NotFound() {
        // Arrange
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            doctorService.getDoctorById(1L);
        });
    }

    @Test
    void testGetDoctorsBySpecializare() {
        // Arrange
        List<Doctor> doctors = Arrays.asList(doctor);
        when(doctorRepository.findBySpecializare("Cardiologie")).thenReturn(doctors);

        // Act
        List<Doctor> result = doctorService.getDoctorsBySpecializare("Cardiologie");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cardiologie", result.get(0).getSpecializare());
        verify(doctorRepository, times(1)).findBySpecializare("Cardiologie");
    }

    @Test
    void testUpdateDoctor_Success() {
        // Arrange
        Doctor updatedDetails = new Doctor();
        updatedDetails.setNume("Ionescu Updated");
        updatedDetails.setPrenume("Maria Updated");
        updatedDetails.setSpecializare("Cardiologie Interventionala");
        updatedDetails.setTelefon("0721111222");
        updatedDetails.setEmail("dr.ionescu@cabinet.ro");

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // Act
        Doctor result = doctorService.updateDoctor(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void testDeleteDoctor_Success() {
        // Arrange
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        doNothing().when(doctorRepository).delete(doctor);

        // Act
        doctorService.deleteDoctor(1L);

        // Assert
        verify(doctorRepository, times(1)).delete(doctor);
    }

    @Test
    void testDeleteDoctor_NotFound() {
        // Arrange
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            doctorService.deleteDoctor(1L);
        });
        verify(doctorRepository, never()).delete(any(Doctor.class));
    }
}