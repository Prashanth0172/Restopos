package com.hyva.restopos;

import com.hyva.restopos.rest.Hiposservice.BasicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    BasicDataService basicDataService;

    @RequestMapping("/home")
    public String home(@CookieValue(value = "accessToken", required = false) String accessToken, Model model)  {
        if (accessToken != null) {
            model.addAttribute("pageName", "login");
        } else {
            model.addAttribute("pageName", "dashboard");
        }
        basicDataService.pushData();
        return "index";
    }
}
