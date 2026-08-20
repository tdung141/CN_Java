package lab05.ui;

import lab05.bus.SanPhamBUS;
import lab05.model.SanPham;
import lab05.util.MessageUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class SanPhamPanel extends JPanel {
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();

    private final JTextField txtMaSp = new JTextField(5);
    private final JTextField txtTenSp = new JTextField(15);
    private final JTextField txtDonGia = new JTextField(10);
    private final JTextField txtSoLuong = new JTextField(6);
    private final JTextField txtTimKiem = new JTextField(15);

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new Object[]{"Ma SP", "Ten SP", "Don gia", "So luong"}, 0) {
                @Override
                public boolean isCellEditable(int row, int col) { return false; } 
            };
    private final JTable table = new JTable(tableModel);

    public SanPhamPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelectedRow();
        });

        loadData();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtMaSp.setEditable(false); 

        panel.add(new JLabel("Ma SP:"));
        panel.add(txtMaSp);
        panel.add(new JLabel("Ten SP:"));
        panel.add(txtTenSp);
        panel.add(new JLabel("Don gia:"));
        panel.add(txtDonGia);
        panel.add(new JLabel("So luong:"));
        panel.add(txtSoLuong);
        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");
        JButton btnLamMoi = new JButton("Lam moi");
        JButton btnTim = new JButton("Tim kiem");

        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e -> suaSanPham());
        btnXoa.addActionListener(e -> xoaSanPham());
        btnLamMoi.addActionListener(e -> { clearForm(); loadData(); });
        btnTim.addActionListener(e -> timKiem());

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(new JLabel("Tim theo ten:"));
        panel.add(txtTimKiem);
        panel.add(btnTim);
        return panel;
    }

    public void loadData() {
        try {
            hienThi(sanPhamBUS.findAll());
        } catch (SQLException e) {
            MessageUtil.loi(this, "Loi tai du lieu san pham: " + e.getMessage());
        }
    }

    private void timKiem() {
        try {
            hienThi(sanPhamBUS.searchByName(txtTimKiem.getText().trim()));
        } catch (SQLException e) {
            MessageUtil.loi(this, "Loi tim kiem: " + e.getMessage());
        }
    }

    private void themSanPham() {
        try {
            SanPham sp = layDuLieuTuForm();
            sp.setMaSp(0); 
            sanPhamBUS.save(sp);
            MessageUtil.thongBao(this, "Them san pham thanh cong!");
            clearForm();
            loadData();
        } catch (IllegalArgumentException ex) {
            MessageUtil.loi(this, ex.getMessage()); 
        } catch (SQLException ex) {
            MessageUtil.loi(this, "Loi: " + ex.getMessage());
        }
    }

    private void suaSanPham() {
        if (txtMaSp.getText().isBlank()) {
            MessageUtil.loi(this, "Vui long chon 1 san pham tren bang truoc khi sua");
            return;
        }
        try {
            SanPham sp = layDuLieuTuForm();
            sp.setMaSp(Integer.parseInt(txtMaSp.getText()));
            sanPhamBUS.save(sp);
            MessageUtil.thongBao(this, "Cap nhat san pham thanh cong!");
            clearForm();
            loadData();
        } catch (IllegalArgumentException ex) {
            MessageUtil.loi(this, ex.getMessage());
        } catch (SQLException ex) {
            MessageUtil.loi(this, "Loi: " + ex.getMessage());
        }
    }

    private void xoaSanPham() {
        if (txtMaSp.getText().isBlank()) {
            MessageUtil.loi(this, "Vui long chon 1 san pham tren bang truoc khi xoa");
            return;
        }
        int maSp = Integer.parseInt(txtMaSp.getText());
        if (!MessageUtil.xacNhan(this, "Ban co chac muon xoa san pham nay?")) return;

        try {
            sanPhamBUS.delete(maSp);
            MessageUtil.thongBao(this, "Xoa san pham thanh cong!");
            clearForm();
            loadData();
        } catch (SQLException ex) {
            MessageUtil.loi(this, ex.getMessage());
        }
    }

    private SanPham layDuLieuTuForm() {
        String ten = txtTenSp.getText().trim();
        BigDecimal donGia;
        int soLuong;
        try {
            donGia = new BigDecimal(txtDonGia.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Don gia phai la so hop le");
        }
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("So luong phai la so nguyen hop le");
        }
        return new SanPham(0, ten, donGia, soLuong);
    }

    private void hienThi(List<SanPham> list) {
        tableModel.setRowCount(0);
        for (SanPham sp : list) {
            tableModel.addRow(new Object[]{sp.getMaSp(), sp.getTenSp(), sp.getDonGia(), sp.getSoLuong()});
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtMaSp.setText(tableModel.getValueAt(row, 0).toString());
        txtTenSp.setText(tableModel.getValueAt(row, 1).toString());
        txtDonGia.setText(tableModel.getValueAt(row, 2).toString());
        txtSoLuong.setText(tableModel.getValueAt(row, 3).toString());
    }

    private void clearForm() {
        txtMaSp.setText("");
        txtTenSp.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("");
        table.clearSelection();
    }
}
