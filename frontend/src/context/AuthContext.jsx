import { createContext, useContext, useEffect, useState } from "react";
import { checkPasswordRules } from "../utils/password";

const AuthContext = createContext(null);

const STORAGE_KEY = "medicare.user";
const USERS_KEY = "medicare.users";

const normalizeEmail = (email) => (email || "").trim().toLowerCase();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [ready, setReady] = useState(false);

  useEffect(() => {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (raw) setUser(JSON.parse(raw));
    setReady(true);
  }, []);

  const persist = (u) => {
    setUser(u);
    if (u) localStorage.setItem(STORAGE_KEY, JSON.stringify(u));
    else localStorage.removeItem(STORAGE_KEY);
  };

  const readUsers = () => JSON.parse(localStorage.getItem(USERS_KEY) || "[]");
  const writeUsers = (list) =>
    localStorage.setItem(USERS_KEY, JSON.stringify(list));

  const register = (data) => {
    const email = normalizeEmail(data.email);
    const parola = data.parola ?? "";

    if (!email) throw new Error("Email-ul este obligatoriu.");
    const rules = checkPasswordRules(parola);
    if (!rules.allValid) {
      throw new Error("Parola nu îndeplinește toate cerințele.");
    }

    const users = readUsers();
    if (users.some((u) => u.email === email)) {
      throw new Error("Există deja un cont cu acest email.");
    }
    const newUser = { ...data, email, parola, id: Date.now() };
    users.push(newUser);
    writeUsers(users);
    const { parola: _omit, ...safe } = newUser;
    persist(safe);
    return safe;
  };

  const login = (email, parola) => {
    const normEmail = normalizeEmail(email);
    const users = readUsers();
    const found = users.find(
      (u) => u.email === normEmail && u.parola === parola
    );
    if (!found) throw new Error("Email sau parolă incorecte.");
    const { parola: _omit, ...safe } = found;
    persist(safe);
    return safe;
  };

  const logout = () => persist(null);

  const updateProfile = (patch) => {
    if (!user) return;
    const users = readUsers();
    const idx = users.findIndex((u) => u.id === user.id);
    if (idx !== -1) {
      users[idx] = { ...users[idx], ...patch };
      writeUsers(users);
    }
    const next = { ...user, ...patch };
    persist(next);
  };

  return (
    <AuthContext.Provider
      value={{ user, ready, register, login, logout, updateProfile }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
