package net.gunn.elimination.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.gunn.elimination.auth.BannedUserException;
import net.gunn.elimination.auth.RegistrationClosedException;
import net.gunn.elimination.exceptions.NonPAUSDUserException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class Error implements ErrorController, AccessDeniedHandler {
	private final ObjectMapper mapper;

	public Error(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@RequestMapping("/error")
	public Object handleError(HttpServletRequest request) {
		var code = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		code = code == null ? 500 : code;
		var t = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

		String errorCode = "Unknown Error";
		if (t instanceof Throwable throwable) {
			if (throwable.getMessage() != null)
				errorCode = throwable.getMessage();
			else
				errorCode = throwable.getClass().getSimpleName();
		} else if (code instanceof Integer i && i < 600) {
			var status = HttpStatus.resolve(i);
			if (status != null)
				errorCode = status.getReasonPhrase();
		}


		return Map.of(
			"error",
			Map.of(
				"code", code,
				"message", errorCode
			)
		);
	}

	@ExceptionHandler(NonPAUSDUserException.class)
	public Object handleNonpausd(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		return handleError(request);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.sendError(
			HttpServletResponse.SC_FORBIDDEN,
			mapper.writeValueAsString(Map.of("error", Map.of("code", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE), "message", accessDeniedException.getMessage())))
		);
	}

	@ExceptionHandler(RegistrationClosedException.class)
	public void handleRegistrationClosed(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.sendRedirect("/error/signups-closed");
	}

	@ExceptionHandler(BannedUserException.class)
	public void handleBannedUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.sendRedirect("/error/banned");
	}
}
