package com.example.backend.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@CrossOrigin(origins = "http://localhost:3030", maxAge = 3600)
@Controller
public class auth {
    @PostMapping("/api/auth/user/login")
    public ResponseEntity<String> login(JSONPObject json){
        //String username = json.getValue("username").toString();
        //String password = json.getValue("password").toString();
        return ResponseEntity.status(200).body("000");
    }

}
