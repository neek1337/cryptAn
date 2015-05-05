public class Ana {
    public int n, modul;
    public int[] x;
    public int[] y;

    public Ana(int n, int[] a, int[] b, int modul) {
        this.n = n;
        this.x = a;
        this.y = b;
        this.modul = modul;
    }

    public int[][] fakeInvert() {
        int[][] result = multiply(this.vecX(), this.trans());
        int[][] local = invert1(result);
        result = multiply(this.trans(), local);
        return result;
    }

    public int[][] getA() {
        int[][] a = multiply(vecY(), fakeInvert());
        return a;
    }

    public int[][] mulConst(int con, int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = (arr[i][j] * con) % modul;
            }
        }
        return arr;
    }

    public final int invert(int a) {
        for (int i = 0; i < modul; i++) {
            if ((a * i) % modul == 1) {
                return i;
            }
        }
        return 0;
    }

    public int[][] trans() {
        int[][] result = new int[1][n];
        for (int i = 0; i < n; i++) {
            result[0][i] = x[i];
        }
        return result;
    }


    public int[][] vecX() {
        int[][] result = new int[n][1];
        for (int i = 0; i < n; i++) {
            result[i][0] = x[i];
        }
        return result;
    }


    public int[][] vecY() {
        int[][] result = new int[n][1];
        for (int i = 0; i < n; i++) {
            result[i][0] = y[i];
        }
        return result;
    }

    public int[][] multiply(int A[][], int B[][]) {
        int ni = A.length;
        int nk = A[0].length;
        int nj = B[0].length;
        int C[][] = new int[ni][nj];

        for (int i = 0; i < ni; i++)
            for (int j = 0; j < nj; j++) {
                C[i][j] = 0;
                for (int k = 0; k < nk; k++)
                    C[i][j] = (C[i][j] + (A[i][k] * B[k][j]) % modul) % modul;
            }
        return C;
    }

    public final int[][] invert1(int A[][]) {
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
                    C[i][j] += modul;
                }
                C[i][j] = C[i][j] % modul;
            }
        }

        return C;
    }

    public int CalculateMatrix(int[][] matrix) {
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
        while(calcResult<0){
            calcResult+=modul;
        }
        return calcResult;
    }

    private int[][] GetMinor(int[][] matrix, int row, int column) {
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
