/*
 * Bài 8 - Quản lý sinh viên
 */
package javaapplication1;

import java.util.ArrayList;
import java.util.List;

/**
 * Một sinh viên: mã, họ tên và danh sách môn học.
 *
 * Số môn để tự do, thêm bao nhiêu môn cũng được, không bắt buộc phải 3 môn.
 * Điểm trung bình là trung bình điểm các môn đã nhập.
 *
 * @author mac
 */
public class Student {

    private String id;
    private String fullName;

    /**
     * Danh sách môn của riêng sinh viên này. Khai báo final để danh sách luôn
     * tồn tại, khỏi phải kiểm tra null ở ngoài.
     */
    private final List<Subject> subjects = new ArrayList<>();

    public Student() {
    }

    public Student(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // ---------- thao tác trên danh sách môn ----------

    public List<Subject> getSubjects() {
        return subjects;
    }

    public int getSubjectCount() {
        return subjects.size();
    }

    public Subject getSubject(int index) {
        return subjects.get(index);
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public void removeSubject(int index) {
        subjects.remove(index);
    }

    /**
     * Tìm môn đã có cùng tên, dùng để chặn nhập trùng môn trong cùng một sinh
     * viên. Hai sinh viên khác nhau vẫn được học cùng một môn.
     *
     * @param name tên môn cần tìm
     * @param skipIndex vị trí được bỏ qua, truyền vào khi sửa để môn không tự
     * báo trùng với chính nó. Truyền -1 khi thêm mới.
     * @return vị trí môn trùng tên, hoặc -1 nếu không có
     */
    public int indexOfSubjectName(String name, int skipIndex) {
        for (int i = 0; i < subjects.size(); i++) {
            if (i != skipIndex && subjects.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    // ---------- điểm và xếp loại ----------

    /**
     * Điểm trung bình = trung bình điểm các môn đã nhập.
     *
     * Chưa nhập môn nào thì trả về 0, phần hiển thị sẽ ghi "Chưa có điểm" thay
     * vì xếp loại Yếu cho đúng nghĩa.
     */
    public double getDiemTB() {
        if (subjects.isEmpty()) {
            return 0;
        }
        double tong = 0;
        for (Subject s : subjects) {
            tong += s.getDiem();
        }
        return tong / subjects.size();
    }

    /**
     * Xếp loại theo điểm trung bình, hoặc "Chưa có điểm" khi chưa nhập môn nào.
     */
    public String getRanking() {
        if (subjects.isEmpty()) {
            return "Chưa có điểm";
        }
        return QuyChe.xepLoai(getDiemTB());
    }

    @Override
    public String toString() {
        return id + " - " + fullName + " (" + getSubjectCount() + " môn, "
                + QuyChe.lamTron2(getDiemTB()) + " - " + getRanking() + ")";
    }
}
