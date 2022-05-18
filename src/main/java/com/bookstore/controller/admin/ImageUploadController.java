package com.bookstore.controller.admin;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/images")
public class ImageUploadController {

    private static final String PATH_IMAGE_BOOK = "./src/main/resources/static/uploads/books";
    private static final String PATH_IMAGE_ICON = "./src/main/resources/static/uploads/icons";
    private static final String PATH_IMAGE_BANNER = "./src/main/resources/static/uploads/banners";
    private static final String PATH_IMAGE_AUTHOR = "./src/main/resources/static/uploads/authors";

    @GetMapping("/book/{fileImage}")
    public ResponseEntity<ByteArrayResource> getImageBook(@PathVariable("fileImage") String fileImage) {
        if (!fileImage.equals("") || fileImage != null) {
            try {
                Path fileName = Paths.get(PATH_IMAGE_BOOK, fileImage);
                byte[] buffer = Files.readAllBytes(fileName);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/icon/{fileImage}")
    public ResponseEntity<ByteArrayResource> getImageIcon(@PathVariable("fileImage") String fileImage) {
        if (!fileImage.equals("") || fileImage != null) {
            try {
                Path fileName = Paths.get(PATH_IMAGE_ICON, fileImage);
                byte[] buffer = Files.readAllBytes(fileName);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/banner/{fileImage}")
    public ResponseEntity<ByteArrayResource> getImageBanner(@PathVariable("fileImage") String fileImage) {
        if (!fileImage.equals("") || fileImage != null) {
            try {
                Path fileName = Paths.get(PATH_IMAGE_BANNER, fileImage);
                byte[] buffer = Files.readAllBytes(fileName);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/author/{fileImage}")
    public ResponseEntity<ByteArrayResource> getImageAuthor(@PathVariable("fileImage") String fileImage) {
        if (!fileImage.equals("") || fileImage != null) {
            try {
                Path fileName = Paths.get(PATH_IMAGE_AUTHOR, fileImage);
                byte[] buffer = Files.readAllBytes(fileName);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
