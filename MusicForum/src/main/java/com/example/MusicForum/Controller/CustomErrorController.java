package com.example.MusicForum.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public Object handleError(HttpServletRequest request) {
        //Status error (400, 404, 500...)
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode = (status != null) ? Integer.valueOf(status.toString()) : 500;

        ///api/v1?
        String originalUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        
        if (originalUri != null && originalUri.startsWith("/api/v1")) {
            //It is API -> JSON
            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            response.put("status", statusCode);
            response.put("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
            response.put("message", "Error detectado fuera del controlador (Posible Path Traversal o fallo de seguridad)");
            
            return new ResponseEntity<>(response, HttpStatus.valueOf(statusCode));
        }

        //Web -> error.html
        return "error"; 
    }
}
