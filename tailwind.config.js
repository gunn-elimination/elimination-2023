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
          primary: autumn.accent,
          accent: autumn.primary,
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
    fontFamily: {
      'sans': ['ui-sans-serif', 'system-ui'],
      'serif': [/* 'Antonio', */ 'ui-serif', 'Georgia'],
      'mono': ['ui-monospace', 'SFMono-Regular'],
      'display': ['"Seymour One"'],
      'body': ['Oxygen'],
    },
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
