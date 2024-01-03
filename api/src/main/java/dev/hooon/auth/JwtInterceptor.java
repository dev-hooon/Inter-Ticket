package dev.hooon.auth;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		IOException {
		String token = request.getHeader("Authorization");

		// if (token != null && jwtUtil.validateToken(token)) {
		// 	// 토큰 검증 로직
		// 	return true;
		// } else {
		// 	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		// 	return false;
		// }

		return true;
	}
}
