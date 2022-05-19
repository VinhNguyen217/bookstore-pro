$(document).ready(function () {
    $('.load-update-pw').hide();
    let oldPassword = $('.oldPassword').val();
    let password = $('.password').val();
    let confirmPassword = $('.confirmPassword').val();

    const UPDATE_PASSWORD_SUCCESS = "Cập nhật mật khẩu thành công";
    const PASSWORD_VALIDATE = 'Mật khẩu không hợp lệ';
    const CONFIRM_PASSWORD = 'Mật khẩu chưa trùng khớp';

    $('.oldPassword').keyup(function () {
        oldPassword = $('.oldPassword').val();
        checkInputPassword();
    });
    $('.password').keyup(function () {
        password = $('.password').val();
        checkInputPassword();
    });
    $('.confirmPassword').keyup(function () {
        confirmPassword = $('.confirmPassword').val();
        checkInputPassword();
    });

    $('#eye-oldPassword').click(function () {
        if (document.getElementById("icon-eye-oldPassword").classList.contains('fa-eye')) {
            document.getElementById("icon-eye-oldPassword").classList.remove('fa-eye');
            document.getElementById("icon-eye-oldPassword").classList.add('fa-eye-slash');
            $('.oldPassword').get(0).type = 'text';
        } else {
            document.getElementById("icon-eye-oldPassword").classList.remove('fa-eye-slash');
            document.getElementById("icon-eye-oldPassword").classList.add('fa-eye');
            $('.oldPassword').get(0).type = 'password';
        }
    })

    $('#eye-password').click(function () {
        if (document.getElementById("icon-eye-password").classList.contains('fa-eye')) {
            document.getElementById("icon-eye-password").classList.remove('fa-eye');
            document.getElementById("icon-eye-password").classList.add('fa-eye-slash');
            $('.password').get(0).type = 'text';
        } else {
            document.getElementById("icon-eye-password").classList.remove('fa-eye-slash');
            document.getElementById("icon-eye-password").classList.add('fa-eye');
            $('.password').get(0).type = 'password';
        }
    })

    $('#eye-confirmPassword').click(function () {
        if (document.getElementById("icon-eye-confirmPassword").classList.contains('fa-eye')) {
            document.getElementById("icon-eye-confirmPassword").classList.remove('fa-eye');
            document.getElementById("icon-eye-confirmPassword").classList.add('fa-eye-slash');
            $('.confirmPassword').get(0).type = 'text';
        } else {
            document.getElementById("icon-eye-confirmPassword").classList.remove('fa-eye-slash');
            document.getElementById("icon-eye-confirmPassword").classList.add('fa-eye');
            $('.confirmPassword').get(0).type = 'password';
        }
    })

    $('#btn-udpw').click(function () {
        checkPassword();
    })

    $('.btn-ok-udpw').click(function () {
        $('.dialog-udpw').removeClass('appear');
    })

    function checkInputPassword() {
        if (oldPassword != '' && password != '' && confirmPassword != '') {
            document.getElementById("btn-udpw").disabled = false;
        } else {
            document.getElementById("btn-udpw").disabled = true;
        }
    }

    function checkPassword() {
        if (password.length < 8) {
            $('#msg-pw').text(PASSWORD_VALIDATE);
            $('#msg-cfpw').text(null);
        } else {
            if (password != confirmPassword) {
                $('#msg-cfpw').text(CONFIRM_PASSWORD);
                $('#msg-pw').text(null);
            } else {
                $('#msg-cfpw').text(null);
                $('#msg-pw').text(null);
                $('.load-update-pw').show();
                updatePassword(oldPassword, password);
            }
        }
    }

    function updatePassword(oldPw, newPw) {
        $.ajax({
            url: "http://13.212.87.195:5000/book-store/api/user/update-password",
            type: "PUT",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                "oldPassword": oldPw,
                "newPassword": newPw,
            }),
            success: function () {
                $('.dialog-udpw p').text(UPDATE_PASSWORD_SUCCESS);
                $('.load-update-pw').hide();
                $('.dialog-udpw').addClass('appear');
                $('#msg-opw').text(null);
                $('.oldPassword').val(null);
                $('.password').val(null);
                $('.confirmPassword').val(null);
                document.getElementById('btn-udpw').disabled = true;
            }, error: function (err) {
                $('#msg-opw').text(err.responseJSON.message);
                $('.load-update-pw').hide();
            }
        })
    }
})