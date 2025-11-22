package br.com.fiap.skillup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Landing pública
    @GetMapping({"/", "/home-public"})
    public String homePublic() {
        return "home-public";
    }
}