import { Link } from "react-router-dom";
import {
  HeartIcon,
  DropletIcon,
  SmileIcon,
  BrainIcon,
  BoneIcon,
  EyeIcon,
  CalendarIcon,
  ClockIcon,
  UserIcon,
  ZapIcon,
} from "../components/Icons";

const specialties = [
  { name: "Cardiologie", Icon: HeartIcon, desc: "Sănătatea inimii și a vaselor" },
  { name: "Dermatologie", Icon: DropletIcon, desc: "Îngrijirea pielii" },
  { name: "Pediatrie", Icon: SmileIcon, desc: "Sănătatea copiilor" },
  { name: "Neurologie", Icon: BrainIcon, desc: "Sistemul nervos" },
  { name: "Ortopedie", Icon: BoneIcon, desc: "Oase și articulații" },
  { name: "Oftalmologie", Icon: EyeIcon, desc: "Sănătatea ochilor" },
];

const features = [
  {
    Icon: CalendarIcon,
    title: "Calendar în timp real",
    desc: "Vezi imediat orele disponibile, fără a mai suna la recepție.",
  },
  {
    Icon: ClockIcon,
    title: "Programări 24/7",
    desc: "Rezervi consultații oricând, independent de programul clinicii.",
  },
  {
    Icon: UserIcon,
    title: "Profil unic",
    desc: "Datele tale se salvează o singură dată și sunt reutilizate.",
  },
  {
    Icon: ZapIcon,
    title: "Anulare rapidă",
    desc: "Modifici sau anulezi programarea cu un singur click.",
  },
];

export default function HomePage() {
  return (
    <div>
      <section className="bg-gradient-to-br from-brand-50 via-white to-brand-100">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-24 text-center">
          <span className="inline-block bg-brand-100 text-brand-800 text-xs font-semibold px-3 py-1 rounded-full">
            Programări online · MediCare Clinic
          </span>
          <h1 className="mt-4 text-4xl sm:text-5xl font-extrabold text-slate-900 leading-tight">
            Sănătatea ta, la doar{" "}
            <span className="text-brand-600">câteva click-uri</span>{" "}
            distanță.
          </h1>
          <p className="mt-5 text-lg text-slate-600 max-w-2xl mx-auto">
            Explorează specializări medicale, alege medicul potrivit și
            rezervă o consultație în câteva secunde. Fără telefoane, fără
            așteptare.
          </p>
          <div className="mt-8 flex flex-wrap gap-3 justify-center">
            <Link
              to="/book"
              className="px-6 py-3 rounded-lg bg-brand-600 text-white font-semibold hover:bg-brand-700 transition"
            >
              Programează-te acum
            </Link>
            <Link
              to="/doctors"
              className="px-6 py-3 rounded-lg bg-white border border-slate-300 text-slate-700 font-semibold hover:bg-slate-50 transition"
            >
              Vezi medicii
            </Link>
          </div>
        </div>
      </section>

      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <h2 className="text-3xl font-bold text-slate-900 text-center">
          Specializări medicale
        </h2>
        <p className="text-center text-slate-600 mt-2">
          Alege specializarea de care ai nevoie
        </p>
        <div className="mt-10 grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {specialties.map(({ name, Icon, desc }) => (
            <Link
              key={name}
              to={`/doctors?specializare=${encodeURIComponent(name)}`}
              className="group p-6 bg-white rounded-xl border border-slate-200 hover:border-brand-400 hover:shadow-md transition"
            >
              <div className="w-11 h-11 rounded-lg bg-brand-50 text-brand-600 grid place-items-center group-hover:bg-brand-100">
                <Icon className="w-6 h-6" />
              </div>
              <h3 className="mt-4 font-semibold text-lg text-slate-900 group-hover:text-brand-700">
                {name}
              </h3>
              <p className="text-slate-500 text-sm mt-1">{desc}</p>
            </Link>
          ))}
        </div>
      </section>

      <section className="bg-white border-y border-slate-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
          <h2 className="text-3xl font-bold text-slate-900 text-center">
            De ce MediCare?
          </h2>
          <div className="mt-10 grid sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {features.map(({ Icon, title, desc }) => (
              <div
                key={title}
                className="p-6 rounded-xl bg-brand-50 border border-brand-100"
              >
                <div className="w-10 h-10 rounded-lg bg-white text-brand-600 grid place-items-center border border-brand-100">
                  <Icon className="w-5 h-5" />
                </div>
                <h3 className="mt-4 font-semibold text-slate-900">{title}</h3>
                <p className="text-slate-600 text-sm mt-2">{desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
