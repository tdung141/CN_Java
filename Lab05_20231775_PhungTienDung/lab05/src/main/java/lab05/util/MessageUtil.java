package lab05.util;

import javax.swing.JOptionPane;
import java.awt.Component;

public class MessageUtil {
    private MessageUtil() {}

    public static void thongBao(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thong bao", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void loi(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Loi", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean xacNhan(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Xac nhan", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}
