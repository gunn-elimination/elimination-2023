const autumn = require("daisyui/src/colors/themes")["[data-theme=autumn]"]

module.exports = {
  plugins: [require("@tailwindcss/typography"), require('daisyui')],
  daisyui: {
    styled: true,
    themes: [{
      autumn: {
        ...autumn,
        primary: autumn.accent,
        "primary-focus": autumn["accent-focus"],
        "primary-content": autumn["accent-content"],
        accent: autumn.primary,
        "accent-focus": autumn["primary-focus"],
        "accent-content": autumn["primary-content"]
      }
    }, "coffee"],
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
      'serif': [//'Antonio', 
        'ui-serif', 'Georgia'],
      'mono': ['ui-monospace', 'SFMono-Regular'],
      'display': ['"Seymour One"'],
      'body': ['Oxygen'],
    }
  }
};
