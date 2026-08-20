package lab05.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HoaDon {
    private int maHd;
    private LocalDate ngayLap;
    private int maKh;
    private String tenKh;
    private BigDecimal tongTien;

    public HoaDon() {}

    public HoaDon(int maHd, LocalDate ngayLap, int maKh, String tenKh, BigDecimal tongTien) {
        this.maHd = maHd;
        this.ngayLap = ngayLap;
        this.maKh = maKh;
        this.tenKh = tenKh;
        this.tongTien = tongTien;
    }

    public int getMaHd() { return maHd; }
    public void setMaHd(int maHd) { this.maHd = maHd; }

    public LocalDate getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDate ngayLap) { this.ngayLap = ngayLap; }

    public int getMaKh() { return maKh; }
    public void setMaKh(int maKh) { this.maKh = maKh; }

    public String getTenKh() { return tenKh; }
    public void setTenKh(String tenKh) { this.tenKh = tenKh; }

    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
}
