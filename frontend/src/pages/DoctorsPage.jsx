import { useEffect, useMemo, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { getAllDoctors } from "../api/doctors";
import {
  SearchIcon,
  StarIcon,
  PhoneIcon,
  MailIcon,
  BriefcaseIcon,
} from "../components/Icons";

const KNOWN_SPECIALTIES = [
  "Cardiologie",
  "Dermatologie",
  "Pediatrie",
  "Neurologie",
  "Ortopedie",
  "Oftalmologie",
  "ORL",
  "Ginecologie",
  "Medicină generală",
];

const PAGE_SIZE = 6;

export default function DoctorsPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [query, setQuery] = useState("");
  const [page, setPage] = useState(1);

  const specializare = searchParams.get("specializare") || "";

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError("");
    getAllDoctors(specializare || undefined)
      .then((data) => {
        if (!cancelled) setDoctors(data);
      })
      .catch((e) => {
        if (!cancelled) {
          console.error(e);
          setError(
            "Nu am putut încărca lista de medici. Verifică dacă backend-ul rulează pe localhost:8080."
          );
        }
      })
      .finally(() => !cancelled && setLoading(false));
    return () => {
      cancelled = true;
    };
  }, [specializare]);

  const specialties = useMemo(() => {
    const found = new Set(KNOWN_SPECIALTIES);
    doctors.forEach((d) => d.specializare && found.add(d.specializare));
    return Array.from(found).sort();
  }, [doctors]);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return doctors;
    return doctors.filter((d) =>
      `${d.nume} ${d.prenume}`.toLowerCase().includes(q)
    );
  }, [doctors, query]);

  useEffect(() => {
    setPage(1);
  }, [query, specializare]);

  const totalPages = Math.max(1, Math.ceil(filtered.length / PAGE_SIZE));
  const currentPage = Math.min(page, totalPages);
  const pageItems = useMemo(() => {
    const start = (currentPage - 1) * PAGE_SIZE;
    return filtered.slice(start, start + PAGE_SIZE);
  }, [filtered, currentPage]);

  const setSpecialty = (val) => {
    if (val) searchParams.set("specializare", val);
    else searchParams.delete("specializare");
    setSearchParams(searchParams, { replace: true });
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-slate-900">Medicii noștri</h1>
          <p className="text-slate-500 mt-1">
            Caută după nume sau filtrează după specializare.
          </p>
        </div>
      </div>

      <div className="mt-6 grid sm:grid-cols-2 gap-3">
        <div className="relative">
          <SearchIcon className="w-5 h-5 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2 pointer-events-none" />
          <input
            type="search"
            placeholder="Caută un medic după nume..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="w-full rounded-lg border border-slate-300 pl-10 pr-4 py-2.5 focus:border-brand-500 focus:ring-2 focus:ring-brand-100 outline-none"
          />
        </div>
        <select
          value={specializare}
          onChange={(e) => setSpecialty(e.target.value)}
          className="w-full rounded-lg border border-slate-300 px-4 py-2.5 bg-white focus:border-brand-500 focus:ring-2 focus:ring-brand-100 outline-none"
        >
          <option value="">Toate specializările</option>
          {specialties.map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </select>
      </div>

      <div className="mt-8">
        {loading ? (
          <p className="text-slate-500">Se încarcă medicii...</p>
        ) : error ? (
          <div className="p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg">
            {error}
          </div>
        ) : filtered.length === 0 ? (
          <div className="p-8 bg-white rounded-xl border border-dashed border-slate-300 text-center text-slate-500">
            Niciun medic găsit cu criteriile selectate.
          </div>
        ) : (
          <>
            <div className="flex items-center justify-between text-sm text-slate-500 mb-3">
              <span>
                Afișare {(currentPage - 1) * PAGE_SIZE + 1}–
                {Math.min(currentPage * PAGE_SIZE, filtered.length)} din{" "}
                {filtered.length} medici
              </span>
            </div>
            <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-5">
              {pageItems.map((d) => (
                <DoctorCard key={d.id} doctor={d} />
              ))}
            </div>
            {totalPages > 1 && (
              <Pagination
                page={currentPage}
                totalPages={totalPages}
                onChange={setPage}
              />
            )}
          </>
        )}
      </div>
    </div>
  );
}

