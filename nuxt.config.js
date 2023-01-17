// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    modules: [
        '@nuxtjs/tailwindcss',
        '@nuxt-alt/auth',
        '@pinia/nuxt'
    ],
    auth: {
        //globalMiddleware: true,
        strategies: {
            cookie: {
                cookie: {
                    server: true,
                    name: "SESSION",
                    domain: "xz.ax",
                    httpOnly: true
                },
                endpoints: {
                    login: { 
                        url: 'https://xz.ax/login', 
                        method: 'get' 
                    },
                    user: { 
                        
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
