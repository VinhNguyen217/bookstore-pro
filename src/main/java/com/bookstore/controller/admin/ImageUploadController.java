package com.bookstore.controller.admin;

import com.bookstore.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.bookstore.utils.CommonStringUtil.*;

@Controller
@RequestMapping("/images")
public class ImageUploadController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/book/{fileImage}")
    public ResponseEntity<ByteArrayResource> getImageBook(@PathVariable("fileImage") String fileImage) {
        return storageService.getImage(fileImage, PATH_IMAGE_BOOK);
    }

    @GetMapping("/icon/{fileImage}")
    public ResponseEntity<ByteArrayResource> getImageIcon(@PathVariable("fileImage") String fileImage) {
        return storageService.getImage(fileImage, PATH_IMAGE_ICON);
    }

    @GetMapping("/banner/{fileImage}")
    public ResponseEntity<ByteArrayResource> getImageBanner(@PathVariable("fileImage") String fileImage) {
        return storageService.getImage(fileImage, PATH_IMAGE_BANNER);
    }

    @GetMapping("/author/{fileImage}")
    public ResponseEntity<ByteArrayResource> getImageAuthor(@PathVariable("fileImage") String fileImage) {
        return storageService.getImage(fileImage, PATH_IMAGE_AUTHOR);
    }
}
