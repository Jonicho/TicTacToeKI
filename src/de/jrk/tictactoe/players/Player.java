package de.jrk.tictactoe.players;

import de.jrk.tictactoe.TicTacToe;

/**
 * Ein Spieler für {@link TicTacToe}
 */
public abstract class Player {
	/**
	 * Wird am Anfang jedes Spiels aufgerufen.
	 * 
	 * @param firstPlayer
	 *            Gibt an, ob dieser Spieler der erste Spieler ist.
	 */
	public abstract void init(boolean firstPlayer);

	/**
	 * Wird in jedem Zug aufgerufen.
	 * 
	 * @param field
	 *            Das Spielfeld.
	 * @return Ein Array mit den Koordinaten, wo dieser Spieler hinsetzen soll.
	 */
	public abstract int[] turn(int[][] field);

	/**
	 * Wird am Ende jedes Spiels aufgerufen.
	 * 
	 * @param winPlayer
	 *            Die Nummer des Spielers, der gewonnen hat.
	 * @param field
	 *            Das Spielfeld.
	 */
	public abstract void finish(int winPlayer, int[][] field);
}
