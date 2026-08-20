package lab05.dal;

import lab05.config.DBHelper;
import lab05.model.HoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class ThongKeDAL {
    public BigDecimal tinhDoanhThu(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        String sql = "SELECT COALESCE(SUM(tong_tien), 0) AS doanh_thu " +
                "FROM hoa_don WHERE ngay_lap BETWEEN ? AND ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getBigDecimal("doanh_thu") : BigDecimal.ZERO;
            }
        }
    }

    public HoaDon hoaDonCaoNhat() throws SQLException {
        String sql = "SELECT hd.ma_hd, hd.ngay_lap, hd.ma_kh, kh.ten_kh, hd.tong_tien " +
                "FROM hoa_don hd JOIN khach_hang kh ON hd.ma_kh = kh.ma_kh " +
                "ORDER BY hd.tong_tien DESC LIMIT 1";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new HoaDon(
                        rs.getInt("ma_hd"),
                        rs.getDate("ngay_lap").toLocalDate(),
                        rs.getInt("ma_kh"),
                        rs.getString("ten_kh"),
                        rs.getBigDecimal("tong_tien"));
            }
            return null;
        }
    }

    public record SanPhamBanChay(String tenSp, int tongSoLuong) {}

    public SanPhamBanChay sanPhamBanChayNhat() throws SQLException {
        String sql = "SELECT sp.ten_sp, SUM(ct.so_luong) AS tong_so_luong " +
                "FROM chi_tiet_hoa_don ct JOIN san_pham sp ON ct.ma_sp = sp.ma_sp " +
                "GROUP BY sp.ma_sp, sp.ten_sp " +
                "ORDER BY tong_so_luong DESC LIMIT 1";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new SanPhamBanChay(rs.getString("ten_sp"), rs.getInt("tong_so_luong"));
            }
            return null;
        }
    }
}
