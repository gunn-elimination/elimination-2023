export default defineNuxtRouteMiddleware(async (to, from) => {
    if (process.server) return;
    const a = await fetch('https://xz.ax/me', {
        credentials: 'include'
    }).then((response) => response.json());
    console.log(a);
    if (a.error) return navigateTo('/login');
    //return navigateTo('/')
    return;
});
