package com.cabinetmedical.service;

import com.cabinetmedical.model.Consultation;
import com.cabinetmedical.model.Doctor;
import com.cabinetmedical.model.Patient;
import com.cabinetmedical.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Transactional
    public Consultation createConsultation(Consultation consultation) {

        Patient patient = patientService.getPatientById(consultation.getPatient().getId());
        consultation.setPatient(patient);

        Doctor doctor = doctorService.getDoctorById(consultation.getDoctor().getId());
        consultation.setDoctor(doctor);

        List<Consultation> existingConsultations = consultationRepository
                .findByDoctorIdAndDataAndOra(doctor.getId(), consultation.getData(), consultation.getOra());

        if (!existingConsultations.isEmpty()) {
            throw new RuntimeException("Medicul are deja o consultație programată la această dată și oră");
        }

        return consultationRepository.save(consultation);
    }

    @Transactional(readOnly = true)
    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Consultation getConsultationById(Long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Consultation> getConsultationsByPatientId(Long patientId) {
        patientService.getPatientById(patientId);
        return consultationRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<Consultation> getConsultationsByDoctorId(Long doctorId) {
        doctorService.getDoctorById(doctorId);
        return consultationRepository.findByDoctorId(doctorId);
    }

    @Transactional(readOnly = true)
    public List<Consultation> getConsultationsByStatus(String status) {
        return consultationRepository.findByStatus(status);
    }

    @Transactional
    public Consultation updateConsultation(Long id, Consultation consultationDetails) {
        Consultation consultation = getConsultationById(id);

        consultation.setData(consultationDetails.getData());
        consultation.setOra(consultationDetails.getOra());
        consultation.setMotiv(consultationDetails.getMotiv());
        consultation.setDiagnostic(consultationDetails.getDiagnostic());
        consultation.setSimptome(consultationDetails.getSimptome());
        consultation.setRecomandari(consultationDetails.getRecomandari());
        consultation.setStatus(consultationDetails.getStatus());

        return consultationRepository.save(consultation);
    }

    @Transactional
    public void deleteConsultation(Long id) {
        Consultation consultation = getConsultationById(id);
        consultationRepository.delete(consultation);
    }
}