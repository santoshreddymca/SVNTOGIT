package com.outlet.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.outlet.model.User;
import com.outlet.service.UserService;


@RestController
public class UserController {
    @Autowired
    UserService userService;

    /**
     * Handle request to the default page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<User> viewHome() {
        return userService.findAllUsers();
    }

    @GetMapping("/download2")
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {

        FileInputStream inputStream = new FileInputStream(new File("/Users/hendisantika/Documents/IdeaProjects/spring-boot-excel-csv-pdf/README.md"));

        response.setHeader("Content-Disposition", "attachment; filename=\"README.md\"");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream, outputStream);

        outputStream.close();
        inputStream.close();
    }

    @GetMapping(value = "/download3", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> downloadDocument(
            String acquistionId,
            String fileType,
            Integer expressVfId) throws IOException {
        File file = new File("/Users/hendisantika/Documents/IdeaProjects/spring-boot-excel-csv-pdf/README.md");



        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        InputStreamReader i = new InputStreamReader(new FileInputStream(file));
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        System.out.println("The length of the file is : " + file.length());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @GetMapping(value = "/download4", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> downloadPDFFile()
            throws IOException {

        ClassPathResource pdfFile = new ClassPathResource("Keyboard Shortcuts Mac.pdf");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .contentLength(pdfFile.contentLength())
                .contentType(
                        MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(pdfFile.getInputStream()));
    }

    @GetMapping("/download5")
    public StreamingResponseBody getSteamingFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"Keyboard Shortcuts Mac.pdf\"");
        InputStream inputStream = new FileInputStream(new File("/Users/hendisantika/Documents/IdeaProjects/spring-boot-excel-csv-pdf/src/main/resources/Keyboard Shortcuts Mac.pdf"));
        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                System.out.println("Writing some bytes..");
                outputStream.write(data, 0, nRead);
            }
        };
    }
}
