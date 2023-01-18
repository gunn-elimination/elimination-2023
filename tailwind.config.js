const autumn = require("daisyui/src/colors/themes")["[data-theme=autumn]"]
let coffee = require("daisyui/src/colors/themes")["[data-theme=coffee]"]
delete coffee['base-content']

module.exports = {
  plugins: [require("@tailwindcss/typography"), require('daisyui')],
  daisyui: {
    styled: true,
    themes: [{
      autumn: {
        ...autumn,
        primary: autumn.accent,
        accent: autumn.primary,
      }
    }, {
      coffee: {
        ...coffee,
      }
    }],
    base: true,
    utils: true,
    logs: true,
    rtl: false,
    prefix: "",
    darkTheme: "coffee",
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
