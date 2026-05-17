import { useEffect, useMemo, useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const NAME_RE = /^[\p{L}][\p{L} .'\-]{1,49}$/u;
const CNP_RE = /^\d{13}$/;
const PHONE_RE = /^(\+40|0)\d{9}$/;

const FIELD_LABELS = {
  nume: "Nume",
  prenume: "Prenume",
  cnp: "CNP",
  dataNasterii: "Data nașterii",
  telefon: "Telefon",
  adresa: "Adresa",
  alergii: "Alergii",
};

function validate(form) {
  const errs = {};
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  if (!form.nume.trim()) {
    errs.nume = "Numele este obligatoriu.";
  } else if (!NAME_RE.test(form.nume.trim())) {
    errs.nume =
      "Numele poate conține litere (inclusiv ă, â, î, ș, ț), spații, cratimă (-) sau apostrof.";
  }

  if (!form.prenume.trim()) {
    errs.prenume = "Prenumele este obligatoriu.";
  } else if (!NAME_RE.test(form.prenume.trim())) {
    errs.prenume =
      "Prenumele poate conține litere (inclusiv ă, â, î, ș, ț), spații, cratimă (-) sau apostrof.";
  }

  if (!form.cnp.trim()) {
    errs.cnp = "CNP-ul este obligatoriu.";
  } else if (!CNP_RE.test(form.cnp.trim())) {
    errs.cnp = "CNP-ul trebuie să conțină exact 13 cifre.";
  }

  if (!form.dataNasterii) {
    errs.dataNasterii = "Data nașterii este obligatorie.";
  } else {
    const d = new Date(form.dataNasterii);
    if (Number.isNaN(d.getTime()) || d >= today) {
      errs.dataNasterii = "Data nașterii trebuie să fie în trecut.";
    }
  }

  if (!form.telefon.trim()) {
    errs.telefon = "Telefonul este obligatoriu.";
  } else if (!PHONE_RE.test(form.telefon.trim())) {
    errs.telefon = "Formatul telefonului este invalid (ex: 07xxxxxxxx sau +40xxxxxxxxx).";
  }

  if (!form.adresa.trim()) {
    errs.adresa = "Adresa este obligatorie.";
  } else if (form.adresa.trim().length > 200) {
    errs.adresa = "Adresa nu poate depăși 200 de caractere.";
  }

  if (form.alergii && form.alergii.length > 500) {
    errs.alergii = "Alergiile nu pot depăși 500 de caractere.";
  }

  return errs;
}

export default function ProfilePage() {
  const { user, ready, updateProfile } = useAuth();
  const navigate = useNavigate();
  const [saved, setSaved] = useState(false);
  const [submitError, setSubmitError] = useState("");
  const [touched, setTouched] = useState({});
  const [attempted, setAttempted] = useState(false);
  const [form, setForm] = useState({
    nume: "",
    prenume: "",
    cnp: "",
    dataNasterii: "",
    telefon: "",
    adresa: "",
    alergii: "",
  });

  useEffect(() => {
    if (user) {
      setForm({
        nume: user.nume || "",
        prenume: user.prenume || "",
        cnp: user.cnp || "",
        dataNasterii: user.dataNasterii || "",
        telefon: user.telefon || "",
        adresa: user.adresa || "",
        alergii: user.alergii || "",
      });
    }
  }, [user]);

  const errors = useMemo(() => validate(form), [form]);

  if (!ready) return null;
  if (!user) return <Navigate to="/login" replace state={{ from: "/profile" }} />;

  const onChange = (e) => {
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));
    setSaved(false);
  };
  const onBlur = (e) =>
    setTouched((t) => ({ ...t, [e.target.name]: true }));

  const showError = (name) => (attempted || touched[name]) && errors[name];

  const submit = (e) => {
    e.preventDefault();
    setAttempted(true);
    setSubmitError("");
    if (Object.keys(errors).length > 0) {
      setSubmitError(
        "Formularul conține erori. Verifică câmpurile marcate cu roșu."
      );
      return;
    }
    try {
      updateProfile({
        ...form,
        nume: form.nume.trim(),
        prenume: form.prenume.trim(),
        cnp: form.cnp.trim(),
        telefon: form.telefon.trim(),
        adresa: form.adresa.trim(),
        alergii: form.alergii.trim(),
      });
      setSaved(true);
      setTimeout(() => setSaved(false), 2500);
    } catch (err) {
      setSubmitError(err.message || "Nu am putut salva modificările.");
    }
  };

  const isComplete =
    form.nume && form.prenume && form.cnp && form.dataNasterii && form.telefon && form.adresa;

  const errorList = attempted
    ? Object.entries(errors).map(([name, msg]) => ({
        label: FIELD_LABELS[name] || name,
        msg,
      }))
    : [];

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="flex items-center justify-between flex-wrap gap-3">
        <div>
          <h1 className="text-3xl font-bold text-slate-900">Contul meu</h1>
          <p className="text-slate-500 mt-1">
            Completează-ți datele o singură dată — vor fi reutilizate la fiecare
            programare.
          </p>
        </div>
        <span className="text-sm text-slate-500">
          Logat ca <strong>{user.email}</strong>
        </span>
      </div>

      {!isComplete && (
        <div className="mt-6 p-4 rounded-lg bg-amber-50 border border-amber-200 text-amber-800 text-sm">
          Profil incomplet — completează datele de mai jos pentru a putea face o
          programare rapidă.
        </div>
      )}

      <form
        onSubmit={submit}
        noValidate
        className="mt-6 bg-white rounded-xl border border-slate-200 p-6 space-y-4"
      >
        <div className="grid sm:grid-cols-2 gap-3">
          <Field
            label="Nume"
            name="nume"
            value={form.nume}
            onChange={onChange}
            onBlur={onBlur}
            error={showError("nume") ? errors.nume : ""}
            hint="Litere, spații, cratimă (-) sau apostrof — ex: Popescu-Ionescu"
            required
          />
          <Field
            label="Prenume"
            name="prenume"
            value={form.prenume}
            onChange={onChange}
            onBlur={onBlur}
            error={showError("prenume") ? errors.prenume : ""}
            hint="Ex: Ana-Maria, D'Artagnan"
            required
          />
          <Field
            label="CNP"
            name="cnp"
            value={form.cnp}
            onChange={onChange}
            onBlur={onBlur}
            error={showError("cnp") ? errors.cnp : ""}
            inputMode="numeric"
            maxLength={13}
            required
          />
          <Field
            label="Data nașterii"
            type="date"
            name="dataNasterii"
            value={form.dataNasterii}
            onChange={onChange}
            onBlur={onBlur}
            error={showError("dataNasterii") ? errors.dataNasterii : ""}
            required
          />
          <Field
            label="Telefon"
            name="telefon"
            value={form.telefon}
            onChange={onChange}
            onBlur={onBlur}
            error={showError("telefon") ? errors.telefon : ""}
            placeholder="07xxxxxxxx"
            required
          />
          <Field
            label="Email"
            name="email"
            value={user.email}
            disabled
          />
        </div>
        <Field
          label="Adresa"
          name="adresa"
          value={form.adresa}
          onChange={onChange}
          onBlur={onBlur}
          error={showError("adresa") ? errors.adresa : ""}
          required
        />
        <Field
          label="Alergii (opțional)"
          name="alergii"
          value={form.alergii}
          onChange={onChange}
          onBlur={onBlur}
          error={showError("alergii") ? errors.alergii : ""}
        />

        {(submitError || errorList.length > 0) && (
          <div className="text-sm text-red-700 bg-red-50 border border-red-200 rounded-md px-3 py-3">
            <p className="font-semibold">
              {submitError || "Te rugăm să corectezi următoarele câmpuri:"}
            </p>
            {errorList.length > 0 && (
              <ul className="mt-2 list-disc list-inside space-y-1">
                {errorList.map((e) => (
                  <li key={e.label}>
                    <strong>{e.label}:</strong> {e.msg}
                  </li>
                ))}
              </ul>
            )}
          </div>
        )}
        {saved && (
          <p className="text-sm text-emerald-700 bg-emerald-50 border border-emerald-200 rounded-md px-3 py-2">
            Modificările au fost salvate.
          </p>
        )}

        <div className="flex flex-wrap gap-3 pt-2">
          <button
            type="submit"
            className="px-5 py-2 rounded-lg bg-brand-600 text-white font-semibold hover:bg-brand-700"
          >
            Salvează
          </button>
          <button
            type="button"
            onClick={() => navigate("/book")}
            className="px-5 py-2 rounded-lg border border-slate-300 hover:bg-slate-50"
          >
            Programează o consultație
          </button>
        </div>
      </form>
    </div>
  );
}

function Field({ label, error, hint, ...props }) {
  const base =
    "mt-1 block w-full rounded-lg border px-3 py-2 outline-none disabled:bg-slate-100 disabled:text-slate-500";
  const stateCls = error
    ? "border-red-400 focus:border-red-500 focus:ring-2 focus:ring-red-100"
    : "border-slate-300 focus:border-brand-500 focus:ring-2 focus:ring-brand-100";
  return (
    <label className="block">
      <span className="text-sm font-medium text-slate-700">{label}</span>
      <input
        aria-invalid={!!error}
        {...props}
        className={`${base} ${stateCls}`}
      />
      {error ? (
        <span className="mt-1 block text-xs text-red-600">{error}</span>
      ) : hint ? (
        <span className="mt-1 block text-xs text-slate-400">{hint}</span>
      ) : null}
    </label>
  );
}
