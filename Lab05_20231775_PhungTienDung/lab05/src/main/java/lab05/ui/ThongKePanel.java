package lab05.ui;

import lab05.bus.ThongKeBUS;
import lab05.dal.ThongKeDAL;
import lab05.model.HoaDon;
import lab05.util.MessageUtil;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ThongKePanel extends JPanel {
    private final ThongKeBUS thongKeBUS = new ThongKeBUS();

    private final JTextField txtTuNgay = new JTextField("2024-01-01", 10);
    private final JTextField txtDenNgay = new JTextField(LocalDate.now().toString(), 10);
    private final JLabel lblDoanhThu = new JLabel("Doanh thu: -");
    private final JLabel lblHoaDonCaoNhat = new JLabel("Hoa don cao nhat: -");
    private final JLabel lblSpBanChay = new JLabel("San pham ban chay: -");

    public ThongKePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildKetQuaPanel(), BorderLayout.CENTER);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnDoanhThu = new JButton("Tinh doanh thu");
        JButton btnHoaDonCaoNhat = new JButton("Hoa don cao nhat");
        JButton btnSpBanChay = new JButton("San pham ban chay nhat");

        btnDoanhThu.addActionListener(e -> tinhDoanhThuBatDongBo());
        btnHoaDonCaoNhat.addActionListener(e -> hoaDonCaoNhatBatDongBo());
        btnSpBanChay.addActionListener(e -> spBanChayBatDongBo());

        panel.add(new JLabel("Tu ngay (yyyy-MM-dd):"));
        panel.add(txtTuNgay);
        panel.add(new JLabel("Den ngay:"));
        panel.add(txtDenNgay);
        panel.add(btnDoanhThu);
        panel.add(btnHoaDonCaoNhat);
        panel.add(btnSpBanChay);
        return panel;
    }

    private JPanel buildKetQuaPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Font font = lblDoanhThu.getFont().deriveFont(Font.BOLD, 14f);
        lblDoanhThu.setFont(font);
        lblHoaDonCaoNhat.setFont(font);
        lblSpBanChay.setFont(font);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblDoanhThu);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblHoaDonCaoNhat);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblSpBanChay);
        return panel;
    }

    private void tinhDoanhThuBatDongBo() {
        final LocalDate tuNgay, denNgay;
        try {
            tuNgay = LocalDate.parse(txtTuNgay.getText().trim());
            denNgay = LocalDate.parse(txtDenNgay.getText().trim());
        } catch (DateTimeParseException ex) {
            MessageUtil.loi(this, "Ngay khong dung dinh dang yyyy-MM-dd");
            return;
        }

        lblDoanhThu.setText("Doanh thu: dang tinh...");
        new SwingWorker<BigDecimal, Void>() {
            @Override
            protected BigDecimal doInBackground() throws Exception {
                return thongKeBUS.tinhDoanhThu(tuNgay, denNgay);
            }

            @Override
            protected void done() {
                try {
                    lblDoanhThu.setText("Doanh thu (" + tuNgay + " -> " + denNgay + "): " + get() + " VND");
                } catch (Exception e) {
                    lblDoanhThu.setText("Doanh thu: loi khi tinh");
                    MessageUtil.loi(ThongKePanel.this, "Loi thong ke doanh thu: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void hoaDonCaoNhatBatDongBo() {
        lblHoaDonCaoNhat.setText("Hoa don cao nhat: dang tim...");
        new SwingWorker<HoaDon, Void>() {
            @Override
            protected HoaDon doInBackground() throws Exception {
                return thongKeBUS.hoaDonCaoNhat();
            }

            @Override
            protected void done() {
                try {
                    HoaDon hd = get();
                    if (hd == null) {
                        lblHoaDonCaoNhat.setText("Hoa don cao nhat: chua co hoa don nao");
                    } else {
                        lblHoaDonCaoNhat.setText(String.format(
                                "Hoa don cao nhat: HD%d - %s - %s - %s VND",
                                hd.getMaHd(), hd.getTenKh(), hd.getNgayLap(), hd.getTongTien()));
                    }
                } catch (Exception e) {
                    lblHoaDonCaoNhat.setText("Hoa don cao nhat: loi khi tim");
                    MessageUtil.loi(ThongKePanel.this, "Loi thong ke hoa don: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void spBanChayBatDongBo() {
        lblSpBanChay.setText("San pham ban chay: dang tim...");
        new SwingWorker<ThongKeDAL.SanPhamBanChay, Void>() {
            @Override
            protected ThongKeDAL.SanPhamBanChay doInBackground() throws Exception {
                return thongKeBUS.sanPhamBanChayNhat();
            }

            @Override
            protected void done() {
                try {
                    ThongKeDAL.SanPhamBanChay kq = get();
                    if (kq == null) {
                        lblSpBanChay.setText("San pham ban chay: chua co du lieu ban hang");
                    } else {
                        lblSpBanChay.setText("San pham ban chay nhat: " + kq.tenSp()
                                + " (" + kq.tongSoLuong() + " san pham)");
                    }
                } catch (Exception e) {
                    lblSpBanChay.setText("San pham ban chay: loi khi tim");
                    MessageUtil.loi(ThongKePanel.this, "Loi thong ke san pham: " + e.getMessage());
                }
            }
        }.execute();
    }
}
