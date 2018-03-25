package de.jrk.tictactoe.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.jrk.tictactoe.players.Player;

public class ConsolePlayer extends Player {
	private boolean firstPlayer;

	@Override
	public void init(boolean firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	@Override
	public int[] turn(int[][] field) {
		for (int j = 0; j < field[0].length; j++) {
			for (int i = 0; i < field.length; i++) {
				if (i != 0) {
					System.out.print(" ");
				}
				System.out.print(field[i][j]);
			}
			System.out.println();
		}
		System.out.println("Player " + (firstPlayer ? "1" : "2") + ", type pos:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line[] = null;
		try {
			line = br.readLine().split(",");
		} catch (IOException e) {
			e.printStackTrace();
		}
		int[] result = { Integer.parseInt(line[0].trim()), Integer.parseInt(line[1].trim()) };
		return result;
	}

	@Override
	public void finish(int winPlayer, int[][] field) {
		for (int j = 0; j < field[0].length; j++) {
			for (int i = 0; i < field.length; i++) {
				if (i != 0) {
					System.out.print(" ");
				}
				System.out.print(field[i][j]);
			}
			System.out.println();
		}
		System.out.println("Player " + winPlayer + " won!");
	}
}
