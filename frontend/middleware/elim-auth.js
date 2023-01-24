export default defineNuxtRouteMiddleware(async (to, from) => {
    if (process.server) return;
    const a = await fetch('/api/me', {
        headers: {
            // 'Content-Type': 'application/json'
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        credentials: 'include'
    }).then((response) => response.json())
    //console.log(a)
    if (a.error) return navigateTo('/login');
    //return navigateTo('/')
})
