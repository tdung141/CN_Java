package lab05.bus;

import lab05.dal.ThongKeDAL;
import lab05.model.HoaDon;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class ThongKeBUS {
    private final ThongKeDAL thongKeDAL = new ThongKeDAL();

    public BigDecimal tinhDoanhThu(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        if (tuNgay == null || denNgay == null) {
            throw new IllegalArgumentException("Vui long nhap day du tu ngay va den ngay");
        }
        if (tuNgay.isAfter(denNgay)) {
            throw new IllegalArgumentException("Tu ngay khong duoc sau den ngay");
        }
        return thongKeDAL.tinhDoanhThu(tuNgay, denNgay);
    }

    public HoaDon hoaDonCaoNhat() throws SQLException {
        return thongKeDAL.hoaDonCaoNhat();
    }

    public ThongKeDAL.SanPhamBanChay sanPhamBanChayNhat() throws SQLException {
        return thongKeDAL.sanPhamBanChayNhat();
    }
}
