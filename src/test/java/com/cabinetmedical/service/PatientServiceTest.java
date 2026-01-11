package com.cabinetmedical.service;

import com.cabinetmedical.model.Patient;
import com.cabinetmedical.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
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
    void testCreatePatient_Success() {
        // Arrange
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Act
        Patient result = patientService.createPatient(patient);

        // Assert
        assertNotNull(result);
        assertEquals("Popescu", result.getNume());
        assertEquals("Ion", result.getPrenume());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testGetAllPatients() {
        // Arrange
        List<Patient> patients = Arrays.asList(patient);
        when(patientRepository.findAll()).thenReturn(patients);

        // Act
        List<Patient> result = patientService.getAllPatients();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testGetPatientById_Success() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // Act
        Patient result = patientService.getPatientById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Popescu", result.getNume());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPatientById_NotFound() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            patientService.getPatientById(1L);
        });
    }

    @Test
    void testUpdatePatient_Success() {
        // Arrange
        Patient updatedDetails = new Patient();
        updatedDetails.setNume("Popescu Updated");
        updatedDetails.setPrenume("Ion Updated");
        updatedDetails.setCnp("1234567890123");
        updatedDetails.setDataNasterii(LocalDate.of(1990, 5, 15));
        updatedDetails.setTelefon("0721234567");
        updatedDetails.setEmail("ion.popescu@email.com");
        updatedDetails.setAdresa("Str. Noua nr. 2");
        updatedDetails.setAlergii("Penicilină");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Act
        Patient result = patientService.updatePatient(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testDeletePatient_Success() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        doNothing().when(patientRepository).delete(patient);

        // Act
        patientService.deletePatient(1L);

        // Assert
        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    void testDeletePatient_NotFound() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            patientService.deletePatient(1L);
        });
        verify(patientRepository, never()).delete(any(Patient.class));
    }
}
