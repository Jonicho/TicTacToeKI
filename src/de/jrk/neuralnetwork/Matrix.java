package de.jrk.neuralnetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matrix {
	private final double[][] data;

	/**
	 * Erzeugt ein Matrix mit {@code rows} Zeilen und {@code cols} Spalten.
	 * 
	 * @param rows
	 *            Die Anzahl der Zeilen.
	 * @param cols
	 *            Die Anzahl der Spalten.
	 */
	public Matrix(int rows, int cols) {
		data = new double[rows][cols];
	}

	/**
	 * Gibt den Wert an der Position {@code i,j} zurück.
	 * 
	 * @param i
	 *            Der Zeilenindex.
	 * @param j
	 *            Der Spaltenindex.
	 * @return Der Wert an der Position {@code i,j}.
	 */
	public double get(int i, int j) {
		return data[i][j];
	}

	/**
	 * Gibt die Anzahl der Zeilen zurück.
	 * 
	 * @return Die Anzahl der Zeilen.
	 */
	public int getRows() {
		return data.length;
	}

	/**
	 * Gibt die Anzahl der Spalten zurück.
	 * 
	 * @return Die Anzahl der Spalten.
	 */
	public int getCols() {
		return data[0].length;
	}

	/**
	 * Gibt die Summe dieser Matrix und der Matrix {@code m} zurück. Die übergebene
	 * Matrix muss die gleiche Größe wie diese Matrix haben.
	 * 
	 * @param m
	 *            Die Matrix, die addiert werden soll.
	 * @return Die Summenmatrix.
	 */
	public Matrix add(Matrix m) {
		if (getRows() != m.getRows() || getCols() != m.getCols()) {
			throw new IllegalArgumentException("The size of this Matrix does not match the size of the given Matrix!");
		}
		return map((x, i, j) -> x + m.get(i, j));
	}

	/**
	 * Gibt die Produktmatrix der Matrixmultiplikation von dieser Matrix und der
	 * Matrix {@code m} zurück.
	 * 
	 * @param m
	 *            Die Matrix mit der multipliziert werden soll.
	 * 
	 * @return Die Produktmatrix.
	 */
	public Matrix multiply(Matrix m) {
		if (getCols() != m.getRows()) {
			throw new IllegalArgumentException("The columns of this Matrix do not match the rows of the given Matrix!");
		}
		return new Matrix(getRows(), m.getCols()).map((x, i, j) -> {
			double sum = 0;
			for (int k = 0; k < getCols(); k++) {
				sum += get(i, k) * m.get(k, j);
			}
			return sum;
		});
	}

	/**
	 * Wendet die übergebene {@link MapFunction} {@code function} auf alle Werte
	 * dieser Matrix an und gibt eine Matrix mit den Ergebnissen der
	 * {@link MapFunction} zurück.
	 * 
	 * @param function
	 *            Die Funktion, die auf alle Werte angewendet werden soll.
	 * @return Die Ergebnismatrix.
	 */
	public Matrix map(MapFunction function) {
		Matrix result = new Matrix(getRows(), getCols());
		for (int i = 0; i < result.getRows(); i++) {
			for (int j = 0; j < result.getCols(); j++) {
				result.data[i][j] = function.function(get(i, j), i, j);
			}
		}
		return result;
	}

	/**
	 * Funktion, die in der Methode {@link Matrix#map(MapFunction) map} genutzt
	 * wird.
	 */
	public interface MapFunction {
		double function(double x, int i, int j);
	}

	/**
	 * Gibt eine Kopie dieser Matrix zurück.
	 * 
	 * @return Eine Kopie dieser Matrix.
	 */
	public Matrix getCopy() {
		return (Matrix) clone();
	}

	@Override
	protected Object clone() {
		return map((x, i, j) -> x);
	}

	@Override
	public String toString() {
		return Arrays.deepToString(data);
	}

	/**
	 * Erzeugt eine 2-dimensionale Matrix aus dem 2-dimensionalen Array
	 * {@code array}.
	 * 
	 * @param array
	 *            Das 2-dimensionalen Array aus dem eine Matrix erzeugt werden soll.
	 * @return Die 2-dimensionale Matrix.
	 */
	public static Matrix from2DArray(double... array) {
		return new Matrix(array.length, 1).map((x, i, j) -> array[i]);
	}

	/**
	 * Erzeugt eine Matrix aus einem String, der von {@link #toString() toString}
	 * zurück gegeben wurde.
	 * 
	 * @param string
	 *            Ein String, der eine Matrix repräsentiert.
	 * @return Die Matrix, die durch den String repräsentiert wurde.
	 */
	public static Matrix fromString(String string) {
		Pattern pattern = Pattern.compile("\\[([^\\[\\]]*)\\]");
		Matcher matcher = pattern.matcher(string);
		ArrayList<double[]> g = new ArrayList<double[]>();
		int cols = 0;
		while (matcher.find()) {
			String[] s = matcher.group(1).split(", ");
			if (g.size() == 0) {
				cols = s.length;
			} else if (s.length != cols) {
				throw new IllegalArgumentException("Invalid matrix string!");
			}
			double[] d = new double[s.length];
			for (int i = 0; i < s.length; i++) {
				d[i] = Double.parseDouble(s[i]);
			}
			g.add(d);
		}
		return new Matrix(g.size(), cols).map((x, i, j) -> g.get(i)[j]);
	}
}