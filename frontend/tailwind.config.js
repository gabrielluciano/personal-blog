/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      fontSize: {
        'mini': '0.625rem',
      },
      colors: {
        blue: {
          app: '#304ffe',
        },
      },
      gridTemplateColumns: {
        'postlist-md': '170px 1fr',
        'postlist-sm': '130px 1fr',
      },
    },
  },
  darkMode: 'class',
  plugins: [require('@tailwindcss/typography')],
};
