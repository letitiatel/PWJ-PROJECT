// Prețuri orientative per specializare (RON). Pot fi mutate în backend ulterior.
const PRICES = {
  Cardiologie: 250,
  Dermatologie: 200,
  Pediatrie: 180,
  Neurologie: 280,
  Ortopedie: 220,
  Oftalmologie: 190,
  ORL: 170,
  Ginecologie: 230,
  "Medicină generală": 150,
};

const DEFAULT_PRICE = 200;

export function priceForSpecialty(specializare) {
  if (!specializare) return DEFAULT_PRICE;
  return PRICES[specializare] ?? DEFAULT_PRICE;
}

export function formatPrice(value) {
  return new Intl.NumberFormat("ro-RO", {
    style: "currency",
    currency: "RON",
    maximumFractionDigits: 0,
  }).format(value);
}
