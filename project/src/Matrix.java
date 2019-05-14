public class Matrix {

    private static final double PRECIOUS = 0.000001;
    
    static Matrix add(Matrix m1, Matrix m2) throws Exception {
        check(m1, m2, 0);
        double[][] value = new double[m1.getRowNumber()][m1.getColumnNumber()];
        for (int i = 0; i < m1.getRowNumber(); i++) {
            for (int j = 0; j < m1.getColumnNumber(); j++) {
                value[i][j] = m1.getValue(i, j) + m2.getValue(i, j);
            }
        }
        return new Matrix(value);
    }

    static Matrix subtract(Matrix m1, Matrix m2) throws Exception {
        check(m1, m2, 0);
        double[][] value = new double[m1.getRowNumber()][m1.getColumnNumber()];
        for (int i = 0; i < m1.getRowNumber(); i++) {
            for (int j = 0; j < m1.getColumnNumber(); j++) {
                value[i][j] = m1.getValue(i, j) - m2.getValue(i, j);
            }
        }
        return new Matrix(value);
    }

    static Matrix multiply(Matrix m1, Matrix m2) throws Exception {
        check(m1, m2, 0);
        double[][] value = new double[m1.getRowNumber()][m1.getColumnNumber()];
        for (int i = 0; i < m1.getRowNumber(); i++) {
            for (int j = 0; j < m1.getColumnNumber(); j++) {
                value[i][j] = m1.getValue(i, j) * m2.getValue(i, j);
            }
        }
        return new Matrix(value);
    }

    static Matrix divide(Matrix m1, Matrix m2) throws Exception {
        check(m1, m2, 0);
        double[][] value = new double[m1.getRowNumber()][m1.getColumnNumber()];
        for (int i = 0; i < m1.getRowNumber(); i++) {
            for (int j = 0; j < m1.getColumnNumber(); j++) {
                value[i][j] = m1.getValue(i, j) / m2.getValue(i, j);
            }
        }
        return new Matrix(value);
    }

    static Matrix matrixMultiply(Matrix m1, Matrix m2) throws Exception {
        check(m1, m2, 1);
        double[][] value = new double[m1.getRowNumber()][m2.getColumnNumber()];
        for (int i = 0; i < m1.getRowNumber(); i++) {
            for (int j = 0; j < m2.getColumnNumber(); j++) {
                value[i][j] = 0;
                for (int k = 0; k < m1.getColumnNumber(); k++) {
                    value[i][j] += (m1.getValue(i, k) * m2.getValue(k, j));
                }
            }
        }
        return new Matrix(value);
    }

    private static void check(Matrix m1, Matrix m2, int type) throws Exception {
        switch (type) {
            case 1:
                if (m1.getColumnNumber() != m2.getRowNumber()) {
                    throw new Exception("m1.getColumnNumber() != m2.getRowNumber()");
                }
                break;
            default:
                if (m1.getRowNumber() != m2.getRowNumber()) {
                    throw new Exception("m1.getRowNumber() != m2.getRowNumber()");
                }
                if (m1.getColumnNumber() != m2.getColumnNumber()) {
                    throw new Exception("m1.getColumnNumber() != m2.getColumnNumber()");
                }
                break;
        }
    }

    private double[][] value;
    private int rowNumber;
    private int columnNumber;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Matrix) {
            Matrix m = (Matrix) obj;
            if (rowNumber != m.rowNumber) {
                return false;
            }
            if (columnNumber != m.columnNumber) {
                return false;
            }
            for (int i = 0; i < rowNumber; i++) {
                for (int j = 0; j < columnNumber; j++) {
                    if (Math.abs(value[i][j] - m.value[i][j]) >= PRECIOUS) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(1024);
        stringBuilder.append("[");
        for (int i = 0; i < rowNumber; i++) {
            stringBuilder.append("[");
            for (int j = 0; j < columnNumber; j++) {
                stringBuilder.append(value[i][j]);
                if (j == columnNumber - 1) {
                    stringBuilder.append("]");
                } else {
                    stringBuilder.append(" ");
                }
            }
            if (i == rowNumber - 1) {
                stringBuilder.append("]");
            } else {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    Matrix(double[][] value) throws Exception {
        rowNumber = value.length;
        if (rowNumber == 0) {
            throw new Exception("Row number is 0.");
        }
        columnNumber = value[0].length;
        for (int i = 0; i < value.length; i++) {
            if (columnNumber != value[i].length) {
                throw new Exception("Each row should contain " + columnNumber + " element(s), but row " + i + " contains " + value[i].length + " element(s).");
            }
        }
        this.value = value;
    }

    double[][] getValue() {
        // deep copy
        double[][] value = new double[rowNumber][columnNumber];
        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                value[i][j] = this.value[i][j];
            }
        }
        return value;
    }

    double getValue(int rowIndex, int columnIndex) throws Exception {
        if (rowIndex >= rowNumber) {
            throw new Exception("Row index " + rowIndex + " is out of range (" + rowNumber + ").");
        }
        if (columnIndex >= columnNumber) {
            throw new Exception("Column index " + columnIndex + " is out of range (" + columnNumber + ").");
        }
        return value[rowIndex][columnIndex];
    }

    int getRowNumber() {
        return rowNumber;
    }

    int getColumnNumber() {
        return columnNumber;
    }
}
