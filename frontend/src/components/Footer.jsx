import { Link } from "react-router-dom";
import { MapPinIcon, PhoneIcon, MailIcon, ClockIcon } from "./Icons";

const specialties = [
  "Cardiologie",
  "Dermatologie",
  "Pediatrie",
  "Neurologie",
  "Ortopedie",
  "Oftalmologie",
];

export default function Footer() {
  return (
    <footer className="bg-white border-t border-slate-200 mt-auto">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-8">
          <div className="col-span-2 md:col-span-1">
            <Link to="/" className="flex items-center gap-2">
              <div className="w-9 h-9 rounded-xl bg-brand-600 text-white grid place-items-center font-bold">
                M+
              </div>
              <div className="flex flex-col leading-tight">
                <span className="font-extrabold text-slate-900">MediCare</span>
                <span className="text-xs text-slate-500 -mt-0.5">Clinic</span>
              </div>
            </Link>
            <p className="mt-4 text-sm text-slate-500 max-w-xs">
              Programări online cu medici verificați. Calendarul tău de sănătate,
              într-un singur loc.
            </p>
          </div>

          <FooterColumn title="Navigare">
            <FooterLink to="/">Acasă</FooterLink>
            <FooterLink to="/doctors">Medici</FooterLink>
            <FooterLink to="/book">Programări</FooterLink>
            <FooterLink to="/profile">Contul meu</FooterLink>
            <FooterLink to="/login">Autentificare</FooterLink>
          </FooterColumn>

          <FooterColumn title="Specializări">
            {specialties.map((s) => (
              <FooterLink
                key={s}
                to={`/doctors?specializare=${encodeURIComponent(s)}`}
              >
                {s}
              </FooterLink>
            ))}
          </FooterColumn>

          <FooterColumn title="Contact">
            <ContactItem icon={MapPinIcon}>
              Str. Sănătății nr. 12, București
            </ContactItem>
            <ContactItem icon={PhoneIcon}>021 123 45 67</ContactItem>
            <ContactItem icon={MailIcon}>contact@medicare.ro</ContactItem>
            <ContactItem icon={ClockIcon}>
              Luni–Vineri, 08:00–20:00
            </ContactItem>
          </FooterColumn>
        </div>

        <div className="mt-10 pt-6 border-t border-slate-200 flex flex-col sm:flex-row items-center justify-between gap-2 text-sm text-slate-500">
          <span>© {new Date().getFullYear()} MediCare Clinic. Toate drepturile rezervate.</span>
          <span>
            Programări online · Medici verificați · Suport non-stop
          </span>
        </div>
      </div>
    </footer>
  );
}

function FooterColumn({ title, children }) {
  return (
    <div>
      <h3 className="font-semibold text-slate-900 text-sm uppercase tracking-wide">
        {title}
      </h3>
      <ul className="mt-4 space-y-2">{children}</ul>
    </div>
  );
}

function FooterLink({ to, children }) {
  return (
    <li>
      <Link
        to={to}
        className="text-sm text-slate-600 hover:text-brand-700 transition"
      >
        {children}
      </Link>
    </li>
  );
}

function ContactItem({ icon: Icon, children }) {
  return (
    <li className="text-sm text-slate-600 flex items-start gap-2">
      <Icon className="w-4 h-4 mt-0.5 text-slate-400 shrink-0" />
      <span>{children}</span>
    </li>
  );
}
