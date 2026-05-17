import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const linkBase =
    "px-3 py-2 rounded-md text-sm font-medium transition-colors";
  const linkClass = ({ isActive }) =>
    `${linkBase} ${
      isActive
        ? "text-brand-700 bg-brand-50"
        : "text-slate-600 hover:text-brand-700 hover:bg-brand-50"
    }`;

  return (
    <header className="bg-white border-b border-slate-200 sticky top-0 z-30">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex h-16 items-center justify-between">
        <Link to="/" className="flex items-center gap-2">
          <div className="w-9 h-9 rounded-xl bg-brand-600 text-white grid place-items-center font-bold">
            M+
          </div>
          <div className="flex flex-col leading-tight">
            <span className="font-extrabold text-slate-900">MediCare</span>
            <span className="text-xs text-slate-500 -mt-0.5">Clinic</span>
          </div>
        </Link>

        <nav className="flex items-center gap-1">
          <NavLink to="/" end className={linkClass}>
            Acasă
          </NavLink>
          <NavLink to="/doctors" className={linkClass}>
            Medici
          </NavLink>
          <NavLink to="/book" className={linkClass}>
            Programări
          </NavLink>
        </nav>

        <div className="flex items-center gap-2">
          {user ? (
            <>
              <NavLink
                to="/profile"
                className={({ isActive }) =>
                  `hidden sm:inline-flex items-center gap-2 px-3 py-1.5 text-sm rounded-md ${
                    isActive
                      ? "bg-brand-50 text-brand-700"
                      : "text-slate-600 hover:bg-slate-50"
                  }`
                }
              >
                <span className="w-7 h-7 rounded-full bg-brand-100 text-brand-700 grid place-items-center text-xs font-bold">
                  {(user.prenume?.[0] || user.email?.[0] || "U").toUpperCase()}
                </span>
                Contul meu
              </NavLink>
              <button
                onClick={handleLogout}
                className="px-3 py-1.5 text-sm rounded-md border border-slate-300 hover:bg-slate-50"
              >
                Ieșire
              </button>
            </>
          ) : (
            <Link
              to="/login"
              className="px-3 py-1.5 text-sm rounded-md bg-brand-600 text-white hover:bg-brand-700"
            >
              Autentificare
            </Link>
          )}
        </div>
      </div>
    </header>
  );
}
