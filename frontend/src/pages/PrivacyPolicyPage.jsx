import { Link } from "react-router-dom";

export default function PrivacyPolicyPage() {
  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <h1 className="text-3xl font-bold text-slate-900">
        Politica de prelucrare a datelor cu caracter personal
      </h1>
      <p className="text-slate-500 mt-2">
        Ultima actualizare: {new Date().getFullYear()} · MediCare Clinic
      </p>

      <div className="mt-8 space-y-8 text-slate-700 leading-relaxed">
        <Section title="1. Operatorul de date">
          MediCare Clinic, în calitate de operator de date cu caracter personal,
          prelucrează datele tale în conformitate cu Regulamentul (UE) 2016/679
          (GDPR) și legislația națională aplicabilă. Ne poți contacta la{" "}
          <a
            href="mailto:contact@medicare.ro"
            className="text-brand-700 hover:underline"
          >
            contact@medicare.ro
          </a>
          .
        </Section>

        <Section title="2. Ce date colectăm">
          <ul className="list-disc list-inside space-y-1">
            <li>Date de identificare: nume, prenume, CNP, data nașterii;</li>
            <li>Date de contact: e-mail, telefon, adresă;</li>
            <li>Date medicale: alergii și motivul consultației;</li>
            <li>Istoricul programărilor efectuate în aplicație.</li>
          </ul>
        </Section>

        <Section title="3. Scopul prelucrării">
          Datele sunt folosite exclusiv pentru: crearea și gestionarea contului
          tău, programarea și desfășurarea consultațiilor medicale, comunicarea
          legată de programări și îndeplinirea obligațiilor legale (de exemplu,
          evidența medicală).
        </Section>

        <Section title="4. Temeiul legal">
          Prelucrarea se bazează pe consimțământul tău (art. 6 alin. 1 lit. a
          GDPR), pe executarea serviciului medical solicitat (lit. b) și, pentru
          datele medicale, pe art. 9 alin. 2 lit. h GDPR (scopuri de medicină
          preventivă și diagnostic).
        </Section>

        <Section title="5. Securitatea datelor">
          Aplicăm măsuri tehnice și organizatorice adecvate pentru a-ți proteja
          datele împotriva accesului neautorizat, pierderii sau divulgării.
          Accesul la datele tale este restricționat exclusiv personalului
          autorizat. Nu vindem și nu transmitem datele tale către terți în
          scopuri de marketing.
        </Section>

        <Section title="6. Perioada de stocare">
          Păstrăm datele tale doar atât timp cât este necesar pentru scopurile
          de mai sus sau pentru perioada impusă de obligațiile legale. La
          încetarea acestora, datele sunt șterse sau anonimizate.
        </Section>

        <Section title="7. Drepturile tale">
          Conform GDPR, ai dreptul de acces, rectificare, ștergere („dreptul de a
          fi uitat"), restricționare, portabilitate, opoziție, precum și dreptul
          de a-ți retrage consimțământul oricând și de a depune o plângere la
          Autoritatea Națională de Supraveghere a Prelucrării Datelor cu Caracter
          Personal (ANSPDCP).
        </Section>
      </div>

      <div className="mt-10">
        <Link to="/profile" className="text-brand-700 hover:underline">
          ← Înapoi la contul meu
        </Link>
      </div>
    </div>
  );
}

function Section({ title, children }) {
  return (
    <section>
      <h2 className="text-lg font-semibold text-slate-900">{title}</h2>
      <div className="mt-2">{children}</div>
    </section>
  );
}
