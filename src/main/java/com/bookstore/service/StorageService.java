package com.bookstore.service;

import com.bookstore.response.ResponseMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    public ResponseEntity<ByteArrayResource> getImage(String fileImage, String path) {
        if (!fileImage.equals("") || fileImage != null) {
            try {
                Path fileName = Paths.get(path, fileImage);
                byte[] buffer = Files.readAllBytes(fileName);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (NoSuchFileException e) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.IMAGE_NOT_FOUND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    public void deleteImage(String fileImage, String path) throws IOException {
        String fileName = path.concat("/").concat(fileImage);
        Files.delete(Paths.get(fileName));
    }
}
