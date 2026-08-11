package lab03_swing;

import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


class b1_swing{

    static JFrame frame;
    static JTextField txtTen;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> taoGiaoDien());
    }

    static void taoGiaoDien() {
        frame = new JFrame("Bài 1 - Chào người dùng");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        txtTen = new JTextField(20);
        JButton btnChao = new JButton("Hiển thị lời chào");

        panel.add(new JLabel("Nhập tên:"));
        panel.add(txtTen);
        panel.add(btnChao);
        frame.add(panel);

        btnChao.addActionListener(e -> hienThiLoiChao());
        txtTen.addActionListener(e -> hienThiLoiChao());

        frame.pack();                     
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    static void hienThiLoiChao() {
        String ten = txtTen.getText().trim();

      
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Vui lòng nhập tên!",
                    "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            txtTen.requestFocus();
            return;
        }

        JOptionPane.showMessageDialog(frame, "Xin chào, " + ten + "!",
                "Lời chào", JOptionPane.INFORMATION_MESSAGE);
    }
}
