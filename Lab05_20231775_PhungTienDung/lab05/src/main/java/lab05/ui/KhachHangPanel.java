package lab05.ui;

import lab05.bus.KhachHangBUS;
import lab05.model.KhachHang;
import lab05.util.MessageUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class KhachHangPanel extends JPanel {
    private final KhachHangBUS khachHangBUS = new KhachHangBUS();

    private final JTextField txtMaKh = new JTextField(5);
    private final JTextField txtTenKh = new JTextField(15);
    private final JTextField txtSdt = new JTextField(10);
    private final JTextField txtDiaChi = new JTextField(18);
    private final JTextField txtTimKiem = new JTextField(15);

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new Object[]{"Ma KH", "Ten KH", "SDT", "Dia chi"}, 0) {
                @Override
                public boolean isCellEditable(int row, int col) { return false; }
            };
    private final JTable table = new JTable(tableModel);

    public KhachHangPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        ((AbstractDocument) txtSdt.getDocument()).setDocumentFilter(new PhoneDocumentFilter());

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
        txtMaKh.setEditable(false);

        panel.add(new JLabel("Ma KH:"));
        panel.add(txtMaKh);
        panel.add(new JLabel("Ten KH:"));
        panel.add(txtTenKh);
        panel.add(new JLabel("SDT:"));
        panel.add(txtSdt);
        panel.add(new JLabel("Dia chi:"));
        panel.add(txtDiaChi);
        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");
        JButton btnLamMoi = new JButton("Lam moi");
        JButton btnTim = new JButton("Tim kiem");

        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnLamMoi.addActionListener(e -> { clearForm(); loadData(); });
        btnTim.addActionListener(e -> timKiem());

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(new JLabel("Tim (ten/sdt):"));
        panel.add(txtTimKiem);
        panel.add(btnTim);
        return panel;
    }

    public void loadData() {
        try {
            hienThi(khachHangBUS.findAll());
        } catch (SQLException e) {
            MessageUtil.loi(this, "Loi tai du lieu khach hang: " + e.getMessage());
        }
    }

    private void timKiem() {
        try {
            hienThi(khachHangBUS.search(txtTimKiem.getText().trim()));
        } catch (SQLException e) {
            MessageUtil.loi(this, "Loi tim kiem: " + e.getMessage());
        }
    }

    private void themKhachHang() {
        try {
            KhachHang kh = layDuLieuTuForm();
            kh.setMaKh(0);
            khachHangBUS.save(kh);
            MessageUtil.thongBao(this, "Them khach hang thanh cong!");
            clearForm();
            loadData();
        } catch (IllegalArgumentException | SQLException ex) {
            MessageUtil.loi(this, ex.getMessage());
        }
    }

    private void suaKhachHang() {
        if (txtMaKh.getText().isBlank()) {
            MessageUtil.loi(this, "Vui long chon 1 khach hang tren bang truoc khi sua");
            return;
        }
        try {
            KhachHang kh = layDuLieuTuForm();
            kh.setMaKh(Integer.parseInt(txtMaKh.getText()));
            khachHangBUS.save(kh);
            MessageUtil.thongBao(this, "Cap nhat khach hang thanh cong!");
            clearForm();
            loadData();
        } catch (IllegalArgumentException | SQLException ex) {
            MessageUtil.loi(this, ex.getMessage());
        }
    }

    private void xoaKhachHang() {
        if (txtMaKh.getText().isBlank()) {
            MessageUtil.loi(this, "Vui long chon 1 khach hang tren bang truoc khi xoa");
            return;
        }
        int maKh = Integer.parseInt(txtMaKh.getText());
        if (!MessageUtil.xacNhan(this, "Ban co chac muon xoa khach hang nay?")) return;

        try {
            khachHangBUS.delete(maKh);
            MessageUtil.thongBao(this, "Xoa khach hang thanh cong!");
            clearForm();
            loadData();
        } catch (SQLException ex) {
            MessageUtil.loi(this, ex.getMessage());
        }
    }

    private KhachHang layDuLieuTuForm() {
        String ten = txtTenKh.getText().trim();
        String sdt = txtSdt.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        return new KhachHang(0, ten, sdt, diaChi);
    }

    private void hienThi(List<KhachHang> list) {
        tableModel.setRowCount(0);
        for (KhachHang kh : list) {
            tableModel.addRow(new Object[]{kh.getMaKh(), kh.getTenKh(), kh.getSdt(), kh.getDiaChi()});
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtMaKh.setText(tableModel.getValueAt(row, 0).toString());
        txtTenKh.setText(tableModel.getValueAt(row, 1).toString());
        txtSdt.setText(tableModel.getValueAt(row, 2).toString());
        Object diaChi = tableModel.getValueAt(row, 3);
        txtDiaChi.setText(diaChi == null ? "" : diaChi.toString());
    }

    private void clearForm() {
        txtMaKh.setText("");
        txtTenKh.setText("");
        txtSdt.setText("");
        txtDiaChi.setText("");
        table.clearSelection();
    }

    public List<KhachHang> getDanhSachKhachHang() throws SQLException {
        return khachHangBUS.findAll();
    }
}
