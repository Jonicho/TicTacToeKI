package de.jrk.tictactoe;

import de.jrk.tictactoe.players.Player;

public class TicTacToe implements Runnable {
	private int[][] field;
	private Player player1;
	private Player player2;
	private boolean started;
	private boolean startPlayer;

	/**
	 * Setzt Spieler 1.
	 * 
	 * @param player
	 */
	public void setPlayer1(Player player) {
		if (!started) {
			player1 = player;
		}
	}

	/**
	 * Setzt Spieler 2.
	 * 
	 * @param player
	 */
	public void setPlayer2(Player player) {
		if (!started) {
			player2 = player;
		}
	}

	/**
	 * Setzt den Startspieler. {@code true} steht für Spieler 1, {@code false} für
	 * Spieler 2.
	 * 
	 * @param startPlayer
	 */
	public void setStartPlayer(boolean startPlayer) {
		this.startPlayer = startPlayer;
	}

	/**
	 * Führt eine Runde des Spiels Tic-Tac-Toe aus.
	 */
	@Override
	public void run() {
		started = true;
		field = new int[3][3];
		player1.init(true);
		player2.init(false);
		boolean currentPlayer = startPlayer;
		int winPlayer;
		while ((winPlayer = getWinPlayer()) == 0 && !isFieldFull()) {
			int[] pos = (currentPlayer ? player1 : player2).turn(getFieldCopy());
			if (field[pos[0]][pos[1]] == 0) {
				field[pos[0]][pos[1]] = currentPlayer ? 1 : 2;
			} else {
				winPlayer = currentPlayer ? 2 : 1;
				break;
			}
			currentPlayer = !currentPlayer;
		}
		player1.finish(winPlayer, field);
		player2.finish(winPlayer, field);
		started = false;
	}

	/**
	 * Gibt eine Kopie des aktuellen Spielfeldes zurück.
	 * 
	 * @return Eine Kopie des aktuellen Spielfeldes.
	 */
	private int[][] getFieldCopy() {
		int[][] result = new int[3][3];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = field[i][j];
			}
		}
		return result;
	}

	/**
	 * Berechnet welcher Spieler gewonnen hat. Gibt {@code 0} zurück, wenn (noch)
	 * kein Spieler gewonnen hat.
	 * 
	 * @return Die Nummer des Spielers, der gewonnen hat.
	 */
	private int getWinPlayer() {
		int currentPlayer = field[0][0];
		if (currentPlayer != 0 && field[1][1] == currentPlayer && field[2][2] == currentPlayer) {
			return currentPlayer;
		}
		if (currentPlayer != 0 && field[0][1] == currentPlayer && field[0][2] == currentPlayer) {
			return currentPlayer;
		}
		if (currentPlayer != 0 && field[1][0] == currentPlayer && field[2][0] == currentPlayer) {
			return currentPlayer;
		}
		currentPlayer = field[2][0];
		if (currentPlayer != 0 && field[1][1] == currentPlayer && field[0][2] == currentPlayer) {
			return currentPlayer;
		}
		if (currentPlayer != 0 && field[2][1] == currentPlayer && field[2][2] == currentPlayer) {
			return currentPlayer;
		}
		currentPlayer = field[2][2];
		if (currentPlayer != 0 && field[1][2] == currentPlayer && field[0][2] == currentPlayer) {
			return currentPlayer;
		}
		currentPlayer = field[0][1];
		if (currentPlayer != 0 && field[1][1] == currentPlayer && field[2][1] == currentPlayer) {
			return currentPlayer;
		}
		currentPlayer = field[1][0];
		if (currentPlayer != 0 && field[1][1] == currentPlayer && field[1][2] == currentPlayer) {
			return currentPlayer;
		}
		return 0;
	}

	/**
	 * Berechnet, ob das Spielfeld voll ist.
	 * 
	 * @return Ob das Spielfeld voll ist.
	 */
	private boolean isFieldFull() {
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				if (field[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}
}
