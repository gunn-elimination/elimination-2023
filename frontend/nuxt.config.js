// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    modules: [
        '@nuxtjs/tailwindcss',
        '@nuxt-alt/auth',
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
    auth: {
        //globalMiddleware: true,
        strategies: {
            cookie: {
                cookie: {
                    server: true,
                    name: "SSUSER",
                    httpOnly: true,
                },
                endpoints: {
                    login: { 
                        url: '/api/login', 
                        method: 'get' 
                    },
                    user: { 
                        includeCredentials: true,
                        url: '/api/me', 
                        method: 'get' 
                    }
                },
                user: {
                    property: {
                        // false: use the full output of /me for the user object
                        client: false,
                        server: false
                    }
                }
            },
        }
    },
    experimental: {
        treeshakeClientOnly: false
    }
})
