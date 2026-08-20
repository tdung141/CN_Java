package lab05.dal;

import lab05.config.DBHelper;
import lab05.model.ChiTietHoaDon;
import lab05.model.HoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAL {
    public List<HoaDon> findAll() throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        
        String sql = "SELECT hd.ma_hd, hd.ngay_lap, hd.ma_kh, kh.ten_kh, hd.tong_tien " +
                "FROM hoa_don hd JOIN khach_hang kh ON hd.ma_kh = kh.ma_kh " +
                "ORDER BY hd.ma_hd DESC";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new HoaDon(
                        rs.getInt("ma_hd"),
                        rs.getDate("ngay_lap").toLocalDate(),
                        rs.getInt("ma_kh"),
                        rs.getString("ten_kh"),
                        rs.getBigDecimal("tong_tien")));
            }
        }
        return list;
    }

    public int insertHoaDon(int maKh, List<ChiTietHoaDon> chiTietList) throws SQLException {
        String sqlHoaDon = "INSERT INTO hoa_don(ngay_lap, ma_kh, tong_tien) VALUES (?, ?, ?)";
        String sqlChiTiet = "INSERT INTO chi_tiet_hoa_don(ma_hd, ma_sp, so_luong, don_gia, thanh_tien) " +
                "VALUES (?, ?, ?, ?, ?)";
        String sqlTruKho = "UPDATE san_pham SET so_luong = so_luong - ? WHERE ma_sp = ? AND so_luong >= ?";

        Connection conn = null;
        try {
            conn = DBHelper.getConnection();
            conn.setAutoCommit(false); 

            BigDecimal tongTien = tinhTongTien(chiTietList);
            int maHd;

            try (PreparedStatement ps = conn.prepareStatement(sqlHoaDon, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setInt(2, maKh);
                ps.setBigDecimal(3, tongTien);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        maHd = rs.getInt(1);
                    } else {
                        throw new SQLException("Khong lay duoc ma hoa don vua tao");
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlChiTiet)) {
                for (ChiTietHoaDon ct : chiTietList) {
                    ps.setInt(1, maHd);
                    ps.setInt(2, ct.getMaSp());
                    ps.setInt(3, ct.getSoLuong());
                    ps.setBigDecimal(4, ct.getDonGia());
                    ps.setBigDecimal(5, ct.getThanhTien());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlTruKho)) {
                for (ChiTietHoaDon ct : chiTietList) {
                    ps.setInt(1, ct.getSoLuong());
                    ps.setInt(2, ct.getMaSp());
                    ps.setInt(3, ct.getSoLuong());
                    ps.addBatch();
                }
                int[] result = ps.executeBatch();
                for (int r : result) {
                    if (r == 0) {
                        throw new SQLException("Mot san pham khong du ton kho de lap hoa don!");
                    }
                }
            }

            conn.commit();
            return maHd;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private BigDecimal tinhTongTien(List<ChiTietHoaDon> list) {
        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : list) {
            tong = tong.add(ct.getThanhTien());
        }
        return tong;
    }
}
