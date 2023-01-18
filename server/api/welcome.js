// when user returns after oauth
export default defineEventHandler(async (event) => {
    const cookies = parseCookies(event)
    const a = await fetch('https://xz.ax/me', {headers: {
        //'Content-Type': 'application/json'
        // 'Content-Type': 'application/x-www-form-urlencoded',
      }, credentials: 'include'}).then((response) => response.json())
    //return cookies
    return {hi: "welcome lol"}
    if (a.error){
      return sendRedirect(event, '/login/error')
    }
    else {
      setCookie(event, "SSID", "true", {httpOnly:true, sameSite: 'strict'})

      return sendRedirect(event, '/app')
    }
    
  })