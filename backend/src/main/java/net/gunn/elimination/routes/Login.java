package net.gunn.elimination.routes;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/login")
public class Login {

    @GetMapping({"/", ""})
    public void login
        (
            HttpServletRequest request,
            HttpServletResponse response,
            @AuthenticationPrincipal AuthenticatedPrincipal user,
            @RequestParam(value = "redirect_url", defaultValue = "/") String redirectUrl
        ) throws IOException {
        request.getSession().setAttribute("redirect_url", redirectUrl);
        if (user != null) {
            response.sendRedirect(request.getContextPath() + "/login/success");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/oauth2/authorization/google");
    }

    @GetMapping("/success")
    public void success(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var redirectUrl = request.getSession().getAttribute("redirect_url");
        if (redirectUrl != null)
            response.sendRedirect((String)redirectUrl);
        else
            response.sendRedirect(request.getContextPath());
    }
}
