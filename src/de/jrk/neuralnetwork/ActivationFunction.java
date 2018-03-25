package de.jrk.neuralnetwork;

public class ActivationFunction {
	private ActivationFunction() {
	}

	/**
	 * Eine Aktivierungsfunktion.<br>
	 * Folgende Aktivierungsfunktionen sind möglich:<br>
	 * <br>
	 * {@link #IDENTITY}: <code>&phi;(x)=x</code><br>
	 * {@link #SIGMOID}: <code>&phi;(x)=1/(1+e^x)</code><br>
	 * {@link #TANH}: <code>&phi;(x)=tanh(x)</code><br>
	 * {@link #SOFTSIGN}: <code>&phi;(x)=x/(1+|x|)</code><br>
	 * {@link #SOFTSIGN_NORM}: <code>&phi;(x)=0.5*x/(1+|x|)+0.5</code><br>
	 */
	public static final String IDENTITY = "identity", SIGMOID = "sigmoid", TANH = "tanh", SOFTSIGN = "softsign",
			SOFTSIGN_NORM = "softsign_norm";

	/**
	 * Gibt das Ergebnis der Aktivierungsfunktion {@code function} zurück.<br>
	 * Folgende Aktivierungsfunktionen sind möglich:<br>
	 * <br>
	 * {@link #IDENTITY}: <code>&phi;(x)=x</code><br>
	 * {@link #SIGMOID}: <code>&phi;(x)=1/(1+e^x)</code><br>
	 * {@link #TANH}: <code>&phi;(x)=tanh(x)</code><br>
	 * {@link #SOFTSIGN}: <code>&phi;(x)=x/(1+|x|)</code><br>
	 * {@link #SOFTSIGN_NORM}: <code>&phi;(x)=0.5*x/(1+|x|)+0.5</code><br>
	 * 
	 * @param function
	 *            Die Aktivierungsfunktion.
	 * @param x
	 *            Das x.
	 * @return Das Ergebnis.
	 */
	public static double function(String function, double x) {
		switch (function) {
		case IDENTITY:
			return x;
		case SIGMOID:
			return 1 / (1 + Math.exp(-x));
		case TANH:
			return Math.tanh(x);
		case SOFTSIGN:
			return x / (1 + Math.abs(x));
		case SOFTSIGN_NORM:
			return 0.5 * x / (1 + Math.abs(x)) + 0.5;
		default:
			throw new IllegalArgumentException("Activation function \"" + function + "\" does not exist!");
		}
	}
}
