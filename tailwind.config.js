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
          accent: autumn.secondary
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
