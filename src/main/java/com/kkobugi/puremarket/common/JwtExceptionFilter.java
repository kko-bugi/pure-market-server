package com.kkobugi.puremarket.common;

import com.kkobugi.puremarket.common.enums.BaseResponseStatus;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.*;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            String message = e.getMessage();
            if (INVALID_JWT_SIGNATURE.getMessage().equals(message)){
                setResponse(response, INVALID_JWT_SIGNATURE);
            } else if (EMPTY_JWT_CLAIM.getMessage().equals(message)){
                setResponse(response, EMPTY_JWT_CLAIM);
            } else if (EXPIRED_ACCESS_TOKEN.getMessage().equals(message)){
                setResponse(response, EXPIRED_ACCESS_TOKEN);
            } else if (INVALID_ACCESS_TOKEN.getMessage().equals(message)){
                setResponse(response, INVALID_ACCESS_TOKEN);
            } else if (UNSUPPORTED_JWT_TOKEN.getMessage().equals(message)){
                setResponse(response, UNSUPPORTED_JWT_TOKEN);
            } else {
                setResponse(response, ACCESS_DENIED);
            }
        }
    }

    private void setResponse(HttpServletResponse response, BaseResponseStatus responseStatus) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", responseStatus.getMessage());
        responseJson.put("code", responseStatus.getCode());

        response.getWriter().print(responseJson);
    }
}
