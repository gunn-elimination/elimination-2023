// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    modules: [
        '@nuxtjs/tailwindcss',
        '@pinia/nuxt',
        '@kevinmarrec/nuxt-pwa'
    ],
    pwa: {
        meta: {
            name: 'Gunn Elimination',
            description: 'Eliminate or be eliminated. It\'s Gunn Elimination 2023!',
            nativeUI: true
        },
        manifest: {
            name: 'Gunn Elimination',
            short_name: 'Elimination',
            description: 'Eliminate or be eliminated. It\'s Gunn Elimination 2023!',
            start_url: '/app'
        }
    },
    runtimeConfig: {
        public: {
            apiUrl: process.env.NODE_ENV !== 'production' ? 'https://elimination.gunn.one/api' : '/api',
            appUrl: process.env.NODE_ENV !== 'production' ? 'http://localhost:3000' : '',
        }
    },
    experimental: {
        treeshakeClientOnly: false
    }
})
