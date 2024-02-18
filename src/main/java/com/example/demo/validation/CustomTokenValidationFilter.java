package com.example.demo.validation;

import java.io.IOException;
import java.security.Key;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.rest.ApiService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class CustomTokenValidationFilter extends OncePerRequestFilter {
	
	@Autowired
	ApiService apiService;

    private static final String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;
    
    // URLs to skip token validation
    private static final String[] URLS_TO_SKIP_VALIDATION = {"/swagger-ui.html","*.js", "*.png"};
    
    // Laravel Passport public key
    private static final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "YOUR_PUBLIC_KEY_HERE\n" +
            "-----END PUBLIC KEY-----";



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (shouldSkipValidation(request)){
        	filterChain.doFilter(request, response);
        }else if (isValidAuthorizationHeader(authorizationHeader)) {
            filterChain.doFilter(request, response);
        } else {
            sendError(response, "Invalid or missing Authorization header");
        }
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) throws IOException {
         if (authorizationHeader != null){
        	 //return validateToken(authorizationHeader);
        	 return apiService.callApi(authorizationHeader);
         }
         return false;
    }

    private void sendError(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(errorMessage);
    }
    
    private boolean shouldSkipValidation(HttpServletRequest request) {
        // Check if the request URL matches any of the URLs to skip validation
        for (String url : URLS_TO_SKIP_VALIDATION) {
            if (request.getRequestURI().startsWith(url)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean validateToken(String authorizationHeader) {
        // Extract token from Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        try {
            // Decode and verify the token
            Key key = Keys.hmacShaKeyFor(PUBLIC_KEY.getBytes());
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            // Token is valid, perform additional validation if needed

            // Return success response
            return true;
        } catch (Exception e) {
            // Token validation failed, handle error
            return false;
        }
    }
}
