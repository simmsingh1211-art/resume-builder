package com.example.resumebuilder.controller;

import com.example.resumebuilder.model.Resume;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ResumeController {

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("resume", new Resume());
        return "form";
    }

    @PostMapping("/submit")
    public String submitForm(
            @RequestParam String name,
            @RequestParam String education,
            @RequestParam String skills,
            @RequestParam String experience,
            @RequestParam(value = "image", required = false) MultipartFile file,
            Model model) throws Exception {

        Resume resume = new Resume();
        resume.setName(name);
        resume.setEducation(education);
        resume.setSkills(skills);
        resume.setExperience(experience);

        if (file != null && !file.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());

            resume.setImage(fileName);
        }

        model.addAttribute("resume", resume);
        return "result";
    }

    @GetMapping("/download")
    public void downloadPdf(
            HttpServletResponse response,
            @RequestParam String name,
            @RequestParam String education,
            @RequestParam String skills,
            @RequestParam String experience) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=resume.pdf");

        com.itextpdf.kernel.pdf.PdfWriter writer =
                new com.itextpdf.kernel.pdf.PdfWriter(response.getOutputStream());

        com.itextpdf.kernel.pdf.PdfDocument pdf =
                new com.itextpdf.kernel.pdf.PdfDocument(writer);

        com.itextpdf.layout.Document document =
                new com.itextpdf.layout.Document(pdf);

        document.add(new com.itextpdf.layout.element.Paragraph("Resume"));
        document.add(new com.itextpdf.layout.element.Paragraph("Name: " + name));
        document.add(new com.itextpdf.layout.element.Paragraph("Education: " + education));
        document.add(new com.itextpdf.layout.element.Paragraph("Skills: " + skills));
        document.add(new com.itextpdf.layout.element.Paragraph("Experience: " + experience));

        document.close();
    }
}