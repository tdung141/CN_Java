/*
 * Bài 8 - Quản lý sinh viên
 */
package javaapplication1;

/**
 * Quy chế tính điểm: gom mọi con số của quy chế vào một chỗ.
 *
 * Sau này thầy đổi trọng số hay đổi thang xếp loại thì chỉ sửa file này, không
 * phải đi tìm trong giao diện.
 *
 * @author mac
 */
public final class QuyChe {

    /**
     * Trọng số điểm chuyên cần: 10%.
     */
    public static final double TRONG_SO_CHUYEN_CAN = 0.1;

    /**
     * Trọng số điểm giữa kỳ: 30%.
     */
    public static final double TRONG_SO_GIUA_KY = 0.3;

    /**
     * Trọng số điểm cuối kỳ: 60%.
     */
    public static final double TRONG_SO_CUOI_KY = 0.6;

    /**
     * Điểm nhập vào phải nằm trong khoảng này.
     */
    public static final double DIEM_MIN = 0;
    public static final double DIEM_MAX = 10;

    /**
     * Lớp toàn hàm static, không cho tạo đối tượng.
     */
    private QuyChe() {
    }

    /**
     * Điểm một môn = chuyên cần×10% + giữa kỳ×30% + cuối kỳ×60%.
     */
    public static double diemMon(double chuyenCan, double giuaKy, double cuoiKy) {
        return chuyenCan * TRONG_SO_CHUYEN_CAN
                + giuaKy * TRONG_SO_GIUA_KY
                + cuoiKy * TRONG_SO_CUOI_KY;
    }

    /**
     * Xếp loại theo điểm. Dùng chung cho cả điểm từng môn và điểm trung bình.
     *
     * >= 8.5 Giỏi, >= 7.0 Khá, >= 5.0 Trung bình, còn lại Yếu.
     */
    public static String xepLoai(double diem) {
        if (diem >= 8.5) {
            return "Giỏi";
        }
        if (diem >= 7.0) {
            return "Khá";
        }
        if (diem >= 5.0) {
            return "Trung bình";
        }
        return "Yếu";
    }

    /**
     * Kiểm tra điểm có nằm trong khoảng 0 - 10 hay không.
     */
    public static boolean diemHopLe(double diem) {
        return diem >= DIEM_MIN && diem <= DIEM_MAX;
    }

    /**
     * Làm tròn 2 chữ số thập phân cho gọn khi hiển thị.
     *
     * Chỉ dùng lúc hiện ra bảng, không làm tròn lúc tính để đỡ sai số.
     */
    public static double lamTron2(double diem) {
        return Math.round(diem * 100.0) / 100.0;
    }
}
