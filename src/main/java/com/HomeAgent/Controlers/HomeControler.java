package com.HomeAgent.Controlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller()
public class HomeControler {
    @GetMapping("/home")
  public  String homepage(){
        return "Home";
    }

    @GetMapping("/index")
  public  String home(){
        return "index";
    }

    @GetMapping("/inventory")
    public String inventory() {
        return "inventory";   // loads inventory.html from templates
    }

    @GetMapping("/documents")
    public String documents() {
        return "documents";
    }

//    @GetMapping("/reminders")
//    public String reminders() {
//        return "reminders";
//    }



    @GetMapping("/analytics")
    public String analytics() {
        return "analytics";
    }

    @GetMapping("/ai")
    public String ai() {
        return "ai";
    }
    @GetMapping("/login")
    public String login(){
        return "Login";
    }
    @GetMapping("/signUp")
    public String signUp(){
        return "SignUp";
    }

//    @GetMapping("/settings")
//    public String settings() {
//        return "settings";
//    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }


}
