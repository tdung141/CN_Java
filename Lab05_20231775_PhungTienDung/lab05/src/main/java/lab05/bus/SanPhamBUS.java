package lab05.bus;

import lab05.dal.SanPhamDAL;
import lab05.model.SanPham;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class SanPhamBUS {
    private final SanPhamDAL sanPhamDAL = new SanPhamDAL();

    public List<SanPham> findAll() throws SQLException {
        return sanPhamDAL.findAll();
    }

    public List<SanPham> searchByName(String keyword) throws SQLException {
        return sanPhamDAL.searchByName(keyword);
    }

    public boolean save(SanPham sp) throws SQLException {
        validate(sp);
        return sp.getMaSp() == 0 ? sanPhamDAL.insert(sp) : sanPhamDAL.update(sp);
    }

    public boolean delete(int maSp) throws SQLException {
        if (maSp <= 0) {
            throw new IllegalArgumentException("Ma san pham khong hop le");
        }
        try {
            return sanPhamDAL.delete(maSp);
        } catch (SQLException e) {
            throw new SQLException("Khong the xoa: san pham nay da co trong hoa don!", e);
        }
    }

    private void validate(SanPham sp) {
        if (sp.getTenSp() == null || sp.getTenSp().trim().isEmpty()) {
            throw new IllegalArgumentException("Ten san pham khong duoc rong");
        }
        if (sp.getDonGia() == null || sp.getDonGia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Don gia phai lon hon 0");
        }
        if (sp.getSoLuong() < 0) {
            throw new IllegalArgumentException("So luong khong duoc am");
        }
    }
}
