package lab05.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("MiniShop - Quan ly ban hang (Lab 5)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null); 

        JTabbedPane tabbedPane = new JTabbedPane();

        SanPhamPanel sanPhamPanel = new SanPhamPanel();
        KhachHangPanel khachHangPanel = new KhachHangPanel();

        HoaDonPanel hoaDonPanel = new HoaDonPanel(sanPhamPanel, khachHangPanel);
        ThongKePanel thongKePanel = new ThongKePanel();

        tabbedPane.addTab("San pham", sanPhamPanel);
        tabbedPane.addTab("Khach hang", khachHangPanel);
        tabbedPane.addTab("Hoa don", hoaDonPanel);
        tabbedPane.addTab("Thong ke", thongKePanel);

        add(tabbedPane);
    }
}
