const autumn = require("daisyui/src/colors/themes")["[data-theme=autumn]"]
const business = require("daisyui/src/colors/themes")["[data-theme=business]"]

module.exports = {
  plugins: [require("@tailwindcss/typography"), require('daisyui')],
  daisyui: {
    styled: true,
    themes: [
      {
        autumn: {
          ...autumn,
          primary: '#a51618',
          secondary: '#6b7280',
          accent: autumn.secondary,
          'base-200': '#e4e4e7',
          'base-300': '#d4d4d8'
        }
      },
      {
        business: {
          ...business,
          primary: '#ff594c',
          accent: '#eb144c'
        }
      }
    ],
    base: true,
    utils: true,
    logs: true,
    rtl: false,
    prefix: "",
    darkTheme: "business",
  },
  theme: {
    container: {
      center: true,
      padding: {
        DEFAULT: '2rem',
        sm: '3rem',
        lg: '4rem',
        xl: '5rem',
        '2xl': '6rem',
      }
    }
  }
};
