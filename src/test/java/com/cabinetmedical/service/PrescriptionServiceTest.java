package com.cabinetmedical.service;

import com.cabinetmedical.model.Consultation;
import com.cabinetmedical.model.Doctor;
import com.cabinetmedical.model.Patient;
import com.cabinetmedical.model.Prescription;
import com.cabinetmedical.repository.PrescriptionRepository;
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
class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private ConsultationService consultationService;

    @InjectMocks
    private PrescriptionService prescriptionService;

    private Prescription prescription;
    private Patient patient;
    private Doctor doctor;
    private Consultation consultation;

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
    void testCreatePrescription_Success() {
        // Arrange
        when(patientService.getPatientById(1L)).thenReturn(patient);
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(consultationService.getConsultationById(1L)).thenReturn(consultation);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Act
        Prescription result = prescriptionService.createPrescription(prescription);

        // Assert
        assertNotNull(result);
        assertEquals("ACTIVA", result.getStatus());
        assertEquals("A se lua după masă", result.getObservatii());
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    void testCreatePrescription_WithoutConsultation() {
        // Arrange
        prescription.setConsultation(null);
        when(patientService.getPatientById(1L)).thenReturn(patient);
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Act
        Prescription result = prescriptionService.createPrescription(prescription);

        // Assert
        assertNotNull(result);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    void testGetAllPrescriptions() {
        // Arrange
        List<Prescription> prescriptions = Arrays.asList(prescription);
        when(prescriptionRepository.findAll()).thenReturn(prescriptions);

        // Act
        List<Prescription> result = prescriptionService.getAllPrescriptions();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(prescriptionRepository, times(1)).findAll();
    }

    @Test
    void testGetPrescriptionById_Success() {
        // Arrange
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));

        // Act
        Prescription result = prescriptionService.getPrescriptionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(prescriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPrescriptionById_NotFound() {
        // Arrange
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            prescriptionService.getPrescriptionById(1L);
        });
    }

    @Test
    void testGetPrescriptionsByPatientId() {
        // Arrange
        when(patientService.getPatientById(1L)).thenReturn(patient);
        when(prescriptionRepository.findByPatientId(1L)).thenReturn(Arrays.asList(prescription));

        // Act
        List<Prescription> result = prescriptionService.getPrescriptionsByPatientId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(prescriptionRepository, times(1)).findByPatientId(1L);
    }

    @Test
    void testGetPrescriptionsByDoctorId() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(prescriptionRepository.findByDoctorId(1L)).thenReturn(Arrays.asList(prescription));

        // Act
        List<Prescription> result = prescriptionService.getPrescriptionsByDoctorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(prescriptionRepository, times(1)).findByDoctorId(1L);
    }

    @Test
    void testGetPrescriptionsByStatus() {
        // Arrange
        when(prescriptionRepository.findByStatus("ACTIVA")).thenReturn(Arrays.asList(prescription));

        // Act
        List<Prescription> result = prescriptionService.getPrescriptionsByStatus("ACTIVA");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACTIVA", result.get(0).getStatus());
        verify(prescriptionRepository, times(1)).findByStatus("ACTIVA");
    }

    @Test
    void testUpdatePrescription_Success() {
        // Arrange
        Prescription updatedDetails = new Prescription();
        updatedDetails.setObservatii("Observații actualizate");
        updatedDetails.setStatus("ELIBERATA");

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Act
        Prescription result = prescriptionService.updatePrescription(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    void testUpdatePrescriptionStatus_Success() {
        // Arrange
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Act
        Prescription result = prescriptionService.updatePrescriptionStatus(1L, "ELIBERATA");

        // Assert
        assertNotNull(result);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    void testDeletePrescription_Success() {
        // Arrange
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        doNothing().when(prescriptionRepository).delete(prescription);

        // Act
        prescriptionService.deletePrescription(1L);

        // Assert
        verify(prescriptionRepository, times(1)).delete(prescription);
    }
}