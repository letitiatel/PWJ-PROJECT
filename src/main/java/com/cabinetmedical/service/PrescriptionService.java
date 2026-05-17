package com.cabinetmedical.service;

import com.cabinetmedical.model.Consultation;
import com.cabinetmedical.model.Doctor;
import com.cabinetmedical.model.Patient;
import com.cabinetmedical.model.Prescription;
import com.cabinetmedical.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ConsultationService consultationService;

    @Transactional
    public Prescription createPrescription(Prescription prescription) {

        Patient patient = patientService.getPatientById(prescription.getPatient().getId());
        prescription.setPatient(patient);

        Doctor doctor = doctorService.getDoctorById(prescription.getDoctor().getId());
        prescription.setDoctor(doctor);

        if (prescription.getConsultation() != null && prescription.getConsultation().getId() != null) {
            Consultation consultation = consultationService.getConsultationById(prescription.getConsultation().getId());
            prescription.setConsultation(consultation);
        }

        if (prescription.getDataEmitere() == null) {
            prescription.setDataEmitere(LocalDate.now());
        }

        return prescriptionRepository.save(prescription);
    }

    @Transactional(readOnly = true)
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        patientService.getPatientById(patientId);
        return prescriptionRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsByDoctorId(Long doctorId) {
        doctorService.getDoctorById(doctorId);
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsByStatus(String status) {
        return prescriptionRepository.findByStatus(status);
    }

    @Transactional
    public Prescription updatePrescription(Long id, Prescription prescriptionDetails) {
        Prescription prescription = getPrescriptionById(id);

        prescription.setObservatii(prescriptionDetails.getObservatii());
        prescription.setStatus(prescriptionDetails.getStatus());

        return prescriptionRepository.save(prescription);
    }

    @Transactional
    public Prescription updatePrescriptionStatus(Long id, String status) {
        Prescription prescription = getPrescriptionById(id);
        prescription.setStatus(status);
        return prescriptionRepository.save(prescription);
    }

    @Transactional
    public void deletePrescription(Long id) {
        Prescription prescription = getPrescriptionById(id);
        prescriptionRepository.delete(prescription);
    }
}