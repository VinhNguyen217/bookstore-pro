// Đăng ký tài khoản
$(document).ready(function () {
    const regexEmail = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    const EMAIL_VALIDATE = 'Hãy nhập địa chỉ email hợp lệ .\nExample@gmail.com'
    const PASSWORD_VALIDATE = 'Mật khẩu không hợp lệ';
    const REGIS_ALERT = 'Tạo tài khoản thành công.Bạn vui lòng xác nhận email mà chúng tôi gửi đến để kích hoạt tài khoản';

    let firstname = $('.regis-firstname').val();
    let lastname = $('.regis-lastname').val();
    let email = $('.regis-email').val();
    let password = $('.regis-password').val();

    $('.msg-regis-alert').hide();
    $('.load-regis').hide();

    $('.regis-firstname').keyup(function () {
        firstname = $('.regis-firstname').val();
        activeBtnRegis();
    });
    $('.regis-lastname').keyup(function () {
        lastname = $('.regis-lastname').val();
        activeBtnRegis();
    });
    $('.regis-email').keyup(function () {
        email = $('.regis-email').val();
        activeBtnRegis();
    });
    $('.regis-password').keyup(function () {
        password = $('.regis-password').val();
        activeBtnRegis();
    })

    $('#btn-regis').click(function () {
        checkInput();
    });

    function activeBtnRegis() {
        if (firstname.trim() != '' && lastname.trim() != '' && email.trim() != '' && password != '') {
            document.getElementById('btn-regis').disabled = false;
        } else {
            document.getElementById('btn-regis').disabled = true;
        }
    }

    function checkInput() {
        if (!regexEmail.test(email)) {
            $('.msg-regis-email').text(EMAIL_VALIDATE);
        } else {
            $('.msg-regis-email').text(null);
            if (password.length < 8)
                $('.msg-regis-pw').text(PASSWORD_VALIDATE);
            else {
                $('.msg-regis-pw').text(null);
                $('.load-regis').show();
                createUser();
            }
        }
    }

    function createUser() {
        $.ajax({
            url: 'http://13.127.11.84:8082/book-store/api/user/create',
            dataType: 'json',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                "firstName": firstname,
                "lastName": lastname,
                "email": email,
                "password": password,
                "role": 'ROLE_USER'
            }),
            processData: false,
            success: function (res) {
                console.log(res);
                $('.load-regis').hide();
                $('.msg-regis-alert').show();
                $('.msg-regis-alert').text(REGIS_ALERT);
                $('.regis-firstname').val('');
                $('.regis-lastname').val('');
                $('.regis-password').val('');
                $('.regis-email').val('');
                document.getElementById('btn-regis').disabled = true;
            },
            error: function (err) {
                console.log(err);
                $('.load-regis').hide();
                $('.msg-regis-email').text(err.responseJSON.message);
            }
        });
    }
})

//Đăng nhập
$(document).ready(function () {
    const regexEmail = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    const EMAIL_VALIDATE = 'Hãy nhập địa chỉ email hợp lệ .\nExample@gmail.com';

    let email = $('.login-email').val();
    let password = $('.login-password').val();

    $('.alert-login').hide();

    $('.login-email').keyup(function () {
        email = $('.login-email').val();
        activeBtnLogin();
    });
    $('.login-password').keyup(function () {
        password = $('.login-password').val();
        activeBtnLogin();
    })
    $('#btn-login').click(function () {
        checkInput();
    })

    function activeBtnLogin() {
        if (email.trim() != '' && password != '') {
            document.getElementById('btn-login').disabled = false;
        } else {
            document.getElementById('btn-login').disabled = true;
        }
    }

    function checkInput() {
        if (!regexEmail.test(email)) {
            $('.alert-login').hide();
            $('.msg-login-email').text(EMAIL_VALIDATE);
        } else {
            $('.msg-login-email').text(null);
            login();
        }
    }

    function login() {
        $.ajax({
            url: 'http://13.127.11.84:8082/book-store/api/user/login',
            dataType: 'json',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                "email": email,
                "password": password,
            }),
            processData: false,
            success: function (res) {
                alert(res.message);
                window.location = 'http://13.127.11.84:8082/book-store/';
            },
            error: function (err) {
                $('.alert-login').show();
                $('.alert-login').text(err.responseJSON.message);
            }
        });
    }
})


