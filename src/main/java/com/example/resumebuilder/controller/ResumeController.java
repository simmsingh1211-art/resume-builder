package com.example.resumebuilder.controller;

import com.example.resumebuilder.model.Resume;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ResumeController {

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("resume", new Resume());
        return "form";
    }

    @PostMapping("/submit")
    public String submitForm(@ModelAttribute Resume resume, Model model) {
        model.addAttribute("resume", resume);
        return "result";
    }
}
