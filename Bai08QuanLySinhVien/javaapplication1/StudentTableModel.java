/*
 * Bài 8 - Quản lý sinh viên
 */
package javaapplication1;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Model của bảng sinh viên, đồng thời là nơi giữ danh sách sinh viên.
 *
 * JTable không hiểu lớp Student, nó chỉ hỏi table model ba câu: bao nhiêu dòng,
 * bao nhiêu cột, ô (dòng, cột) chứa gì. Lớp này trả lời ba câu đó.
 *
 * Cột Số môn, Điểm TB, Xếp loại không lưu sẵn mà tính lại từ danh sách môn mỗi
 * lần bảng vẽ, nên thêm hay sửa điểm một môn là bảng sinh viên tự đổi theo.
 *
 * @author mac
 */
public class StudentTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Mã SV", "Họ tên", "Số môn", "Điểm TB", "Xếp loại"};

    private final List<Student> data = new ArrayList<>();

    // ---------- thao tác trên danh sách ----------

    /**
     * Nạp cả danh sách mới, dùng khi đọc từ file lúc mở chương trình.
     */
    public void setData(List<Student> students) {
        data.clear();
        data.addAll(students);
        fireTableDataChanged();
    }

    /**
     * Trả về danh sách đang giữ để ghi ra file.
     */
    public List<Student> getData() {
        return data;
    }

    public void addStudent(Student s) {
        data.add(s);
        // báo cho JTable biết vừa thêm 1 dòng ở cuối để nó vẽ thêm
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    /**
     * Sửa mã và họ tên của sinh viên ở dòng cho trước, giữ nguyên danh sách môn
     * đã nhập.
     */
    public void updateStudent(int modelRow, String id, String fullName) {
        Student target = data.get(modelRow);
        target.setId(id);
        target.setFullName(fullName);
        fireTableRowsUpdated(modelRow, modelRow);
    }

    public void removeStudent(int modelRow) {
        data.remove(modelRow);
        fireTableRowsDeleted(modelRow, modelRow);
    }

    public Student getStudentAt(int modelRow) {
        return data.get(modelRow);
    }

    /**
     * Vẽ lại một dòng, gọi sau khi danh sách môn của sinh viên đó thay đổi để
     * cột Số môn và Điểm TB cập nhật.
     */
    public void refreshRow(int modelRow) {
        fireTableRowsUpdated(modelRow, modelRow);
    }

    /**
     * Tìm dòng có mã sinh viên trùng, dùng để chặn trùng mã.
     *
     * @param id mã cần tìm
     * @param skipRow dòng được bỏ qua, truyền vào khi sửa để không tự báo trùng
     * với chính nó. Truyền -1 khi thêm mới.
     * @return chỉ số dòng trùng, hoặc -1 nếu không có
     */
    public int indexOfId(String id, int skipRow) {
        for (int i = 0; i < data.size(); i++) {
            if (i != skipRow && data.get(i).getId().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return -1;
    }

    // ---------- phần bắt buộc phải cài của AbstractTableModel ----------

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    /**
     * Khai báo kiểu từng cột. Nhờ cột số là Integer/Double, JTable canh phải và
     * sắp xếp theo giá trị chứ không theo chuỗi.
     */
    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 2:
                return Integer.class;
            case 3:
                return Double.class;
            default:
                return String.class;
        }
    }

    /**
     * Không cho sửa trực tiếp trên bảng, mọi thay đổi đi qua các nút.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student s = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return s.getId();
            case 1:
                return s.getFullName();
            case 2:
                return s.getSubjectCount();
            case 3:
                return QuyChe.lamTron2(s.getDiemTB());
            case 4:
                return s.getRanking();
            default:
                return null;
        }
    }
}
