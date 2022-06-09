package com.bookstore.response;

public class ResponseMessage {
    public static final String REQUIRED_LOGIN = "Yêu cầu đăng nhập";
    public static final String EMAIL_EXIST = "Email này đã tồn tại";
    public static final String VERIFY_TITLE_SUCCESS = "Xác nhận thành công";
    public static final String VERIFY_TITLE_ERROR = "Xác nhận thất bại";
    public static final String VERIFY_USER_SUCCESS = "Xác nhận tài khoản thành công.Bạn có thể đăng nhập vào hệ thống";
    public static final String EMAIL_NOT_EXIST = "Email này không tồn tại";
    public static final String PASSWORD_ERROR = "Mật khẩu không chính xác";
    public static final String ACCESS_DENIED = "Truy cập bị từ chối";
    public static final String ACCOUNT_NOT_ACTIVE = "Rất tiếc,tài khoản này chưa được kích hoạt";
    public static final String ACCOUNT_NOT_DELETE = "Không thể xóa tài khoản này do tài khoản đã được kích hoạt";
    public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
    public static final String LOGOUT_SUCCESS = "Đăng xuất thành công";

    public static final String NOT_FOUND = "Không tìm thấy";
    public static final String IMAGE_NOT_FOUND = "Không tìm thấy ảnh";
    public static final String CART_ITEM_EXIST = "Sản phẩm này đã có trong giỏ hàng";
    public static final String CART_ITEM_NOT_FOUND = "Không tìm thấy item này";
    public static final String CATEGORY_NOT_FOUND = "Không tìm thấy danh mục này";
    public static final String PAGE_ERROR = "Số trang phải lớn hơn 0";

    public static final String DELETE_SUCCESS = "Xóa thành công";
    public static final String UPDATE_SUCCESS = "Cập nhật thành công";
    public static final String SQL_ERROR = "Không thể xóa vì mối quan hệ ràng buộc trong cơ sở dữ liệu";

    public static final String UPDATE_ORDER_DELIVERY_ERROR = "Đơn hàng này đã bị hủy,nên không thể xác nhận được.\n" +
            "Vui lòng tải lại trang để cập nhật trạng thái đơn hàng";
    public static final String UPDATE_ORDER_DELIVERED_ERROR = "Đơn hàng này đã bị hủy,nên không thể cập nhật được.\n" +
            "Vui lòng tải lại trang để cập nhật trạng thái đơn hàng";
    public static final String CANCELLED_ORDER_ERROR = "Đơn hàng đã được giao thành công.\n" +
            "Vui lòng tải lại trang để cập nhật trạng thái đơn hàng";
}
