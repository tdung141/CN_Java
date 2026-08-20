package lab05.bus;

import lab05.dal.KhachHangDAL;
import lab05.model.KhachHang;

import java.sql.SQLException;
import java.util.List;

public class KhachHangBUS {
    private final KhachHangDAL khachHangDAL = new KhachHangDAL();

    public List<KhachHang> findAll() throws SQLException {
        return khachHangDAL.findAll();
    }

    public List<KhachHang> search(String keyword) throws SQLException {
        return khachHangDAL.search(keyword);
    }

    public boolean save(KhachHang kh) throws SQLException {
        validate(kh);
        if (khachHangDAL.existsSdt(kh.getSdt(), kh.getMaKh())) {
            throw new IllegalArgumentException("So dien thoai da ton tai cho khach hang khac");
        }
        return kh.getMaKh() == 0 ? khachHangDAL.insert(kh) : khachHangDAL.update(kh);
    }

    public boolean delete(int maKh) throws SQLException {
        if (maKh <= 0) {
            throw new IllegalArgumentException("Ma khach hang khong hop le");
        }
        try {
            return khachHangDAL.delete(maKh);
        } catch (SQLException e) {
            throw new SQLException("Khong the xoa: khach hang nay da co hoa don!", e);
        }
    }

    private void validate(KhachHang kh) {
        if (kh.getTenKh() == null || kh.getTenKh().trim().isEmpty()) {
            throw new IllegalArgumentException("Ten khach hang khong duoc rong");
        }
        
        if (kh.getSdt() == null || !kh.getSdt().matches("\\d{1,10}")) {
            throw new IllegalArgumentException("So dien thoai chi gom so va toi da 10 ky tu");
        }
    }
}
