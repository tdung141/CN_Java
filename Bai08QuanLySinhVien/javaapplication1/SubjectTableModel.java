/*
 * Bài 8 - Quản lý sinh viên
 */
package javaapplication1;

import javax.swing.table.AbstractTableModel;

/**
 * Model của bảng điểm các môn.
 *
 * Bảng này không giữ dữ liệu riêng, nó chỉ trỏ vào sinh viên đang được chọn ở
 * bảng trên và hiện danh sách môn của sinh viên đó. Chọn sinh viên khác thì gọi
 * {@link #setStudent(Student)} là bảng đổi nội dung theo.
 *
 * Chưa chọn sinh viên nào thì student = null và bảng rỗng.
 *
 * @author mac
 */
public class SubjectTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {
        "Tên môn", "Chuyên cần (10%)", "Giữa kỳ (30%)", "Cuối kỳ (60%)", "Điểm môn", "Xếp loại"
    };

    private Student student;

    /**
     * Đổi sinh viên đang xem, bảng vẽ lại toàn bộ.
     */
    public void setStudent(Student student) {
        this.student = student;
        fireTableDataChanged();
    }

    public Student getStudent() {
        return student;
    }

    // ---------- thao tác trên danh sách môn ----------

    public Subject getSubjectAt(int modelRow) {
        return student.getSubject(modelRow);
    }

    public void addSubject(Subject subject) {
        student.addSubject(subject);
        fireTableRowsInserted(student.getSubjectCount() - 1, student.getSubjectCount() - 1);
    }

    /**
     * Ghi dữ liệu mới lên môn ở dòng cho trước.
     */
    public void updateSubject(int modelRow, Subject newData) {
        student.getSubject(modelRow).copyFrom(newData);
        fireTableRowsUpdated(modelRow, modelRow);
    }

    public void removeSubject(int modelRow) {
        student.removeSubject(modelRow);
        fireTableRowsDeleted(modelRow, modelRow);
    }

    // ---------- phần bắt buộc phải cài của AbstractTableModel ----------

    @Override
    public int getRowCount() {
        return (student == null) ? 0 : student.getSubjectCount();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        // cột 0 tên môn và cột 5 xếp loại là chữ, còn lại là số
        return (column == 0 || column == 5) ? String.class : Double.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Subject m = student.getSubject(rowIndex);
        switch (columnIndex) {
            case 0:
                return m.getName();
            case 1:
                return m.getChuyenCan();
            case 2:
                return m.getGiuaKy();
            case 3:
                return m.getCuoiKy();
            case 4:
                return QuyChe.lamTron2(m.getDiem());
            case 5:
                return m.getRanking();
            default:
                return null;
        }
    }
}
