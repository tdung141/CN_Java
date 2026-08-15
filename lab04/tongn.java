package lab04;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class tongn extends JFrame {

    private JTextField txtN;
    private JButton btnTinh;
    private JLabel lblResult;
    private JProgressBar progressBar;

    public tongn() {
        setTitle("Bài 2 - Tổng số nguyên tố nhỏ hơn N");
        setSize(400, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        txtN = new JTextField(10);
        btnTinh = new JButton("Tính");
        lblResult = new JLabel("Kết quả: ");
        lblResult.setFont(new Font("Arial", Font.BOLD, 16));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(txtN);
        panel.add(btnTinh);
        panel.add(progressBar);
        panel.add(lblResult);
        add(panel);

        btnTinh.addActionListener(e -> tinhTong());
    }

    private void tinhTong() {
        final int n;
        try {
            n = Integer.parseInt(txtN.getText().trim());
            if (n <= 0) {
                JOptionPane.showMessageDialog(this, "N phải lớn hơn 0");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên hợp lệ");
            return;
        }

        btnTinh.setEnabled(false);
        progressBar.setValue(0);
        lblResult.setText("Kết quả: đang tính...");

        SwingWorker<Long, Integer> worker = new SwingWorker<Long, Integer>() {
            @Override
            protected Long doInBackground() throws Exception {
                long tong = 0;
                for (int i = 2; i < n; i++) {
                    if (isPrime(i)) {
                        tong += i;
                    }
                    int percent = (int) (((i - 1) * 100.0) / Math.max(n - 2, 1));
                    publish(percent);
                }
                return tong;
            }

            @Override
            protected void process(List<Integer> chunks) {
                int value = chunks.get(chunks.size() - 1);
                progressBar.setValue(Math.min(value, 100));
            }

            @Override
            protected void done() {
                try {
                    long tong = get();
                    lblResult.setText("Kết quả: " + tong);
                    progressBar.setValue(100);
                } catch (Exception ex) {
                    lblResult.setText("Kết quả: lỗi tính toán");
                } finally {
                    btnTinh.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new tongn().setVisible(true));
    }
}