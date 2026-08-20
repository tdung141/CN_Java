package lab05;

import lab05.config.DBHelper;
import lab05.ui.MainFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        boolean ketNoiOk = DBHelper.testConnection();
        if (!ketNoiOk) {
            JOptionPane.showMessageDialog(null,
                    "Khong the ket noi CSDL minishop_db.\n" +
                            "Kiem tra MySQL server va thong tin trong DBHelper.java.",
                    "Loi ket noi", JOptionPane.ERROR_MESSAGE);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
