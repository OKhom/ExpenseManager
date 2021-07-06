package com.okdev.ems.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

//    @GetMapping("/")
//    public String mainIndex() {
//        return "Hello, Oleh!\nWelcome to Expense Manager!";
//    }

    @GetMapping("/admin/get")
    public String getAdmin() {
        return "Hello, Admin!";
    }

    @GetMapping("/user/get")
    public String getUser() {
        return "Hello, User!";
    }
}
