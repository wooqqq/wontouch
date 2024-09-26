/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        bitbit: ["DNFBitBitv2"],
        galmuri11: ["Galmuri11"],
        galmuri11bold: ["Galmuri11-Bold"],
        galmuri14: ["Galmuri14"],
      },
    },
  },
  plugins: [],
};
