import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Никита
 * Date: 16.03.15
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */
public class Solution {
    static final String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ ";
    public static int n = 0;
    public static int[][] A, B;
    public static String text;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("input.txt"));
        n = 3;
        A = new int[n][n];
        B = new int[1][n];


        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = scanner.nextInt() % alphabet.length();
            }
        }
        //   printMatrix(A);
        for (int i = 0; i < n; i++) {
            B[0][i] = scanner.nextInt() % alphabet.length();
        }
        scanner.nextLine();
        text = scanner.nextLine();
        System.out.println(crypt());
      /*  String y = scanner.next();
        String x = scanner.next();

        A = getA(x, y);
        B = getB(x, y);
        printMatrix(A);
        printMatrix(B);        */
    }

    public static int[][] getB(String mes, String cryp) {
        int[][] x = makeVectorFromS(mes.substring(0, n));
        int[][] y = makeVectorFromS(cryp.substring(0, n));
        return (sub(y, multiply(x, A)));
    }


    public static int[][] toAn(String s) {
        int[][] local = new int[n][n];
        String localString = s.substring(0, n);
        s = s.substring(n, s.length());
        int[][] first = makeVectorFromS(localString);
        for (int i = 0; i < n; i++) {
            localString = s.substring(0, n);
            s = s.substring(n, s.length());
            int[][] vec = makeVectorFromS(localString);
            for (int j = 0; j < n; j++) {

                local[i][j] = vec[0][j] - first[0][j];
                if (local[i][j] < 0) {
                    local[i][j] += alphabet.length();
                }
            }
        }
        return local;
    }


    public static int[][] getA(String a, String b) {
        // System.out.println(cryp);
        String mes = a;
        String cryp = b;
        int[][] x, y, z, p;
        x = toAn(mes);
        y = toAn(cryp);
        z = invert1(x);

        return multiply(z, y);
    }

    public static int[][] makeMFromS(String str) {
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = alphabet.indexOf(str.charAt(i * n + j));
            }
        }
        return result;
    }

    public static void printMatrix(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] % alphabet.length() + " ");
            }
            System.out.println();
        }
    }

    public static String decrypt() {
        int[][] invertedMatrix = copyVal(invert1(A));
        String decryptedText = "";
        String localText = text;
        while (localText.length() != 0) {
            int[][] local = makeVectorFromS(localText.substring(0, n));
            localText = localText.substring(n, localText.length());
            local = sub(local, B);
            local = multiply(local, invertedMatrix);

            decryptedText += StringFromMatrix(local);
        }
        return decryptedText;
    }

    public static int[][] copyVal(int[][] local) {
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = local[i][j];
            }
        }
        return result;
    }

    public static String StringFromMatrix(int[][] local) {
        String result = "";
        for (int i = 0; i < local.length; i++) {
            for (int j = 0; j < local[i].length; j++) {
                result += alphabet.charAt(local[i][j]);
            }
        }
        return result;
    }

    public static int[][] makeVectorFromS(String s) {
        int[][] result = new int[1][n];
        for (int i = 0; i < n; i++) {
            result[0][i] = alphabet.indexOf(s.charAt(i));
        }
        return result;
    }

    public static String crypt() {
        String localText = text;
        String cryptedText = "";
        while (localText.length() != 0) {
            int[][] local = makeVectorFromS(localText.substring(0, n));
            localText = localText.substring(n, localText.length());
            printMatrix(local);
            local = multiply(local, A);
            printMatrix(local);
            local = add(local, B);
            printMatrix(local);
            cryptedText += StringFromMatrix(local);
        }
        return cryptedText;
    }


    public static final int[][] multiply(final int A[][], final int B[][]) {
        int ni = A.length;
        int nk = A[0].length;
        int nj = B[0].length;
        int C[][] = new int[ni][nj];

        for (int i = 0; i < ni; i++)
            for (int j = 0; j < nj; j++) {
                C[i][j] = 0;
                for (int k = 0; k < nk; k++)
                    C[i][j] = (C[i][j] + (A[i][k] * B[k][j]) % alphabet.length()) % alphabet.length();
            }
        return C;
    }

    public static final int[][] multiply(final double A[][], final int B[][]) {
        int ni = A.length;
        int nk = A[0].length;
        int nj = B[0].length;
        int C1[][] = new int[ni][nj];
        double c[][] = new double[ni][nj];

        for (int i = 0; i < ni; i++)
            for (int j = 0; j < nj; j++) {
                c[i][j] = 0.0;
                for (int k = 0; k < nk; k++)
                    c[i][j] = (c[i][j] + A[i][k] * B[k][j]);
            }
        for (int i = 0; i < ni; i++) {
            for (int j = 0; j < nj; j++) {
                C1[i][j] = (int) Math.round(c[i][j]);
            }
        }
        return C1;
    }

    public static final int[][] add(final int A[][], final int B[][]) {
        int C[][] = new int[1][n];
        for (int i = 0; i < n; i++)
            C[0][i] = (A[0][i] + B[0][i]) % alphabet.length();
        return C;
    }

    public static final int[][] sub(final int A[][], final int B[][]) {
        int C[][] = new int[1][n];
        for (int i = 0; i < n; i++) {
            C[0][i] = (A[0][i] - B[0][i]) % alphabet.length();
            while (C[0][i] < 0) {
                C[0][i] += alphabet.length();
            }
        }
        return C;
    }

    public static final int[][] subM(final int A[][], final int B[][]) {
        int C[][] = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = (A[i][j] - B[i][j]) % alphabet.length();
                while (C[i][j] < 0) {
                    C[i][j] += alphabet.length();
                }
            }
        }
        return C;
    }


    public static final int invert(int a) {
        for (int i = 0; i < 67; i++) {
            if ((a * i) % 67 == 1) {
                return i;
            }
        }
        return a;
    }

    public static final int[][] invert1(int A[][]) {
        int det1 = invert(CalculateMatrix(A));
        int[][] At = new int[A.length][A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                At[i][j] = A[j][i];
            }
        }
        int[][] C = new int[A.length][A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                C[i][j] = det1 * CalculateMatrix(GetMinor(At, i, j));
                if ((i + j) % 2 != 0) {
                    C[i][j] *= -1;
                }
                while (C[i][j] < 0) {
                    C[i][j] += 67;
                }
                C[i][j] = C[i][j] % 67;
            }
        }

        return C;
    }

    public static int CalculateMatrix(int[][] matrix) {
        int calcResult = 0;
        if (matrix.length == 1) {
            return matrix[0][0];
        }
        if (matrix.length == 2) {
            calcResult = matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1];
        } else {
            int koeff = 1;
            for (int i = 0; i < matrix.length; i++) {
                if (i % 2 == 1) {  //я решил не возводить в степень, а просто поставить условие - это быстрее.
                    // Т.к. я раскладываю всегда по первой (читай - "нулевой") строке,
                    // то фактически я проверяю на четность значение i+0.
                    koeff = -1;
                } else {
                    koeff = 1;
                }
                ;
                //собственно разложение:
                calcResult += koeff * matrix[0][i] * CalculateMatrix(GetMinor(matrix, 0, i));
            }
        }

        //возвращаем ответ
        return calcResult;
    }

    private static int[][] GetMinor(int[][] matrix, int row, int column) {
        int minorLength = matrix.length - 1;
        int[][] minor = new int[minorLength][minorLength];
        int dI = 0;//эти переменные для того, чтобы "пропускать" ненужные нам строку и столбец
        int dJ = 0;
        for (int i = 0; i <= minorLength; i++) {
            dJ = 0;
            for (int j = 0; j <= minorLength; j++) {
                if (i == row) {
                    dI = 1;
                } else {
                    if (j == column) {
                        dJ = 1;
                    } else {
                        minor[i - dI][j - dJ] = matrix[i][j];
                    }
                }
            }
        }

        return minor;

    }
}
