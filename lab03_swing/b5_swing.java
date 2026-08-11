package lab03_swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

class b5_swing{

    static final int N_TOI_DA = 92;

    static JFrame frame;
    static JTextField txtN;
    static JTextArea txtKetQua;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        frame = new JFrame("Bài 5 - Hiển thị dãy Fibonacci");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        txtN = new JTextField(8);
        JButton btnHienThi = new JButton("Hiển thị");
        JButton btnXoa = new JButton("Xóa");

        JPanel panelTren = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        panelTren.add(new JLabel("Nhập n (1 - " + N_TOI_DA + "):"));
        panelTren.add(txtN);
        panelTren.add(btnHienThi);
        panelTren.add(btnXoa);

        txtKetQua = new JTextArea(10, 40);
        txtKetQua.setEditable(false);                        
        txtKetQua.setLineWrap(true);                           
        txtKetQua.setWrapStyleWord(true);                      
        txtKetQua.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtKetQua.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        btnHienThi.addActionListener(e -> hienThiFibonacci());
        txtN.addActionListener(e -> hienThiFibonacci());      
        btnXoa.addActionListener(e -> {
            txtN.setText("");
            txtKetQua.setText("");
            txtN.requestFocus();
        });

        frame.add(panelTren, BorderLayout.NORTH);
        frame.add(new JScrollPane(txtKetQua), BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void hienThiFibonacci() {
        String s = txtN.getText().trim();
        if (s.isEmpty()) {
            baoLoi("Bạn chưa nhập n.");
            return;
        }

        try {
            int n = Integer.parseInt(s);
            if (n <= 0 || n > N_TOI_DA) {
                baoLoi("n phải nằm trong khoảng 1 đến " + N_TOI_DA
                        + " để tránh tràn kiểu long!");
                return;
            }
            txtKetQua.setText(taoDayFibonacci(n));
            txtKetQua.setCaretPosition(0);
        } catch (NumberFormatException ex) {
            baoLoi("n phải là số nguyên dương!");
        }
    }


    static String taoDayFibonacci(int n) {
        StringBuilder sb = new StringBuilder();
        long a = 0;
        long b = 1;
        for (int i = 1; i <= n; i++) {
            if (i > 1) sb.append(" ");
            sb.append(a);
            long tiepTheo = a + b;   
            a = b;
            b = tiepTheo;
        }
        return sb.toString();
    }

    static void baoLoi(String thongBao) {
        JOptionPane.showMessageDialog(frame, thongBao, "Dữ liệu không hợp lệ",
                JOptionPane.ERROR_MESSAGE);
        txtN.requestFocus();
        txtN.selectAll();
    }
}
