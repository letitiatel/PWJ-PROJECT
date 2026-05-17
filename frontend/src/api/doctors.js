import client from "./client";

export const getAllDoctors = (specializare) => {
  const params = specializare ? { specializare } : {};
  return client.get("/doctors", { params }).then((r) => r.data);
};

export const getDoctorById = (id) =>
  client.get(`/doctors/${id}`).then((r) => r.data);
