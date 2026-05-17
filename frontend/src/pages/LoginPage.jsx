import { useMemo, useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { checkPasswordRules } from "../utils/password";
import { EyeIcon, EyeOffIcon } from "../components/Icons";

export default function LoginPage() {
  const [mode, setMode] = useState("login");
  const [form, setForm] = useState({
    email: "",
    parola: "",
    confirmParola: "",
  });
  const [error, setError] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const { login, register } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from || "/";

  const isRegister = mode === "register";
  const rules = useMemo(() => checkPasswordRules(form.parola), [form.parola]);
  const confirmMismatch =
    isRegister &&
    form.confirmParola.length > 0 &&
    form.confirmParola !== form.parola;

  const onChange = (e) =>
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));

  const submit = (e) => {
    e.preventDefault();
    setError("");

    if (isRegister) {
      if (!rules.allValid) {
        setError("Parola nu îndeplinește toate cerințele.");
        return;
      }
      if (form.parola !== form.confirmParola) {
        setError("Parolele introduse nu se potrivesc.");
        return;
      }
    }

    try {
      if (mode === "login") {
        login(form.email, form.parola);
        navigate(from, { replace: true });
      } else {
        register({ email: form.email, parola: form.parola });
        navigate("/profile", { replace: true });
      }
    } catch (err) {
      setError(err.message);
    }
  };

  const switchMode = (next) => {
    setMode(next);
    setError("");
    setShowPassword(false);
    setShowConfirm(false);
    setForm({ email: form.email, parola: "", confirmParola: "" });
  };

  return (
    <div className="max-w-md mx-auto px-4 py-12">
      <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-8">
        <h1 className="text-2xl font-bold text-slate-900">
          {isRegister ? "Creează cont" : "Bine ai revenit"}
        </h1>
        <p className="text-sm text-slate-500 mt-1">
          {isRegister
            ? "Ai nevoie doar de un email și o parolă. Restul datelor le poți completa apoi în contul tău."
            : "Autentifică-te pentru a-ți gestiona programările."}
        </p>

        <div className="mt-6 inline-flex rounded-lg bg-slate-100 p-1">
          <button
            type="button"
            onClick={() => switchMode("login")}
            className={`px-4 py-1.5 text-sm rounded-md ${
              !isRegister
                ? "bg-white shadow text-brand-700 font-semibold"
                : "text-slate-600"
            }`}
          >
            Autentificare
          </button>
          <button
            type="button"
            onClick={() => switchMode("register")}
            className={`px-4 py-1.5 text-sm rounded-md ${
              isRegister
                ? "bg-white shadow text-brand-700 font-semibold"
                : "text-slate-600"
            }`}
          >
            Creează cont
          </button>
        </div>

        <form onSubmit={submit} className="mt-6 space-y-4" noValidate>
          <label className="block">
            <span className="text-sm font-medium text-slate-700">Email</span>
            <input
              type="email"
              name="email"
              autoComplete="email"
              value={form.email}
              onChange={onChange}
              required
              className="mt-1 block w-full rounded-lg border border-slate-300 px-3 py-2 focus:border-brand-500 focus:ring-2 focus:ring-brand-100 outline-none"
            />
            {isRegister && (
              <span className="mt-1 block text-xs text-slate-400">
                Email-ul este normalizat (litere mici) — nu contează cum îl
                scrii.
              </span>
            )}
          </label>

          <PasswordField
            label="Parolă"
            name="parola"
            value={form.parola}
            onChange={onChange}
            visible={showPassword}
            onToggleVisibility={() => setShowPassword((v) => !v)}
            autoComplete={isRegister ? "new-password" : "current-password"}
          />

          {isRegister && (
            <>
              <PasswordField
                label="Confirmă parola"
                name="confirmParola"
                value={form.confirmParola}
                onChange={onChange}
                visible={showConfirm}
                onToggleVisibility={() => setShowConfirm((v) => !v)}
                autoComplete="new-password"
                error={
                  confirmMismatch ? "Parolele nu se potrivesc." : ""
                }
              />
              <PasswordRules rules={rules.results} />
              <p className="text-xs text-slate-500">
                Parolele sunt sensibile la litere mari/mici —{" "}
                <code className="px-1 bg-slate-100 rounded">Parola1!</code> și{" "}
                <code className="px-1 bg-slate-100 rounded">parola1!</code>{" "}
                sunt diferite.
              </p>
            </>
          )}

          {error && (
            <p className="text-sm text-red-600 bg-red-50 border border-red-200 rounded-md px-3 py-2">
              {error}
            </p>
          )}

          <button
            type="submit"
            disabled={
              isRegister && (!rules.allValid || confirmMismatch)
            }
            className="w-full py-2.5 rounded-lg bg-brand-600 text-white font-semibold hover:bg-brand-700 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {isRegister ? "Creează contul" : "Autentificare"}
          </button>
        </form>

        <p className="text-xs text-slate-400 text-center mt-4">
          Pentru moment, contul este salvat local (frontend-only).
        </p>
      </div>

      <p className="text-center text-sm text-slate-500 mt-6">
        <Link to="/" className="hover:text-brand-700">
          ← Înapoi la pagina principală
        </Link>
      </p>
    </div>
  );
}

function PasswordField({
  label,
  name,
  value,
  onChange,
  visible,
  onToggleVisibility,
  autoComplete,
  error,
}) {
  return (
    <label className="block">
      <span className="text-sm font-medium text-slate-700">{label}</span>
      <div className="relative mt-1">
        <input
          type={visible ? "text" : "password"}
          name={name}
          value={value}
          onChange={onChange}
          required
          autoComplete={autoComplete}
          className={`block w-full rounded-lg border px-3 py-2 pr-10 outline-none ${
            error
              ? "border-red-400 focus:border-red-500 focus:ring-2 focus:ring-red-100"
              : "border-slate-300 focus:border-brand-500 focus:ring-2 focus:ring-brand-100"
          }`}
        />
        <button
          type="button"
          onClick={onToggleVisibility}
          aria-label={visible ? "Ascunde parola" : "Arată parola"}
          aria-pressed={visible}
          className="absolute right-2 top-1/2 -translate-y-1/2 p-1.5 rounded-md text-slate-500 hover:bg-slate-100 hover:text-slate-700"
        >
          {visible ? <EyeOffIcon className="w-5 h-5" /> : <EyeIcon className="w-5 h-5" />}
        </button>
      </div>
      {error && (
        <span className="mt-1 block text-xs text-red-600">{error}</span>
      )}
    </label>
  );
}

function PasswordRules({ rules }) {
  return (
    <div className="rounded-lg border border-slate-200 bg-slate-50 p-3">
      <p className="text-xs font-semibold text-slate-700 mb-2">
        Parola trebuie să aibă:
      </p>
      <ul className="space-y-1">
        {rules.map((r) => (
          <li
            key={r.key}
            className={`text-xs flex items-center gap-2 ${
              r.valid ? "text-emerald-700" : "text-slate-500"
            }`}
          >
            <span
              className={`inline-flex w-4 h-4 rounded-full items-center justify-center text-[10px] font-bold ${
                r.valid
                  ? "bg-emerald-100 text-emerald-700"
                  : "bg-slate-200 text-slate-400"
              }`}
              aria-hidden
            >
              {r.valid ? "✓" : "•"}
            </span>
            {r.label}
          </li>
        ))}
      </ul>
    </div>
  );
}

