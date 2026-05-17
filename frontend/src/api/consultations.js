import client from "./client";

export const createConsultation = (consultation) =>
  client.post("/consultations", consultation).then((r) => r.data);

export const getConsultationsByDoctorId = (doctorId) =>
  client.get(`/consultations/doctor/${doctorId}`).then((r) => r.data);

export const getConsultationsByPatientId = (patientId) =>
  client.get(`/consultations/patient/${patientId}`).then((r) => r.data);
