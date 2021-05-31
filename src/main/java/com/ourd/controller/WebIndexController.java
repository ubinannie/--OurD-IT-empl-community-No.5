package com.ourd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebIndexController {
    @GetMapping({"","/index"})
    public String index() {
        return "redirect:/chat/room";
    }
}