package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.Category;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CategoryRepository;
import com.bookstore.repository.custom.BookCustomRepository;
import com.bookstore.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    //Quy định mỗi trang có 12 sản phẩm
    private static Integer PAGE_SIZE = 8;

    private static final String PATH_IMAGE_BOOK = "./src/main/resources/static/uploads/books";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    /**
     * Trả về tất cả sách sắp xếp theo thứ tự giảm dần theo id
     *
     * @return
     */
    public List<Book> getAllDescId() {
        return bookRepository.getAllDescId();
    }

    public Book getById(Integer id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent())
            return bookOptional.get();
        else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    public void createBook(Book book, MultipartFile fileImage) throws IOException {
        String fileName = StringUtils.cleanPath(fileImage.getOriginalFilename());
        Path uploadPath = Paths.get(PATH_IMAGE_BOOK);
        try (InputStream inputStream = fileImage.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save upload file: " + fileName);
        }
        book.setPhoto(fileName);
        if (book.getSold() == null)
            book.setSold(0);
        bookRepository.save(book);
    }

    public void updateBook(Book book, MultipartFile fileImage) throws IOException {
        Optional<Book> optionalBook = bookRepository.findById(book.getId());
        if (optionalBook.isPresent()) {
            Book bookUpdate = optionalBook.get();
            if (!fileImage.isEmpty()) {
                String fileName = StringUtils.cleanPath(fileImage.getOriginalFilename());
                Path uploadPath = Paths.get(PATH_IMAGE_BOOK);
                try (InputStream inputStream = fileImage.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new IOException("Could not save upload file: " + fileName);
                }
                bookUpdate.setPhoto(fileName);
            }
            bookUpdate.setName(book.getName());
            bookUpdate.setPrice(book.getPrice());
            bookUpdate.setQuantity(book.getQuantity());
            bookUpdate.setPriceImport(book.getPriceImport());
            bookUpdate.setSold(book.getSold());
            bookUpdate.setDescription(book.getDescription());
            bookUpdate.setAuthor_id(book.getAuthor_id());
            bookUpdate.setCategory_id(book.getCategory_id());
            bookUpdate.setPublisher(book.getPublisher());
            bookUpdate.setDatePublish(book.getDatePublish());
            bookRepository.save(bookUpdate);
        }
    }

    public void delete(Integer id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent())
            bookRepository.deleteById(id);
        else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    /**
     * Trả về sách mới nhất
     *
     * @return
     */
    public List<Book> getByNew() {
        return bookRepository.findByNew();
    }

    /**
     * Trả về các quyển sách bán chạy nhất
     *
     * @return
     */
    public List<Book> getBySold() {
        return bookRepository.findBySold();
    }

    /**
     * Trả về các quyển sách được yêu thích nhất
     *
     * @return
     */
    public List<Book> getByLike() {
        return bookRepository.findByLike();
    }

    /**
     * Trả về các sản phẩm tương tự
     *
     * @param id
     * @return
     */
    public List<Book> getSimilar(Integer id) {
        Book book = bookRepository.getById(id);
        if (book != null)
            return bookRepository.findSimilar(book.getCategory_id(), book.getId());
        else
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    public List<Book> getByCategory(Integer catId) {
        return bookRepository.findByCategory(catId);
    }

    public List<Book> getByAuthor(Integer authorId) {
        return bookRepository.findByAuthor(authorId);
    }

    /**
     * Tìm và phân trang danh sách sản phẩm theo danh mục
     *
     * @param catId
     * @param page
     * @return
     */
    public List<Book> findByCategoryAndPaging(Integer catId, Integer page) {
        if(page < 0) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,ResponseMessage.PAGE_ERROR);
        Optional<Category> categoryOptional = categoryRepository.findById(catId);
        if (categoryOptional.isPresent()) {
            Pageable pageable = PageRequest.of(page, PAGE_SIZE);
            return bookRepository.findByCategoryAndPaging(catId, pageable);
        } else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.CATEGORY_NOT_FOUND);
    }

    /**
     * Tìm sách khi người dùng nhập tên sách
     *
     * @param name
     * @return
     */
    public List<Book> findByName(String name) {
        return bookRepository.findByName(name.trim());
    }

    /**
     * Hiển thị hết các sách và phân trang
     *
     * @param page
     * @return
     */
    public List<Book> findAllAndPaging(Integer page) {
        if (page < 0) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.PAGE_ERROR);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return bookRepository.findAllAndPaging(pageable);
    }
}
