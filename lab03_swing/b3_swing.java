package lab03_swing;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


class b3_swing{

    static final double EPS = 1e-9;

    static JFrame frame;
    static JTextField txtA;
    static JTextField txtB;
    static JLabel lblKetQua;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        frame = new JFrame("Bài 3 - Giải phương trình ax + b = 0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // BorderLayout: kết quả ở trên (NORTH), ô nhập ở giữa (CENTER), nút ở dưới (SOUTH)
        frame.setLayout(new BorderLayout(10, 10));

        lblKetQua = new JLabel("Nghiệm: ");
        lblKetQua.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblKetQua.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 12));

        txtA = new JTextField();
        txtB = new JTextField();

        JPanel panelNhap = new JPanel(new GridLayout(2, 2, 8, 8));
        panelNhap.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        panelNhap.add(new JLabel("Hệ số a:"));
        panelNhap.add(txtA);
        panelNhap.add(new JLabel("Hệ số b:"));
        panelNhap.add(txtB);

        JButton btnGiai = new JButton("Giải phương trình");
        btnGiai.addActionListener(e -> giaiPhuongTrinh());

        JPanel panelNut = new JPanel();
        panelNut.add(btnGiai);

        frame.add(lblKetQua, BorderLayout.NORTH);
        frame.add(panelNhap, BorderLayout.CENTER);
        frame.add(panelNut, BorderLayout.SOUTH);

        frame.setSize(440, 210);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void giaiPhuongTrinh() {
        Double a = docSo(txtA, "a");
        if (a == null) return;
        Double b = docSo(txtB, "b");
        if (b == null) return;

        lblKetQua.setText("Nghiệm: " + giai(a, b));
    }

 
    static String giai(double a, double b) {
        if (Math.abs(a) < EPS && Math.abs(b) < EPS) {
            return "phương trình có vô số nghiệm";
        }
        if (Math.abs(a) < EPS) {
            return "phương trình vô nghiệm";
        }
        return String.format(Locale.US, "x = %.4f", -b / a);
    }

    static Double docSo(JTextField txt, String tenHeSo) {
        String s = txt.getText().trim().replace(',', '.');
        if (s.isEmpty()) {
            baoLoi("Bạn chưa nhập hệ số " + tenHeSo + ".", txt);
            return null;
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            baoLoi("Hệ số " + tenHeSo + " phải là số hợp lệ.", txt);
            return null;
        }
    }

    static void baoLoi(String thongBao, JTextField oCanSua) {
        JOptionPane.showMessageDialog(frame, thongBao, "Dữ liệu không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
        oCanSua.requestFocus();
        oCanSua.selectAll();
    }
}

