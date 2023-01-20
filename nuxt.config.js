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
            description: 'Kill or be killed. It\'s Gunn Elimination 2023!',
            nativeUI: true
        },
        manifest: {
            name: 'Gunn Elimination',
            short_name: 'Elimination',
            description: 'Kill or be killed. It\'s Gunn Elimination 2023!',
            start_url: '/app'
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
                        url: 'https://xz.ax/login', 
                        method: 'get' 
                    },
                    user: { 
                        includeCredentials: true,
                        url: 'https://xz.ax/me', 
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
    }
})
