/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        brand: {
          50: "#eff8ff",
          100: "#dbedfe",
          200: "#bfe0fe",
          300: "#93cdfd",
          400: "#60b1fa",
          500: "#3b91f6",
          600: "#2574eb",
          700: "#1d5fd8",
          800: "#1f4eaf",
          900: "#1f448a",
        },
      },
      fontFamily: {
        sans: ["Inter", "system-ui", "sans-serif"],
      },
    },
  },
  plugins: [],
};
