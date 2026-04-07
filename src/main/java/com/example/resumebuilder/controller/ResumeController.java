package com.example.resumebuilder.controller;

import com.example.resumebuilder.model.Resume;
import jakarta.servlet.http.HttpServletResponse;
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
    public String submitForm(@ModelAttribute Resume resume,
                             @RequestParam("image") org.springframework.web.multipart.MultipartFile file,
                             Model model) throws Exception {

        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();

            java.nio.file.Path path =
                    java.nio.file.Paths.get("src/main/resources/static/" + fileName);

            java.nio.file.Files.write(path, file.getBytes());

            resume.setImage(fileName);
        }

        model.addAttribute("resume", resume);
        return "result";
    }

    @GetMapping("/download")
    public void downloadPdf(HttpServletResponse response,
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
