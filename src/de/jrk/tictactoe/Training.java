package de.jrk.tictactoe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import de.jrk.neuralnetwork.ActivationFunction;
import de.jrk.neuralnetwork.NeuralNetwork;
import de.jrk.neuralnetwork.training.EvolutionalTrainer;
import de.jrk.neuralnetwork.training.EvolutionalTrainer.NeuralNetworkTester;
import de.jrk.tictactoe.players.NeuralNetworkPlayer;

public class Training {
	private File saveFile = new File("nets" + System.currentTimeMillis() / 1000 + ".txt");
	private ArrayList<EvolutionalTrainer> evolutionalTrainers;
	private int evolutionalTrainerIndex;

	/**
	 * Trainiert 20 Gruppen á 10 {@link NeuralNetwork}s, Tic-Tac-Toe zu spielen.
	 * Speichert nach jeder Iteration das jeweils Beste {@link NeuralNetwork} aus
	 * jeder Gruppe in eine Datei.
	 */
	public void train() {
		NeuralNetwork n = new NeuralNetwork(ActivationFunction.SOFTSIGN_NORM, 9, 18, 18, 9);
		evolutionalTrainers = new ArrayList<EvolutionalTrainer>();
		for (int i = 0; i < 20; i++) {
			evolutionalTrainers.add(new EvolutionalTrainer(n, 10, 5, true));
			evolutionalTrainers.get(i).setMutationRate(0.2);
		}
		int wholeIterations = 0;
		while (true) {
			if (evolutionalTrainerIndex == 0) {
				wholeIterations++;
			}
			System.out.println("Training " + evolutionalTrainerIndex + " at iteration " + wholeIterations);
			EvolutionalTrainer evolutionalTrainer = evolutionalTrainers.get(evolutionalTrainerIndex);
			ArrayList<NeuralNetwork> opponentNetworks = getOpponentNetworks();
			evolutionalTrainer.resetTested();
			for (int i = 0; i < 100; i++) {
				evolutionalTrainer.doIteration((nn) -> getNeuralNetworkTester(opponentNetworks).test(nn), true);
			}
			if (evolutionalTrainerIndex == evolutionalTrainers.size() - 1) {
				saveBestNetworks();
			}
			evolutionalTrainerIndex = ++evolutionalTrainerIndex % evolutionalTrainers.size();
		}
	}

	/**
	 * Gibt einen {@link NeuralNetworkTester} zurück, um zu testen, wie gut ein
	 * {@link NeuralNetwork} gegen die {@link NeuralNetwork}s
	 * {@code opponentNetworks} ist.
	 * 
	 * @param opponentNetworks
	 *            Die {@link NeuralNetwork}s gegen die das zu testende
	 *            {@link NeuralNetwork} spielt.
	 * @return Der {@link NeuralNetworkTester}.
	 */
	private NeuralNetworkTester getNeuralNetworkTester(ArrayList<NeuralNetwork> opponentNetworks) {
		return new NeuralNetworkTester() {
			@Override
			public double test(NeuralNetwork nn) {
				TicTacToe ttt = new TicTacToe();
				NeuralNetworkPlayer nnp = new NeuralNetworkPlayer(nn);
				ttt.setPlayer1(nnp);
				for (int i = 0; i < opponentNetworks.size(); i++) {
					ttt.setPlayer2(new NeuralNetworkPlayer(opponentNetworks.get(i)));
					ttt.setStartPlayer(i % 2 == 0);
					ttt.run();
				}
				double score = (nnp.getWins() + nnp.getDraws() * 0.5 - nnp.getIlls())
						/ (double) opponentNetworks.size();
				return score;
			}
		};
	}

	/**
	 * Gibt die Gegner-{@link NeuralNetwork}s in einer Liste zurück.
	 * 
	 * @return Die Gegner-{@link NeuralNetwork}s in einer Liste.
	 */
	private ArrayList<NeuralNetwork> getOpponentNetworks() {
		ArrayList<NeuralNetwork> opponentNetworks = new ArrayList<NeuralNetwork>();
		for (int i = 0; i < evolutionalTrainers.size(); i++) {
			if (i == evolutionalTrainerIndex)
				continue;
			opponentNetworks.addAll(evolutionalTrainers.get(i).getNetworks().subList(0, 2));
		}
		Collections.shuffle(opponentNetworks);
		return opponentNetworks;
	}

	/**
	 * Speichert das jeweils Beste {@link NeuralNetwork} aus jeder Gruppe in eine
	 * Datei.
	 */
	private void saveBestNetworks() {
		try {
			FileWriter fw = new FileWriter(saveFile);
			String s = "";
			for (EvolutionalTrainer evt : evolutionalTrainers) {
				s += evt.getBestNetwork().toString() + "\n";
			}
			fw.write(s);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
