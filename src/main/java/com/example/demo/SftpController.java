package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
public class SftpController {

    private final SftpService sftpService;

    @Autowired
    public SftpController(SftpService sftpService) {
        this.sftpService = sftpService;
    }

    @RequestMapping("upload")
    public String upload() throws Exception {

        String filename = "vw_troc.jpg";

        ClassPathResource resource = new ClassPathResource(filename);
        InputStream inputStream = resource.getInputStream();

        sftpService.uploadFile(filename, inputStream);
        return "File uploaded";
    }
}
