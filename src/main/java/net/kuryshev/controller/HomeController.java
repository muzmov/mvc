package net.kuryshev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/one")
public class HomeController {

    @RequestMapping(value = "/test", method = GET)
    public String home() {
        return "home";
    }
}
