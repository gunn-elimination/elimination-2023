module.exports = {
  plugins: [require("@tailwindcss/typography"), require('daisyui')],
  daisyui: {
    styled: true,
    themes: ["autumn", "coffee"],
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
