package lab02maven;

public class GradeCalculator {

    public static double calculateFinalScore(Student sv) {
        return sv.getAttendanceScore() * 0.1
                + sv.getMidtermScore() * 0.3
                + sv.getFinalScore() * 0.6;
    }

    public static char classify(double tong) {
        char xl;
        if (tong >= 8.5) xl = 'A';
        else if (tong >= 7) xl = 'B';
        else if (tong >= 5.5) xl = 'C';
        else if (tong >= 4) xl = 'D';
        else xl = 'F';
        return xl;
    }

    public static boolean isValidScore(double diem) {
        return diem >= 0 && diem <= 10;
    }
}
