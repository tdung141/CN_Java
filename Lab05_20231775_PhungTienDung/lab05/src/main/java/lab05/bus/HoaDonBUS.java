package lab05.bus;

import lab05.dal.HoaDonDAL;
import lab05.model.ChiTietHoaDon;
import lab05.model.HoaDon;

import java.sql.SQLException;
import java.util.List;

public class HoaDonBUS {
    private final HoaDonDAL hoaDonDAL = new HoaDonDAL();

    public List<HoaDon> findAll() throws SQLException {
        return hoaDonDAL.findAll();
    }

    public int lapHoaDon(int maKh, List<ChiTietHoaDon> chiTietList) throws SQLException {
        if (maKh <= 0) {
            throw new IllegalArgumentException("Vui long chon khach hang");
        }
        if (chiTietList == null || chiTietList.isEmpty()) {
            throw new IllegalArgumentException("Hoa don phai co it nhat 1 san pham");
        }
        for (ChiTietHoaDon ct : chiTietList) {
            if (ct.getSoLuong() <= 0) {
                throw new IllegalArgumentException("So luong san pham '" + ct.getTenSp() + "' phai lon hon 0");
            }
        }
        return hoaDonDAL.insertHoaDon(maKh, chiTietList);
    }
}
