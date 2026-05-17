import client from "./client";

export const createPatient = (patient) =>
  client.post("/patients", patient).then((r) => r.data);

export const getPatientById = (id) =>
  client.get(`/patients/${id}`).then((r) => r.data);

export const getAllPatients = () =>
  client.get("/patients").then((r) => r.data);
