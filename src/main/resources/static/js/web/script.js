$(function () {
    $('.khoisanpham').slick({
        dots: false,
        infinite: false,
        speed: 300,
        slidesToShow: 5,
        slidesToScroll: 1,
        responsive: [
            {
                breakpoint: 1400,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 1,
                    infinite: true,
                    dots: true
                }
            },
            {
                breakpoint: 800,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 1
                }
            },
            {
                breakpoint: 480,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1
                }
            }
        ]
    });

    //hieu ung header va nut backtotop
    $("#backtotop").click(function () {
        $("html, body").animate({scrollTop: 0}, 400);
    });

    $(window).scroll(function () {
        if ($("body,html").scrollTop() > 150) {
            $(".navbar").addClass("fixed-top");
        } else {
            $(".navbar").removeClass("fixed-top");
        }
    });

    $(window).scroll(function () {
        if ($("body,html").scrollTop() > 500) {
            $(".nutcuonlen").addClass("hienthi");
        } else {
            $(".nutcuonlen").removeClass("hienthi");
        }
    });

    // thumb-img
    $(".thumb-img.thumb1").addClass('vienvang');
    $('.thumb-img').click(function (e) {
        $('.product-image').attr('src', this.src);
    });

    $('.thumb-img').click(function (e) {
        $('.thumb-img:not(:hover)').removeClass('vienvang');
        $(this).addClass('vienvang');
    });

    //btn-spin
    $(".btn-inc").click(function (e) {
        var strval = $(this).parent().prev().val();
        var val = parseInt(strval) + 1;
        $(this).parent().prev().attr('value', val);
    });
    $(".btn-dec").click(function (e) {
        var strval = $(this).parent().next().val();
        var val = parseInt(strval) - 1;
        if (val < 1) {
            $(this).parent().next().attr('value', 1);
        } else {
            $(this).parent().next().attr('value', val);
        }
    });

});

function submit(e) {
    if (e.keyCode == 13) {
        e.preventDefault();
    }
}
$(document).ready(function () {
    let option = sessionStorage.getItem('option');
    if (option == 'home') {
        $('.nav-home').css('background','#8e161a');
    }
    if (option == 'category') {
        $('.nav-cate').css('background','#8e161a');
    }
    if (option == 'author') {
        $('.nav-author').css('background','#8e161a');
    }
    if (option == 'contact') {
        $('.nav-contact').css('background','#8e161a');
    }

    $.getJSON('http://13.127.11.84:8082/book-store/api/cart/qty', function (res) {
        $('.cart-amount').html(res.data);
    }).fail(function (err) {
        console.log(err);
    });

    $('.btn-search').click(function () {
        let name = $('.name-search').val();
        if (name.trim() != '') {
            window.location = "http://13.127.11.84:8082/book-store/search?name=" + name.trim();
        }
    })

    $('.formdanhgia').hide();
});