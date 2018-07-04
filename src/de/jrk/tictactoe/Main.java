package de.jrk.tictactoe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import de.jrk.neuralnetwork.NeuralNetwork;
import de.jrk.tictactoe.players.ConsolePlayer;
import de.jrk.tictactoe.players.NeuralNetworkPlayer;

public class Main {
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("train")) {
			train();
		} else if (args.length > 1 && args[0].equals("play")) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(new File(args[1])));
				for (int i = 0; i < Integer.parseInt(args[2]) - 1; i++) {
					br.readLine();
				}
				play(br.readLine());
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
				printUsage();
				System.exit(0);
			}
		} else {
			printUsage();
		}
	}
	
	public static void train() {
		new Training().train();
	}
	
	public static void play(String networkString) {
		TicTacToe ticTacToe = new TicTacToe();
		ticTacToe.setPlayer1(new ConsolePlayer());
		ticTacToe.setPlayer2(new NeuralNetworkPlayer(NeuralNetwork.fromString(networkString)));
		ticTacToe.run();
	}

	public static void printUsage() {
		System.out.println("Usage: Use \"train\" to train the network or \"play <networkfile> <network (1-20)>\" to play against the neural network.");
	}
}
