package de.jrk.tictactoe.players;

import de.jrk.neuralnetwork.Matrix;
import de.jrk.neuralnetwork.NeuralNetwork;

public class NeuralNetworkPlayer extends Player {
	private boolean firstPlayer;
	private int wins = 0;
	private int loses = 0;
	private int draws = 0;
	private int ills = 0;
	public NeuralNetwork nn;

	/**
	 * Konstruiert einen neuen Spieler auf Basis des übergebenen
	 * {@link NeuralNetwork} {@code nn};
	 * 
	 * @param nn
	 *            Das {@link NeuralNetwork}.
	 */
	public NeuralNetworkPlayer(NeuralNetwork nn) {
		this.nn = nn;
	}

	@Override
	public void init(boolean firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	@Override
	public int[] turn(int[][] field) {
		double[] inputs = new double[9];
		int a = 0;
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				int fieldPart = field[i][j];
				inputs[a] = 0;
				if (fieldPart == 0) {
					a++;
					continue;
				} else {
					inputs[a] = (fieldPart == 1 && firstPlayer) || (fieldPart == 2 && !firstPlayer) ? 1 : -1;
					a++;
				}
			}
		}
		Matrix outputs = nn.feedforward(Matrix.from2DArray(inputs));
		int[] pos = null;
		double posProp = -1;
		int p = 0;
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				if (outputs.get(p, 0) > posProp && (field[i][j] == 0)) {
					pos = new int[] { i, j };
					posProp = outputs.get(p, 0);
				}
				p++;
			}
		}
		if (field[pos[0]][pos[1]] != 0) {
			ills++;
		}
		return pos;
	}

	/**
	 * @return Wie oft dieser Spieler seit der Instantiierung bzw. dem letztem
	 *         Aufruf von {@link #resetRecord() resetRecord} gewonnen hat.
	 */
	public int getWins() {
		return wins;
	}

	/**
	 * @return Wie oft dieser Spieler seit der Instantiierung bzw. dem letztem
	 *         Aufruf von {@link #resetRecord() resetRecord} verloren hat.
	 */
	public int getLoses() {
		return loses;
	}

	/**
	 * @return Wie oft dieser Spieler seit der Instantiierung bzw. dem letztem
	 *         Aufruf von {@link #resetRecord() resetRecord} unentschieden gespielt
	 *         hat.
	 */
	public int getDraws() {
		return draws;
	}

	/**
	 * @return Wie oft dieser Spieler seit der Instantiierung bzw. dem letztem
	 *         Aufruf von {@link #resetRecord() resetRecord} einen illegalen Zug
	 *         gemacht hat hat.
	 */
	public int getIlls() {
		return ills;
	}

	/**
	 * Setzt die Statistiken zurück.
	 */
	public void resetRecord() {
		wins = 0;
		loses = 0;
		draws = 0;
		ills = 0;
	}

	@Override
	public void finish(int winPlayer, int[][] field) {
		if ((winPlayer == 1 && firstPlayer) || (winPlayer == 2 && !firstPlayer)) {
			wins++;
		} else if (winPlayer != 0) {
			loses++;
		} else {
			draws++;
		}
	}

}
