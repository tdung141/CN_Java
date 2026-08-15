package lab04;


import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fibonacci extends JFrame {

    private JTextField txtN;
    private JButton btnTim;
    private JLabel lblResult;
    private JProgressBar progressBar;

    private final Map<Integer, BigInteger> memo = new HashMap<>();

    public Fibonacci() {
        setTitle("Bài 3 - Tìm số Fibonacci thứ N");
        setSize(450, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        txtN = new JTextField(10);
        btnTim = new JButton("Tìm");
        lblResult = new JLabel("Kết quả: ");
        lblResult.setFont(new Font("Arial", Font.BOLD, 14));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(txtN);
        panel.add(btnTim);
        panel.add(progressBar);
        panel.add(lblResult);
        add(panel);

        memo.put(0, BigInteger.ZERO);
        memo.put(1, BigInteger.ONE);

        btnTim.addActionListener(e -> timFibonacci());
    }

    private void timFibonacci() {
        final int n;
        try {
            n = Integer.parseInt(txtN.getText().trim());
            if (n < 0) {
                JOptionPane.showMessageDialog(this, "N phải >= 0");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên hợp lệ");
            return;
        }

        if (memo.containsKey(n)) {
            lblResult.setText("Kết quả: " + memo.get(n));
            progressBar.setValue(100);
            return;
        }

        btnTim.setEnabled(false);
        progressBar.setValue(0);
        lblResult.setText("Kết quả: đang tính...");

        SwingWorker<BigInteger, Integer> worker = new SwingWorker<BigInteger, Integer>() {
            @Override
            protected BigInteger doInBackground() throws Exception {
                int start = 2;
                for (int i = n; i >= 2; i--) {
                    if (memo.containsKey(i)) {
                        start = i + 1;
                        break;
                    }
                }

                for (int i = start; i <= n; i++) {
                    BigInteger prev1 = memo.get(i - 1);
                    BigInteger prev2 = memo.get(i - 2);
                    BigInteger current = prev1.add(prev2);
                    memo.put(i, current);

                    int percent = (int) (((i - start + 1) * 100.0) / Math.max(n - start + 1, 1));
                    publish(percent);
                    Thread.sleep(5); 
                }
                return memo.get(n);
            }

            @Override
            protected void process(List<Integer> chunks) {
                progressBar.setValue(Math.min(chunks.get(chunks.size() - 1), 100));
            }

            @Override
            protected void done() {
                try {
                    BigInteger result = get();
                    lblResult.setText("Kết quả: " + result);
                    progressBar.setValue(100);
                } catch (Exception ex) {
                    lblResult.setText("Kết quả: lỗi tính toán");
                } finally {
                    btnTim.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Fibonacci().setVisible(true));
    }
}