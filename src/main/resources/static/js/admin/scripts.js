window.addEventListener('DOMContentLoaded', event => {
    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }
});

/**
 * Login admin
 */
$(document).ready(function () {
    const regexEmail = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    const EMAIL_VALIDATE = 'Hãy nhập địa chỉ email hợp lệ .\nExample@gmail.com';

    let email = $('.login-email').val();
    let password = $('.login-password').val();
    $('.login-alert').hide();

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
            $('.msg-email').text(EMAIL_VALIDATE);
        } else {
            $('.msg-email').text(null);
            login();
        }
    }

    function login() {
        $.ajax({
            url: 'http://13.127.11.84:8082/book-store/api/admin/login',
            dataType: 'json',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                "email": email,
                "password": password,
            }),
            processData: false,
            success: function (res) {
                window.localStorage.setItem('admin', res.data.userName);
                sessionStorage.setItem('index', 'home');
                alert(res.message);
                window.location = 'http://13.127.11.84:8082/book-store/admin';
            },
            error: function (err) {
                $('.login-alert').show();
                $('.login-alert p').text(err.responseJSON.message)
            }
        });
    }
})

$(document).ready(function () {
    $.getJSON("http://13.127.11.84:8082/book-store/api/admin/getUserName", function (res) {
        if (res.data == null) {
            $('#sidenavAccordion').hide();
            $('#sidebarToggle').hide();
            $('#account').hide();
        } else {
            $('.admin-info').html(res.data);
        }
    }).fail(function (err) {
        console.log(err);
    })


    $('.fa-times').click(function () {
        $('.alert-success').hide();
        $('.alert-danger').hide();
    })

    /**
     * Đăng xuất admin
     */
    $('.logout').click(function () {
        $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            url: 'http://13.127.11.84:8082/book-store/api/admin/logout',
            cache: false,
            success: function () {
                window.location.href = "http://13.127.11.84:8082/book-store/admin/login";
                sessionStorage.clear();
            }, error: function (err) {
                console.log(err);
            }
        })
    })
});

$(document).ready(function () {
    $('.nav_home').click(function () {
        sessionStorage.setItem('index', 'home');
    })
    $('.nav_category').click(function () {
        sessionStorage.setItem('index', 'category');
    })
    $('.nav_author').click(function () {
        sessionStorage.setItem('index', 'author');
    })
    $('.nav_book').click(function () {
        sessionStorage.setItem('index', 'book');
    })
    $('#nav_account').click(function () {
        sessionStorage.setItem('index', 'account');
    })
    $('#nav_promotion').click(function () {
        sessionStorage.setItem('index', 'promotion');
    })
    $('.nav_order').click(function () {
        sessionStorage.setItem('index', 'order');
    })
    $('#nav_revenue').click(function () {
        sessionStorage.setItem('index', 'revenue');
    })

    if (sessionStorage.getItem('index') == 'home')
        $('#nav_home').css('color', 'blue');
    if (sessionStorage.getItem('index') == 'category')
        $('#nav_category').css('color', 'blue');
    if (sessionStorage.getItem('index') == 'author')
        $('#nav_author').css('color', 'blue');
    if (sessionStorage.getItem('index') == 'book')
        $('#nav_book').css('color', 'blue')
    if (sessionStorage.getItem('index') == 'account')
        $('#nav_account').css('color', 'blue')
    if (sessionStorage.getItem('index') == 'order') {
        $('#nav_report').removeClass('collapsed');
        $('#nav_report').attr("aria-expanded", "true");
        $('#pagesCollapseAuth').addClass('show');
        $('#nav_order').css('color', 'blue')
    }
    if (sessionStorage.getItem('index') == 'revenue') {
        $('#nav_report').removeClass('collapsed');
        $('#nav_report').attr("aria-expanded", "true");
        $('#pagesCollapseAuth').addClass('show');
        $('#nav_revenue').css('color', 'blue')
    }
    if (sessionStorage.getItem('index') == 'promotion')
        $('#nav_promotion').css('color', 'blue')
});
