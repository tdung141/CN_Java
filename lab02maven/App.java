package lab02maven;


import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Ma SV: ");
        String ma = sc.nextLine();
        System.out.print("Ho ten: ");
        String ten = sc.nextLine();

        double cc = inputScore(sc, "diem chuyen can");
        double gk = inputScore(sc, "diem giua ky");
        double ck = inputScore(sc, "diem cuoi ky");

        Student sv = new Student(ma, ten, cc, gk, ck);

        double tong = GradeCalculator.calculateFinalScore(sv);
        char xl = GradeCalculator.classify(tong);

        System.out.printf("%s - %s - %.2f - %c%n", ma, ten, tong, xl);

        sc.close();
    }

    private static double inputScore(Scanner sc, String label) {
        double diem;
        while (true) {
            System.out.print("Nhap " + label + ": ");
            diem = sc.nextDouble();
            if (GradeCalculator.isValidScore(diem)) {
                break;
            }
            System.out.println("Diem khong hop le! Vui long nhap lai (0-10).");
        }
        sc.nextLine();
        return diem;
    }
}