function DoctorCard({ doctor }) {
  const initials = `${doctor.prenume?.[0] || ""}${doctor.nume?.[0] || ""}`;
  const hasRating = typeof doctor.rating === "number";
  const hasExperience = typeof doctor.aniExperienta === "number";

  return (
    <div className="bg-white rounded-xl border border-slate-200 p-5 hover:shadow-md transition flex flex-col">
      <div className="flex items-center gap-3">
        <div className="w-14 h-14 rounded-full bg-brand-100 text-brand-700 font-bold grid place-items-center text-lg">
          {initials.toUpperCase() || "Dr"}
        </div>
        <div className="min-w-0">
          <h3 className="font-semibold text-slate-900 truncate">
            Dr. {doctor.prenume} {doctor.nume}
          </h3>
          <p className="text-sm text-brand-700">{doctor.specializare}</p>
        </div>
      </div>

      <div className="mt-4 flex flex-wrap items-center gap-2">
        {hasRating ? (
          <span
            className="inline-flex items-center gap-1.5 px-2 py-0.5 rounded-md bg-amber-50 border border-amber-200 text-amber-700 text-sm font-semibold"
            title="Rating mediu acordat de pacienți"
          >
            <StarIcon className="w-4 h-4" />
            {doctor.rating.toFixed(1)}/10
          </span>
        ) : (
          <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-md bg-slate-50 border border-slate-200 text-slate-500 text-xs">
            Nou pe platformă
          </span>
        )}
        {hasExperience && (
          <span
            className="inline-flex items-center gap-1.5 px-2 py-0.5 rounded-md bg-brand-50 border border-brand-100 text-brand-700 text-sm font-medium"
            title="Ani de experiență"
          >
            <BriefcaseIcon className="w-4 h-4" />
            {doctor.aniExperienta} ani
          </span>
        )}
      </div>

      <div className="mt-4 text-sm text-slate-500 space-y-1.5">
        <p className="flex items-center gap-2">
          <PhoneIcon className="w-4 h-4 text-slate-400" />
          {doctor.telefon}
        </p>
        <p className="flex items-center gap-2">
          <MailIcon className="w-4 h-4 text-slate-400" />
          {doctor.email}
        </p>
      </div>
      <Link
        to={`/book?doctorId=${doctor.id}`}
        className="mt-5 inline-flex justify-center px-4 py-2 rounded-lg bg-brand-600 text-white font-semibold hover:bg-brand-700"
      >
        Programează consultație
      </Link>
    </div>
  );
}

function Pagination({ page, totalPages, onChange }) {
  const pages = useMemo(() => buildPageList(page, totalPages), [page, totalPages]);
  const btnBase =
    "min-w-[2.25rem] h-9 px-3 rounded-md text-sm font-medium border transition";
  return (
    <nav
      aria-label="Paginare medici"
      className="mt-8 flex items-center justify-center gap-1.5 flex-wrap"
    >
      <button
        type="button"
        onClick={() => onChange(page - 1)}
        disabled={page === 1}
        className={`${btnBase} border-slate-300 bg-white text-slate-600 hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed`}
      >
        ← Anterior
      </button>
      {pages.map((p, i) =>
        p === "…" ? (
          <span key={`gap-${i}`} className="px-2 text-slate-400">
            …
          </span>
        ) : (
          <button
            key={p}
            type="button"
            onClick={() => onChange(p)}
            aria-current={p === page ? "page" : undefined}
            className={`${btnBase} ${
              p === page
                ? "bg-brand-600 text-white border-brand-600"
                : "bg-white text-slate-700 border-slate-300 hover:bg-slate-50"
            }`}
          >
            {p}
          </button>
        )
      )}
      <button
        type="button"
        onClick={() => onChange(page + 1)}
        disabled={page === totalPages}
        className={`${btnBase} border-slate-300 bg-white text-slate-600 hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed`}
      >
        Următor →
      </button>
    </nav>
  );
}

function buildPageList(current, total) {
  if (total <= 7) {
    return Array.from({ length: total }, (_, i) => i + 1);
  }
  const pages = new Set([1, total, current, current - 1, current + 1]);
  const sorted = [...pages].filter((p) => p >= 1 && p <= total).sort((a, b) => a - b);
  const result = [];
  for (let i = 0; i < sorted.length; i++) {
    result.push(sorted[i]);
    if (i < sorted.length - 1 && sorted[i + 1] - sorted[i] > 1) {
      result.push("…");
    }
  }
  return result;
}

