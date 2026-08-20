/*
 * Bài 8 - Quản lý sinh viên
 */
package javaapplication1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Đọc và ghi danh sách sinh viên ra file text, để tắt chương trình mở lại vẫn
 * còn dữ liệu.
 *
 * Định dạng file: mỗi dòng một bản ghi, các cột cách nhau bằng dấu |
 *
 * <pre>
 * SV|SV001|Nguyễn Văn An
 * MH|Toán|9|8.5|9
 * MH|Văn|7|8|7.5
 * SV|SV002|Trần Thị Bình
 * MH|Anh|8|7|6.5
 * </pre>
 *
 * Dòng SV mở đầu một sinh viên, các dòng MH ngay sau đó là môn của sinh viên
 * vừa mở. Vì vậy thứ tự dòng trong file có ý nghĩa, không được sắp xếp lại.
 *
 * File nằm trong thư mục chạy chương trình, trong NetBeans là thư mục project.
 *
 * @author mac
 */
public final class StudentStorage {

    /**
     * Tên file dữ liệu.
     */
    public static final String FILE_NAME = "sinhvien.txt";

    /**
     * Ký tự phân cách cột. Vì nó là phân cách nên tên môn và họ tên không được
     * chứa ký tự này, form sẽ chặn lúc nhập.
     */
    public static final String SEP = "|";

    private static final String TYPE_STUDENT = "SV";
    private static final String TYPE_SUBJECT = "MH";

    private StudentStorage() {
    }

    /**
     * Đường dẫn file dữ liệu, đưa ra ngoài để hiện trên tiêu đề cửa sổ cho biết
     * dữ liệu đang nằm ở đâu.
     */
    public static Path filePath() {
        return Paths.get(FILE_NAME).toAbsolutePath();
    }

    /**
     * Đọc danh sách sinh viên từ file.
     *
     * Lần đầu chạy chưa có file thì trả về danh sách rỗng, coi như chưa có sinh
     * viên nào, không báo lỗi.
     *
     * @throws IOException khi không đọc được file hoặc file sai định dạng
     */
    public static List<Student> load() throws IOException {
        List<Student> result = new ArrayList<>();
        Path path = filePath();
        if (!Files.exists(path)) {
            return result;
        }

        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        Student current = null;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            int soDong = i + 1; // đếm từ 1 cho giống lúc mở file ra xem
            // -1 để giữ lại cả cột rỗng ở cuối, nhờ vậy thiếu cột là phát hiện được
            String[] cols = line.split("\\|", -1);

            if (TYPE_STUDENT.equals(cols[0])) {
                if (cols.length != 3) {
                    throw new IOException("File " + FILE_NAME + " sai định dạng ở dòng " + soDong
                            + ": dòng SV phải có 3 cột.");
                }
                current = new Student(cols[1], cols[2]);
                result.add(current);
            } else if (TYPE_SUBJECT.equals(cols[0])) {
                if (cols.length != 5) {
                    throw new IOException("File " + FILE_NAME + " sai định dạng ở dòng " + soDong
                            + ": dòng MH phải có 5 cột.");
                }
                if (current == null) {
                    throw new IOException("File " + FILE_NAME + " sai định dạng ở dòng " + soDong
                            + ": có môn học nhưng chưa có dòng SV nào phía trên.");
                }
                try {
                    current.addSubject(new Subject(cols[1],
                            Double.parseDouble(cols[2]),
                            Double.parseDouble(cols[3]),
                            Double.parseDouble(cols[4])));
                } catch (NumberFormatException ex) {
                    throw new IOException("File " + FILE_NAME + " sai định dạng ở dòng " + soDong
                            + ": điểm phải là số.", ex);
                }
            } else {
                throw new IOException("File " + FILE_NAME + " sai định dạng ở dòng " + soDong
                        + ": dòng phải bắt đầu bằng SV hoặc MH.");
            }
        }
        return result;
    }

    /**
     * Ghi cả danh sách ra file, ghi đè nội dung cũ.
     *
     * Gọi lại sau mỗi lần thêm, sửa, xóa nên dữ liệu luôn khớp với bảng đang
     * hiện. Danh sách ở đây chỉ vài chục dòng nên ghi lại cả file là đủ nhanh.
     *
     * @throws IOException khi không ghi được file
     */
    public static void save(List<Student> students) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Student s : students) {
            sb.append(TYPE_STUDENT).append(SEP)
                    .append(s.getId()).append(SEP)
                    .append(s.getFullName()).append(System.lineSeparator());
            for (Subject m : s.getSubjects()) {
                sb.append(TYPE_SUBJECT).append(SEP)
                        .append(m.getName()).append(SEP)
                        .append(m.getChuyenCan()).append(SEP)
                        .append(m.getGiuaKy()).append(SEP)
                        .append(m.getCuoiKy()).append(System.lineSeparator());
            }
        }
        Files.write(filePath(), sb.toString().getBytes(StandardCharsets.UTF_8));
    }
}
