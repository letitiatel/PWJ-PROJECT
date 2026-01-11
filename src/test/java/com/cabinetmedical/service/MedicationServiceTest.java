package com.cabinetmedical.service;

import com.cabinetmedical.model.Medication;
import com.cabinetmedical.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
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
    void testCreateMedication_Success() {
        // Arrange
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);

        // Act
        Medication result = medicationService.createMedication(medication);

        // Assert
        assertNotNull(result);
        assertEquals("Paracetamol", result.getNume());
        assertEquals(new BigDecimal("12.50"), result.getPret());
        verify(medicationRepository, times(1)).save(any(Medication.class));
    }

    @Test
    void testGetAllMedications() {
        // Arrange
        List<Medication> medications = Arrays.asList(medication);
        when(medicationRepository.findAll()).thenReturn(medications);

        // Act
        List<Medication> result = medicationService.getAllMedications();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicationRepository, times(1)).findAll();
    }

    @Test
    void testGetMedicationById_Success() {
        // Arrange
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));

        // Act
        Medication result = medicationService.getMedicationById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Paracetamol", result.getNume());
        verify(medicationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMedicationById_NotFound() {
        // Arrange
        when(medicationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            medicationService.getMedicationById(1L);
        });
    }

    @Test
    void testSearchMedications_WithQuery() {
        // Arrange
        when(medicationRepository.searchMedicamente("Paracetamol"))
                .thenReturn(Arrays.asList(medication));

        // Act
        List<Medication> result = medicationService.searchMedications("Paracetamol");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getNume());
        verify(medicationRepository, times(1)).searchMedicamente("Paracetamol");
    }

    @Test
    void testSearchMedications_EmptyQuery() {
        // Arrange
        when(medicationRepository.findAll()).thenReturn(Arrays.asList(medication));

        // Act
        List<Medication> result = medicationService.searchMedications("");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicationRepository, times(1)).findAll();
        verify(medicationRepository, never()).searchMedicamente(any());
    }

    @Test
    void testSearchMedications_NullQuery() {
        // Arrange
        when(medicationRepository.findAll()).thenReturn(Arrays.asList(medication));

        // Act
        List<Medication> result = medicationService.searchMedications(null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicationRepository, times(1)).findAll();
        verify(medicationRepository, never()).searchMedicamente(any());
    }

    @Test
    void testGetMedicationsByNume() {
        // Arrange
        when(medicationRepository.findByNumeContainingIgnoreCase("Paracetamol"))
                .thenReturn(Arrays.asList(medication));

        // Act
        List<Medication> result = medicationService.getMedicationsByNume("Paracetamol");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicationRepository, times(1)).findByNumeContainingIgnoreCase("Paracetamol");
    }

    @Test
    void testUpdateMedication_Success() {
        // Arrange
        Medication updatedDetails = new Medication();
        updatedDetails.setNume("Paracetamol Updated");
        updatedDetails.setConcentratie("250mg/5ml");
        updatedDetails.setPret(new BigDecimal("15.00"));
        updatedDetails.setProducator("Farmacia Noua");
        updatedDetails.setRestrictii("Restrictii actualizate");

        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);

        // Act
        Medication result = medicationService.updateMedication(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        verify(medicationRepository, times(1)).save(any(Medication.class));
    }

    @Test
    void testDeleteMedication_Success() {
        // Arrange
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        doNothing().when(medicationRepository).delete(medication);

        // Act
        medicationService.deleteMedication(1L);

        // Assert
        verify(medicationRepository, times(1)).delete(medication);
    }

    @Test
    void testDeleteMedication_NotFound() {
        // Arrange
        when(medicationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            medicationService.deleteMedication(1L);
        });
        verify(medicationRepository, never()).delete(any(Medication.class));
    }
}