package com.bookstore.service;

import com.bookstore.model.User;
import com.bookstore.model.request.PasswordUpdate;
import com.bookstore.repository.UserRepository;
import com.bookstore.response.ResponseMessage;
import com.bookstore.security.CustomUserDetails;
import com.bookstore.utils.Utility;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utility utility;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User create(User user, User.Role role) {
        String passwordEncoder = utility.encode(user.getPassword());
        user.setPassword(passwordEncoder);
        user.setRole(role);
        return userRepository.save(user);
    }

    /**
     * Tạo user
     *
     * @param user
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public User create(User user) throws MessagingException, UnsupportedEncodingException {
        User userCheck = userRepository.findByEmail(user.getEmail());
        if (userCheck != null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.EMAIL_EXIST);
        else {
            String passwordEncoder = utility.encode(user.getPassword());
            String verifyCode = RandomString.make(64);
            user.setPassword(passwordEncoder);
            user.setVerifyCode(verifyCode);
            user.setEnabled(false);
            user.setCreatedAt(new Date());
            User userCreated = userRepository.save(user);
            sendVerificationEmail(userCreated);
            return userCreated;
        }
    }

    /**
     * Gửi email xác nhân tài khoản
     *
     * @param user
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
        String subject = "Please verify your registration";
        String senderName = "BookStore";
        String mailContent = "Dear " + user.getUserName();
        mailContent += "<p>Please click the link below to verify to your registration</p>";
        String verifyURL = "http://thanh-nien-nghiem-tuc.herokuapp.com/book-store/register/verify?c=" + user.getVerifyCode();
        mailContent += "<a href=\"" + verifyURL + "\">VERIFY</a>";
        mailContent += "<p>Thank you very much !!!</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("bookstorevn217@gmail.com", senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);
        mailSender.send(message);
    }

    public boolean verify(String verify_code) {
        User userCheck = userRepository.findByVerifyCode(verify_code);
        if (userCheck == null) {
            return false;
        } else {
            userCheck.setEnabled(true);
            userCheck.setVerifiedAt(new Date());
            userRepository.save(userCheck);
            return true;
        }
    }

    public User getById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent())
            return userOptional.get();
        else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }

    public void updateUser(User user) {
        User updatedUser = userRepository.findById(user.getId()).get();
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        userRepository.save(updatedUser);
        CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
        customUserDetails.setFullName(user.getFirstName(), user.getLastName());
    }

    public User getUserSession() {
        try {
            CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
            return customUserDetails.getUser();
        } catch (Exception ex) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, ResponseMessage.REQUIRED_LOGIN);
        }
    }

    /**
     * Cập nhật mật khẩu
     *
     * @param passwordUpdate
     * @return
     */
    public void updatePassword(PasswordUpdate passwordUpdate) throws MessagingException, UnsupportedEncodingException {
        CustomUserDetails customUserDetails = CustomUserDetails.getAuthorizedUser();
        User user = customUserDetails.getUser();
        Boolean checkPassword = BCrypt.checkpw(passwordUpdate.getOldPassword(), user.getPassword());
        if (checkPassword) {
            user.setPassword(utility.encode(passwordUpdate.getNewPassword()));
            userRepository.save(user);
            sendEmailUpdatePassword(user);
        } else
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.PASSWORD_ERROR);
    }

    /**
     * Gửi email thông báo sự thay đổi mật khẩu
     *
     * @param user
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendEmailUpdatePassword(User user) throws MessagingException, UnsupportedEncodingException {
        String subject = "Mật khẩu đăng nhập BookStore của bạn đã thay đổi";
        String senderName = "BookStore";
        String mailContent = "Gửi " + user.getUserName();
        mailContent += "<p>BookStore xin được thông báo rằng mật khẩu đăng nhập vào tài khoản BookStore của bạn đã thay đổi</p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("bookstorevn217@gmail.com", senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void delete(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.isEnabled())
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, ResponseMessage.ACCOUNT_NOT_DELETE);
            else userRepository.deleteById(id);
        } else throw new HttpClientErrorException(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND);
    }
}
