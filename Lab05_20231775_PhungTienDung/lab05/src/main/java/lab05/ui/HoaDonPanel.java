package lab05.ui;

import lab05.bus.HoaDonBUS;
import lab05.bus.SanPhamBUS;
import lab05.model.ChiTietHoaDon;
import lab05.model.KhachHang;
import lab05.model.SanPham;
import lab05.util.MessageUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HoaDonPanel extends JPanel {
    private final HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();

    private final SanPhamPanel sanPhamPanelRef;
    private final KhachHangPanel khachHangPanelRef;

    private final JComboBox<KhachHang> cboKhachHang = new JComboBox<>();
    private final JComboBox<SanPham> cboSanPham = new JComboBox<>();
    private final JTextField txtSoLuong = new JTextField(5);
    private final JLabel lblTongTien = new JLabel("Tong tien: 0 VND");

    private final List<ChiTietHoaDon> chiTietTam = new ArrayList<>();

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new Object[]{"Ma SP", "Ten SP", "So luong", "Don gia", "Thanh tien"}, 0) {
                @Override
                public boolean isCellEditable(int row, int col) { return false; }
            };
    private final JTable table = new JTable(tableModel);

    public HoaDonPanel(SanPhamPanel sanPhamPanelRef, KhachHangPanel khachHangPanelRef) {
        this.sanPhamPanelRef = sanPhamPanelRef;
        this.khachHangPanelRef = khachHangPanelRef;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        napComboBox();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTaiLai = new JButton("Tai lai KH/SP");
        btnTaiLai.addActionListener(e -> napComboBox());

        panel.add(new JLabel("Khach hang:"));
        panel.add(cboKhachHang);
        panel.add(new JLabel("San pham:"));
        panel.add(cboSanPham);
        panel.add(new JLabel("So luong:"));
        panel.add(txtSoLuong);
        panel.add(btnTaiLai);
        return panel;
    }

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnThemDong = new JButton("Them dong");
        JButton btnXoaDong = new JButton("Xoa dong da chon");
        JButton btnLuuHoaDon = new JButton("Luu hoa don");
        JButton btnLamMoi = new JButton("Lam moi");

        btnThemDong.addActionListener(e -> themDong());
        btnXoaDong.addActionListener(e -> xoaDong());
        btnLuuHoaDon.addActionListener(e -> luuHoaDon());
        btnLamMoi.addActionListener(e -> lamMoiHoaDonTam());

        buttonRow.add(btnThemDong);
        buttonRow.add(btnXoaDong);
        buttonRow.add(btnLuuHoaDon);
        buttonRow.add(btnLamMoi);

        panel.add(buttonRow, BorderLayout.NORTH);
        panel.add(lblTongTien, BorderLayout.SOUTH);
        return panel;
    }
    
    private void napComboBox() {
        cboKhachHang.removeAllItems();
        cboSanPham.removeAllItems();
        try {
            for (KhachHang kh : khachHangPanelRef.getDanhSachKhachHang()) cboKhachHang.addItem(kh);
            for (SanPham sp : sanPhamBUS.findAll()) cboSanPham.addItem(sp);
        } catch (SQLException e) {
            MessageUtil.loi(this, "Loi tai khach hang/san pham: " + e.getMessage());
        }
    }

    private void themDong() {
        SanPham sp = (SanPham) cboSanPham.getSelectedItem();
        if (sp == null) {
            MessageUtil.loi(this, "Vui long chon san pham");
            return;
        }
        int soLuong;
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
        } catch (NumberFormatException e) {
            MessageUtil.loi(this, "So luong phai la so nguyen hop le");
            return;
        }
        if (soLuong <= 0) {
            MessageUtil.loi(this, "So luong phai lon hon 0");
            return;
        }

        int daThemTruocDo = chiTietTam.stream()
                .filter(ct -> ct.getMaSp() == sp.getMaSp())
                .mapToInt(ChiTietHoaDon::getSoLuong)
                .sum();
        if (daThemTruocDo + soLuong > sp.getSoLuong()) {
            MessageUtil.loi(this, "San pham '" + sp.getTenSp() + "' khong du ton kho (con "
                    + sp.getSoLuong() + ", da chon " + daThemTruocDo + ")");
            return;
        }

        chiTietTam.add(new ChiTietHoaDon(sp.getMaSp(), sp.getTenSp(), soLuong, sp.getDonGia()));
        capNhatBangChiTiet();
        txtSoLuong.setText("");
    }

    private void xoaDong() {
        int row = table.getSelectedRow();
        if (row < 0) {
            MessageUtil.loi(this, "Vui long chon 1 dong tren bang de xoa");
            return;
        }
        chiTietTam.remove(row);
        capNhatBangChiTiet();
    }

    private void luuHoaDon() {
        KhachHang kh = (KhachHang) cboKhachHang.getSelectedItem();
        if (kh == null) {
            MessageUtil.loi(this, "Vui long chon khach hang");
            return;
        }
        try {
            int maHd = hoaDonBUS.lapHoaDon(kh.getMaKh(), chiTietTam);
            MessageUtil.thongBao(this, "Lap hoa don thanh cong! Ma hoa don: " + maHd);
            lamMoiHoaDonTam();
            
            sanPhamPanelRef.loadData();
            napComboBox();
        } catch (IllegalArgumentException ex) {
            MessageUtil.loi(this, ex.getMessage());
        } catch (SQLException ex) {
            MessageUtil.loi(this, "Loi luu hoa don: " + ex.getMessage());
        }
    }

    private void lamMoiHoaDonTam() {
        chiTietTam.clear();
        capNhatBangChiTiet();
        txtSoLuong.setText("");
    }

    private void capNhatBangChiTiet() {
        tableModel.setRowCount(0);
        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : chiTietTam) {
            tableModel.addRow(new Object[]{
                    ct.getMaSp(), ct.getTenSp(), ct.getSoLuong(), ct.getDonGia(), ct.getThanhTien()
            });
            tong = tong.add(ct.getThanhTien());
        }
        lblTongTien.setText("Tong tien: " + tong + " VND");
    }
}
