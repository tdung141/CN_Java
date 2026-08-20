/*
 * Bài 8 - Quản lý sinh viên
 */
package javaapplication1;

/**
 * Một môn học của một sinh viên: tên môn và 3 đầu điểm.
 *
 * Tên môn do người dùng tự gõ (Toán, Văn, Anh, Sử, Địa...) nên không cố định
 * danh sách môn trong code.
 *
 * Điểm môn không lưu thành biến mà tính lại từ 3 đầu điểm mỗi lần gọi, sửa một
 * đầu điểm là điểm môn tự đổi theo.
 *
 * @author mac
 */
public class Subject {

    private String name;
    private double chuyenCan;
    private double giuaKy;
    private double cuoiKy;

    public Subject() {
    }

    public Subject(String name, double chuyenCan, double giuaKy, double cuoiKy) {
        this.name = name;
        this.chuyenCan = chuyenCan;
        this.giuaKy = giuaKy;
        this.cuoiKy = cuoiKy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getChuyenCan() {
        return chuyenCan;
    }

    public void setChuyenCan(double chuyenCan) {
        this.chuyenCan = chuyenCan;
    }

    public double getGiuaKy() {
        return giuaKy;
    }

    public void setGiuaKy(double giuaKy) {
        this.giuaKy = giuaKy;
    }

    public double getCuoiKy() {
        return cuoiKy;
    }

    public void setCuoiKy(double cuoiKy) {
        this.cuoiKy = cuoiKy;
    }

    /**
     * Điểm tổng của môn, tính theo trọng số trong {@link QuyChe}.
     */
    public double getDiem() {
        return QuyChe.diemMon(chuyenCan, giuaKy, cuoiKy);
    }

    /**
     * Xếp loại của riêng môn này.
     */
    public String getRanking() {
        return QuyChe.xepLoai(getDiem());
    }

    /**
     * Chép dữ liệu từ một môn khác sang, giữ nguyên đối tượng đang nằm trong
     * danh sách. Dùng khi bấm nút Sửa môn.
     */
    public void copyFrom(Subject other) {
        this.name = other.name;
        this.chuyenCan = other.chuyenCan;
        this.giuaKy = other.giuaKy;
        this.cuoiKy = other.cuoiKy;
    }

    @Override
    public String toString() {
        return name + " (" + QuyChe.lamTron2(getDiem()) + " - " + getRanking() + ")";
    }
}
