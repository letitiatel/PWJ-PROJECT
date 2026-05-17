export const PASSWORD_RULES = [
  {
    key: "length",
    label: "Minim 8 caractere",
    test: (p) => p.length >= 8,
  },
  {
    key: "uppercase",
    label: "Cel puțin o literă mare (A-Z)",
    test: (p) => /[A-Z]/.test(p),
  },
  {
    key: "lowercase",
    label: "Cel puțin o literă mică (a-z)",
    test: (p) => /[a-z]/.test(p),
  },
  {
    key: "digit",
    label: "Cel puțin o cifră (0-9)",
    test: (p) => /\d/.test(p),
  },
  {
    key: "special",
    label: "Cel puțin un caracter special (!@#$%^&*…)",
    test: (p) => /[^A-Za-z0-9]/.test(p),
  },
];

export function checkPasswordRules(password = "") {
  const results = PASSWORD_RULES.map((r) => ({
    key: r.key,
    label: r.label,
    valid: r.test(password),
  }));
  return {
    results,
    allValid: results.every((r) => r.valid),
  };
}
