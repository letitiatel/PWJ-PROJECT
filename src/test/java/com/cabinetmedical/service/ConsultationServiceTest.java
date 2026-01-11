package com.cabinetmedical.service;

import com.cabinetmedical.model.Consultation;
import com.cabinetmedical.model.Doctor;
import com.cabinetmedical.model.Patient;
import com.cabinetmedical.repository.ConsultationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {

    @Mock
    private ConsultationRepository consultationRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private ConsultationService consultationService;

    private Consultation consultation;
    private Patient patient;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setNume("Popescu");

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setNume("Ionescu");

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
    void testCreateConsultation_Success() {
        // Arrange
        when(patientService.getPatientById(1L)).thenReturn(patient);
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(consultationRepository.findByDoctorIdAndDataAndOra(1L, consultation.getData(), consultation.getOra()))
                .thenReturn(Arrays.asList());
        when(consultationRepository.save(any(Consultation.class))).thenReturn(consultation);

        // Act
        Consultation result = consultationService.createConsultation(consultation);

        // Assert
        assertNotNull(result);
        assertEquals("Control general", result.getMotiv());
        assertEquals("PROGRAMATA", result.getStatus());
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    void testCreateConsultation_DoctorAlreadyHasConsultation() {
        // Arrange
        when(patientService.getPatientById(1L)).thenReturn(patient);
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(consultationRepository.findByDoctorIdAndDataAndOra(1L, consultation.getData(), consultation.getOra()))
                .thenReturn(Arrays.asList(consultation));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            consultationService.createConsultation(consultation);
        });
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    void testGetAllConsultations() {
        // Arrange
        List<Consultation> consultations = Arrays.asList(consultation);
        when(consultationRepository.findAll()).thenReturn(consultations);

        // Act
        List<Consultation> result = consultationService.getAllConsultations();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(consultationRepository, times(1)).findAll();
    }

    @Test
    void testGetConsultationById_Success() {
        // Arrange
        when(consultationRepository.findById(1L)).thenReturn(Optional.of(consultation));

        // Act
        Consultation result = consultationService.getConsultationById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(consultationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetConsultationById_NotFound() {
        // Arrange
        when(consultationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            consultationService.getConsultationById(1L);
        });
    }

    @Test
    void testGetConsultationsByPatientId() {
        // Arrange
        when(patientService.getPatientById(1L)).thenReturn(patient);
        when(consultationRepository.findByPatientId(1L)).thenReturn(Arrays.asList(consultation));

        // Act
        List<Consultation> result = consultationService.getConsultationsByPatientId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(consultationRepository, times(1)).findByPatientId(1L);
    }

    @Test
    void testGetConsultationsByDoctorId() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(consultationRepository.findByDoctorId(1L)).thenReturn(Arrays.asList(consultation));

        // Act
        List<Consultation> result = consultationService.getConsultationsByDoctorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(consultationRepository, times(1)).findByDoctorId(1L);
    }

    @Test
    void testGetConsultationsByStatus() {
        // Arrange
        when(consultationRepository.findByStatus("PROGRAMATA")).thenReturn(Arrays.asList(consultation));

        // Act
        List<Consultation> result = consultationService.getConsultationsByStatus("PROGRAMATA");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PROGRAMATA", result.get(0).getStatus());
        verify(consultationRepository, times(1)).findByStatus("PROGRAMATA");
    }

    @Test
    void testUpdateConsultation_Success() {
        // Arrange
        Consultation updatedDetails = new Consultation();
        updatedDetails.setData(LocalDate.of(2025, 1, 15));
        updatedDetails.setOra(LocalTime.of(14, 0));
        updatedDetails.setMotiv("Control actualizat");
        updatedDetails.setDiagnostic("Hipertensiune");
        updatedDetails.setSimptome("Dureri de cap");
        updatedDetails.setRecomandari("Repaus");
        updatedDetails.setStatus("FINALIZATA");

        when(consultationRepository.findById(1L)).thenReturn(Optional.of(consultation));
        when(consultationRepository.save(any(Consultation.class))).thenReturn(consultation);

        // Act
        Consultation result = consultationService.updateConsultation(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    void testDeleteConsultation_Success() {
        // Arrange
        when(consultationRepository.findById(1L)).thenReturn(Optional.of(consultation));
        doNothing().when(consultationRepository).delete(consultation);

        // Act
        consultationService.deleteConsultation(1L);

        // Assert
        verify(consultationRepository, times(1)).delete(consultation);
    }
}