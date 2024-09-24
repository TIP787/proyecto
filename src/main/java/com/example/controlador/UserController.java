package com.example.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Paz.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public String addUser(@RequestParam String name,
                          @RequestParam String email,
                          @RequestParam String password,
                          @RequestParam String number,
                          @RequestParam String cityCode,
                          @RequestParam String countryCode) {
        userService.addUser(name, email, password, number, cityCode, countryCode);
        return "User added successfully";
    }
}