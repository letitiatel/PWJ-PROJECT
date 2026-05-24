import { useEffect, useMemo, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { format } from "date-fns";
import { getAllDoctors } from "../api/doctors";
import {
  createConsultation,
  getConsultationsByDoctorId,
} from "../api/consultations";
import { createPatient } from "../api/patients";
import { useAuth } from "../context/AuthContext";
import Calendar from "../components/Calendar";
import { CheckCircleIcon, UserIcon, CalendarIcon, ClockIcon, BriefcaseIcon } from "../components/Icons";
import { priceForSpecialty, formatPrice } from "../utils/pricing";

const TIME_SLOTS = [
  "09:00",
  "09:30",
  "10:00",
  "10:30",
  "11:00",
  "11:30",
  "12:00",
  "13:00",
  "13:30",
  "14:00",
  "14:30",
  "15:00",
  "15:30",
  "16:00",
];

export default function BookAppointmentPage() {
  const [searchParams] = useSearchParams();
  const preselectedDoctorId = searchParams.get("doctorId");
  const navigate = useNavigate();
  const { user } = useAuth();

  const [doctors, setDoctors] = useState([]);
  const [doctorId, setDoctorId] = useState(preselectedDoctorId || "");
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState("");
  const [bookedSlots, setBookedSlots] = useState(new Set());
  const [reason, setReason] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [reviewing, setReviewing] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState("");

  const [patient, setPatient] = useState({
    nume: user?.nume || "",
    prenume: user?.prenume || "",
    cnp: user?.cnp || "",
    dataNasterii: user?.dataNasterii || "",
    telefon: user?.telefon || "",
    email: user?.email || "",
    adresa: user?.adresa || "",
    alergii: user?.alergii || "",
  });

  useEffect(() => {
    getAllDoctors()
      .then(setDoctors)
      .catch((e) => {
        console.error(e);
        setError(
          "Nu am putut încărca lista de medici. Verifică dacă backend-ul rulează."
        );
      });
  }, []);

  useEffect(() => {
    if (!doctorId || !selectedDate) {
      setBookedSlots(new Set());
      return;
    }
    getConsultationsByDoctorId(doctorId)
      .then((list) => {
        const dateStr = format(selectedDate, "yyyy-MM-dd");
        const booked = new Set(
          list
            .filter((c) => c.data === dateStr && c.status !== "ANULATA")
            .map((c) => (c.ora || "").slice(0, 5))
        );
        setBookedSlots(booked);
      })
      .catch(() => setBookedSlots(new Set()));
  }, [doctorId, selectedDate]);

  const selectedDoctor = useMemo(
    () => doctors.find((d) => String(d.id) === String(doctorId)),
    [doctors, doctorId]
  );

  const price = useMemo(
    () => priceForSpecialty(selectedDoctor?.specializare),
    [selectedDoctor]
  );

  const onPatientChange = (e) =>
    setPatient((p) => ({ ...p, [e.target.name]: e.target.value }));

  const goToReview = (e) => {
    e.preventDefault();
    setError("");
    if (!doctorId) return setError("Selectează un medic.");
    if (!selectedDate) return setError("Selectează o dată.");
    if (!selectedTime) return setError("Selectează o oră.");
    if (!reason.trim())
      return setError("Te rugăm să introduci motivul consultației.");
    setReviewing(true);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const confirmBooking = async () => {
    setError("");
    setSubmitting(true);
    try {
      const createdPatient = await createPatient(patient);
      const consultation = {
        data: format(selectedDate, "yyyy-MM-dd"),
        ora: `${selectedTime}:00`,
        motiv: reason.trim(),
        status: "PROGRAMATA",
        patient: { id: createdPatient.id },
        doctor: { id: Number(doctorId) },
      };
      await createConsultation(consultation);
      setSuccess(true);
    } catch (err) {
      console.error(err);
      const msg =
        err.response?.data?.message ||
        err.response?.data?.error ||
        "Nu am putut salva programarea. Verifică datele introduse.";
      setError(msg);
    } finally {
      setSubmitting(false);
    }
  };

  if (success) {
    return (
      <div className="max-w-xl mx-auto px-4 py-16 text-center">
        <div className="w-16 h-16 mx-auto rounded-full bg-emerald-50 text-emerald-600 grid place-items-center">
          <CheckCircleIcon className="w-9 h-9" />
        </div>
        <h1 className="mt-4 text-2xl font-bold text-slate-900">
          Programarea ta a fost confirmată!
        </h1>
        <p className="mt-2 text-slate-600">
          Te așteptăm pe {format(selectedDate, "dd.MM.yyyy")} la ora{" "}
          {selectedTime} la Dr. {selectedDoctor?.prenume}{" "}
          {selectedDoctor?.nume}.
        </p>
        <p className="mt-3 inline-flex items-center gap-2 rounded-lg bg-brand-50 border border-brand-100 px-4 py-2 text-brand-800 font-semibold">
          Cost consultație: {formatPrice(price)}
        </p>
        <p className="mt-2 text-xs text-slate-400">
          Plata se face la recepție, în ziua consultației.
        </p>
        <div className="mt-8 flex justify-center gap-3">
          <button
            onClick={() => navigate("/")}
            className="px-5 py-2 rounded-lg border border-slate-300 hover:bg-slate-50"
          >
            Acasă
          </button>
          <button
            onClick={() => window.location.reload()}
            className="px-5 py-2 rounded-lg bg-brand-600 text-white font-semibold hover:bg-brand-700"
          >
            Programare nouă
          </button>
        </div>
      </div>
    );
  }

  if (reviewing) {
    return (
      <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
        <div className="flex items-center gap-2 text-sm text-slate-500">
          <span className="text-brand-700 font-medium">1. Detalii</span>
          <span>›</span>
          <span className="text-slate-900 font-semibold">2. Confirmare</span>
        </div>
        <h1 className="mt-2 text-3xl font-bold text-slate-900">
          Verifică programarea
        </h1>
        <p className="text-slate-500 mt-1">
          Confirmă detaliile de mai jos înainte de a finaliza.
        </p>

        <div className="mt-6 bg-white rounded-xl border border-slate-200 divide-y divide-slate-100">
          <SummaryRow icon={UserIcon} label="Pacient">
            {patient.nume} {patient.prenume}
          </SummaryRow>
          <SummaryRow icon={BriefcaseIcon} label="Medic">
            Dr. {selectedDoctor?.prenume} {selectedDoctor?.nume} —{" "}
            {selectedDoctor?.specializare}
          </SummaryRow>
          <SummaryRow icon={CalendarIcon} label="Data">
            {format(selectedDate, "dd.MM.yyyy")}
          </SummaryRow>
          <SummaryRow icon={ClockIcon} label="Ora">
            {selectedTime}
          </SummaryRow>
          <SummaryRow label="Motiv">{reason}</SummaryRow>
        </div>

        <div className="mt-4 flex items-center justify-between rounded-xl bg-brand-50 border border-brand-100 px-5 py-4">
          <div>
            <p className="text-sm text-slate-600">Cost consultație</p>
            <p className="text-xs text-slate-400">
              Plata se face la recepție, în ziua consultației.
            </p>
          </div>
          <span className="text-2xl font-extrabold text-brand-800">
            {formatPrice(price)}
          </span>
        </div>

        {error && (
          <p className="mt-4 text-sm text-red-600 bg-red-50 border border-red-200 rounded-md px-3 py-2">
            {error}
          </p>
        )}

        <div className="mt-6 flex flex-col-reverse sm:flex-row gap-3">
          <button
            type="button"
            onClick={() => setReviewing(false)}
            disabled={submitting}
            className="px-5 py-2.5 rounded-lg border border-slate-300 hover:bg-slate-50 disabled:opacity-60"
          >
            ← Înapoi la editare
          </button>
          <button
            type="button"
            onClick={confirmBooking}
            disabled={submitting}
            className="flex-1 px-5 py-2.5 rounded-lg bg-brand-600 text-white font-semibold hover:bg-brand-700 disabled:opacity-60"
          >
            {submitting
              ? "Se trimite..."
              : `Confirmă programarea · ${formatPrice(price)}`}
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="flex items-center gap-2 text-sm text-slate-500">
        <span className="text-slate-900 font-semibold">1. Detalii</span>
        <span>›</span>
        <span>2. Confirmare</span>
      </div>
      <h1 className="mt-2 text-3xl font-bold text-slate-900">
        Programează o consultație
      </h1>
      <p className="text-slate-500 mt-1">
        Completează datele tale, alege medicul, ziua și ora.
      </p>

      <form onSubmit={goToReview} className="mt-8 grid lg:grid-cols-3 gap-6">
        <section className="lg:col-span-2 bg-white rounded-xl border border-slate-200 p-6">
          <h2 className="font-semibold text-slate-900">Datele pacientului</h2>
          <div className="mt-4 grid sm:grid-cols-2 gap-3">
            <Field
              label="Nume"
              name="nume"
              value={patient.nume}
              onChange={onPatientChange}
              required
            />
            <Field
              label="Prenume"
              name="prenume"
              value={patient.prenume}
              onChange={onPatientChange}
              required
            />
            <Field
              label="CNP"
              name="cnp"
              value={patient.cnp}
              onChange={onPatientChange}
              maxLength={13}
              required
            />
            <Field
              label="Data nașterii"
              type="date"
              name="dataNasterii"
              value={patient.dataNasterii}
              onChange={onPatientChange}
              required
            />
            <Field
              label="Telefon"
              name="telefon"
              value={patient.telefon}
              onChange={onPatientChange}
              placeholder="07xxxxxxxx"
              required
            />
            <Field
              label="Email"
              type="email"
              name="email"
              value={patient.email}
              onChange={onPatientChange}
              required
            />
            <div className="sm:col-span-2">
              <Field
                label="Adresa"
                name="adresa"
                value={patient.adresa}
                onChange={onPatientChange}
                required
              />
            </div>
            <div className="sm:col-span-2">
              <Field
                label="Alergii (opțional)"
                name="alergii"
                value={patient.alergii}
                onChange={onPatientChange}
              />
            </div>
          </div>

          <h2 className="font-semibold text-slate-900 mt-8">
            Detalii programare
          </h2>
          <div className="mt-4 space-y-3">
            <label className="block">
              <span className="text-sm font-medium text-slate-700">Medic</span>
              <select
                value={doctorId}
                onChange={(e) => {
                  setDoctorId(e.target.value);
                  setSelectedTime("");
                }}
                required
                className="mt-1 block w-full rounded-lg border border-slate-300 px-3 py-2 bg-white focus:border-brand-500 focus:ring-2 focus:ring-brand-100 outline-none"
              >
                <option value="">Selectează un medic...</option>
                {doctors.map((d) => (
                  <option key={d.id} value={d.id}>
                    Dr. {d.prenume} {d.nume} — {d.specializare}
                  </option>
                ))}
              </select>
            </label>
            <label className="block">
              <span className="text-sm font-medium text-slate-700">
                Motivul consultației
              </span>
              <textarea
                value={reason}
                onChange={(e) => setReason(e.target.value)}
                rows={3}
                required
                placeholder="Descrie pe scurt motivul..."
                className="mt-1 block w-full rounded-lg border border-slate-300 px-3 py-2 focus:border-brand-500 focus:ring-2 focus:ring-brand-100 outline-none"
              />
            </label>
          </div>
        </section>

        <section className="bg-white rounded-xl border border-slate-200 p-6">
          <h2 className="font-semibold text-slate-900">Alege ziua și ora</h2>
          <div className="mt-4">
            <Calendar
              selected={selectedDate}
              onSelect={(d) => {
                setSelectedDate(d);
                setSelectedTime("");
              }}
            />
          </div>

          {selectedDate && (
            <div className="mt-5">
              <p className="text-sm font-medium text-slate-700 mb-2">
                Ore disponibile pentru{" "}
                <span className="text-brand-700">
                  {format(selectedDate, "dd.MM.yyyy")}
                </span>
              </p>
              <div className="grid grid-cols-3 gap-2">
                {TIME_SLOTS.map((t) => {
                  const taken = bookedSlots.has(t);
                  const active = selectedTime === t;
                  return (
                    <button
                      key={t}
                      type="button"
                      disabled={taken}
                      onClick={() => setSelectedTime(t)}
                      className={`py-1.5 rounded-md text-sm border transition ${
                        taken
                          ? "bg-slate-100 text-slate-300 border-slate-200 cursor-not-allowed line-through"
                          : active
                          ? "bg-brand-600 text-white border-brand-600 font-semibold"
                          : "bg-white text-slate-700 border-slate-300 hover:border-brand-400"
                      }`}
                    >
                      {t}
                    </button>
                  );
                })}
              </div>
            </div>
          )}

          {selectedDoctor && (
            <div className="mt-5 flex items-center justify-between rounded-lg bg-slate-50 border border-slate-200 px-4 py-2.5">
              <span className="text-sm text-slate-600">Cost estimat</span>
              <span className="font-semibold text-slate-900">
                {formatPrice(price)}
              </span>
            </div>
          )}

          {error && (
            <p className="mt-4 text-sm text-red-600 bg-red-50 border border-red-200 rounded-md px-3 py-2">
              {error}
            </p>
          )}

          <button
            type="submit"
            className="mt-6 w-full py-3 rounded-lg bg-brand-600 text-white font-semibold hover:bg-brand-700 disabled:opacity-60"
          >
            Continuă spre confirmare →
          </button>
        </section>
      </form>
    </div>
  );
}

function SummaryRow({ icon: Icon, label, children }) {
  return (
    <div className="flex items-start gap-3 px-5 py-3.5">
      {Icon ? (
        <Icon className="w-5 h-5 text-slate-400 mt-0.5 shrink-0" />
      ) : (
        <span className="w-5 shrink-0" />
      )}
      <div className="min-w-0">
        <p className="text-xs text-slate-400">{label}</p>
        <p className="text-slate-800">{children}</p>
      </div>
    </div>
  );
}

function Field({ label, ...props }) {
  return (
    <label className="block">
      <span className="text-sm font-medium text-slate-700">{label}</span>
      <input
        {...props}
        className="mt-1 block w-full rounded-lg border border-slate-300 px-3 py-2 focus:border-brand-500 focus:ring-2 focus:ring-brand-100 outline-none"
      />
    </label>
  );
}
