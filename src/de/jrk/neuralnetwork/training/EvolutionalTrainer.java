package de.jrk.neuralnetwork.training;

import java.util.ArrayList;
import de.jrk.neuralnetwork.NeuralNetwork;

public class EvolutionalTrainer {
	private ArrayList<EvolutionalNeuralNetwork> networks;
	private int keepAmount;
	private double mutationRate;
	private double lastHighscore;

	/**
	 * Erzeugt einen neues Objekt zum evolutionärem Lernen von
	 * {@link NeuralNetwork}s.
	 * 
	 * @param seedNetwork
	 *            Das {@link NeuralNetwork}, das Vorlage dient.
	 * @param networkAmount
	 *            Anzahl der {@link NeuralNetwork}s.
	 * @param keepAmount
	 *            Anzahl der {@link NeuralNetwork}s, die in
	 *            {@link #generateNewNetworks() generateNewNetworks} behalten
	 *            werden.
	 * @param randomize
	 *            Ob die {@link NeuralNetwork}s zu Beginn randomisiert werden
	 *            sollen.
	 */
	public EvolutionalTrainer(NeuralNetwork seedNetwork, int networkAmount, int keepAmount, boolean randomize) {
		if (networkAmount < 2) {
			throw new IllegalArgumentException("The amount of networks must not be less than 2!");
		}
		if (keepAmount >= networkAmount) {
			throw new IllegalArgumentException(
					"The amount of keep networks has to be less than the amount of networks!");
		}
		this.keepAmount = keepAmount;
		networks = new ArrayList<EvolutionalNeuralNetwork>(networkAmount);
		for (int i = 0; i < networkAmount; i++) {
			networks.add(new EvolutionalNeuralNetwork(seedNetwork.getCopy()));
		}
		if (randomize) {
			for (EvolutionalNeuralNetwork neuralNetworkWithScore : networks) {
				neuralNetworkWithScore.getNeuralNetwork().randomize(1);
			}
		}
	}

	/**
	 * Führt eine Iteration aus, in der die {@link NeuralNetwork}s mit dem
	 * {@link NeuralNetworkTester} {@code nnt} getestet und nach Score sortiert
	 * werden und anschließend {@link #generateNewNetworks() generateNewNetworks}
	 * aufgerufen wird.
	 * 
	 * @param nnt
	 *            Der {@link NeuralNetworkTester}, mit dem die
	 *            {@link NeuralNetwork}s getestet werden.
	 * @param useMultiThreading
	 *            Ob Multithreading genutzt werden soll.
	 */
	public void doIteration(NeuralNetworkTester nnt, boolean useMultiThreading) {
		Thread[] threads = new Thread[networks.size()];
		for (int i = 0; i < networks.size(); i++) {
			if (!networks.get(i).tested) {
				int index = i;
				NeuralNetwork nn = networks.get(index).getNeuralNetwork();
				threads[i] = new Thread(() -> {
					networks.get(index).setScore(nnt.test(nn));
				});
				if (useMultiThreading) {
					threads[i].start();
				} else {
					threads[i].run();
				}
			}
		}
		if (useMultiThreading) {
			for (int i = 0; i < threads.length; i++) {
				try {
					if (threads[i] != null) {
						threads[i].join();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		sortNetworks();
		lastHighscore = networks.get(0).getScore();
		generateNewNetworks();
	}

	/**
	 * Die besten {@link #keepAmount} {@link NeuralNetwork}s werden behalten, die
	 * restlichen werden durch eine mutierte Variante von zufällig ausgewählten
	 * behaltenen {@link NeuralNetwork}s ersetzt.
	 */
	private void generateNewNetworks() {
		for (int i = keepAmount; i < networks.size(); i++) {
			int randIndex = (int) (Math.random() * Math.random() * keepAmount);
			networks.set(i, new EvolutionalNeuralNetwork(networks.get(randIndex).getNeuralNetwork().getCopy()));
			networks.get(i).mutate(mutationRate);
		}
	}

	/**
	 * Markiert alle {@link NeuralNetwork}s als ungetestet. Sollte aufgerufen
	 * werden, wenn der Test-Algorithmus geändert wurde.
	 */
	public void resetTested() {
		for (EvolutionalNeuralNetwork evolutionalNeuralNetwork : networks) {
			evolutionalNeuralNetwork.tested = false;
		}
	}

	/**
	 * Sortiert die {@link NeuralNetwork}s absteigend nach Score.
	 */
	private void sortNetworks() {
		networks.sort((n1, n2) -> (int) Math.signum(n2.getScore() - n1.getScore()));
	}

	/**
	 * Gibt den letzten höchsten Score zurück.
	 * 
	 * @return Der letzte höchste Score.
	 */
	public double getHighscore() {
		return lastHighscore;
	}

	/**
	 * Setzt die Rate, mit der die {@link NeuralNetwork}s in
	 * {@link #generateNewNetworks() generateNewNetworks} mutiert werden.
	 * 
	 * @param mutationRate
	 *            Die Mutationsrate.
	 */
	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	/**
	 * Gibt eine Kopie des {@link NeuralNetwork}s mit dem höchsten Score zurück.
	 * 
	 * @return Das beste {@link NeuralNetwork}.
	 */
	public NeuralNetwork getBestNetwork() {
		sortNetworks();
		return networks.get(0).getNeuralNetwork().getCopy();
	}

	/**
	 * Gibt alle {@link NeuralNetwork}s in einer Liste zurück.
	 * 
	 * @return Eine Liste mit allen {@link NeuralNetwork}s.
	 */
	public ArrayList<NeuralNetwork> getNetworks() {
		ArrayList<NeuralNetwork> result = new ArrayList<NeuralNetwork>();
		for (int i = 0; i < networks.size(); i++) {
			result.add(networks.get(i).getNeuralNetwork());
		}
		return result;
	}

	/**
	 * Ein Interface, das in
	 * {@link EvolutionalTrainer#doIteration(NeuralNetworkTester, boolean)
	 * doIteration} verwendet wird, um ein {@link NeuralNetwork} zu testen.
	 */
	public interface NeuralNetworkTester {
		public double test(NeuralNetwork nn);
	}

	/**
	 * Klasse, die ein {@link NeuralNetwork} und einen Score hält.
	 */
	public class EvolutionalNeuralNetwork {
		private final NeuralNetwork neuralNetwork;
		private double score;
		private boolean tested = false;

		public EvolutionalNeuralNetwork(NeuralNetwork neuralNetwork) {
			this.neuralNetwork = neuralNetwork;
		}

		public NeuralNetwork getNeuralNetwork() {
			return neuralNetwork;
		}

		public double getScore() {
			return score;
		}

		public void setScore(double score) {
			this.score = score;
			tested = true;
		}

		/**
		 * Mutiert das {@link NeuralNetwork}.
		 * 
		 * @param mutationRate
		 *            Die Rate, die angibt, wie stark das {@link NeuralNetwork} mutiert
		 *            werden soll.
		 */
		public void mutate(double mutationRate) {
			for (int w = 0; w < neuralNetwork.getWeights().length; w++) {
				neuralNetwork.getWeights()[w] = neuralNetwork.getWeights()[w]
						.map((x, i, j) -> x + ((Math.random() * mutationRate * 2) - mutationRate) * x);
			}
			for (int b = 0; b < neuralNetwork.getBiases().length; b++) {
				neuralNetwork.getBiases()[b] = neuralNetwork.getBiases()[b]
						.map((x, i, j) -> x + ((Math.random() * mutationRate * 2) - mutationRate) * x);
			}
		}
	}
}
