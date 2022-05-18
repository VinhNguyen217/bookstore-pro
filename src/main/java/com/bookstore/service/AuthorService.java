package com.bookstore.service;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private static final String PATH_IMAGE_AUTHOR = "./src/main/resources/static/uploads/authors";

    //Quy định mỗi trang có 24 tác giả
    private static Integer PAGE_SIZE = 24;

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    public List<Author> getAllDescId() {
        return authorRepository.getAllDescId();
    }

    public List<Author> getAllBySorted() {
        return authorRepository.getAllBySorted();
    }

    public Author getById(Integer id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if (authorOptional.isPresent())
            return authorRepository.findById(id).get();
        else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    public void createNoImage(Author author) {
        author.setPhoto("author.png");
        authorRepository.save(author);
    }

    public void createWithImage(Author author, MultipartFile fileImage) throws IOException {
        String fileName = StringUtils.cleanPath(fileImage.getOriginalFilename());
        Path uploadPath = Paths.get(PATH_IMAGE_AUTHOR);
        try (InputStream inputStream = fileImage.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save upload file: " + fileName);
        }
        author.setPhoto(fileName);
        authorRepository.save(author);
    }

    public void update(Author author, MultipartFile fileImage) throws IOException {
        Optional<Author> authorOptional = authorRepository.findById(author.getId());
        if (authorOptional.isPresent()) {
            Author authorUpdate = authorOptional.get();
            if (!fileImage.isEmpty()) {
                String fileName = StringUtils.cleanPath(fileImage.getOriginalFilename());
                Path uploadPath = Paths.get(PATH_IMAGE_AUTHOR);
                try (InputStream inputStream = fileImage.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new IOException("Could not save upload file: " + fileName);
                }
                authorUpdate.setPhoto(fileName);
            }
            authorUpdate.setName(author.getName());
            authorUpdate.setDescription(author.getDescription());
            authorRepository.save(authorUpdate);
        }
    }

    public void delete(Integer id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if (authorOptional.isPresent())
            authorRepository.deleteById(id);
        else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    public List<Author> findAllAndPaging(Integer page) {
        if (page < 0) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.PAGE_ERROR);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return authorRepository.findAllAndPaging(pageable);
    }

}
