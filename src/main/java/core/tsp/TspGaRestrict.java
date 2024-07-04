package core.tsp;
import core.io.ConfigDAO;
import java.io.IOException;
import java.util.*;

public class TspGaRestrict extends RestrictionChecker {

    private long workTime;
    private int bestTourCost;
    private List<Integer> bestRoute;
    private final int tournamentSize;
    private final int[][] distanceMatrix;
    private final int populationSize;
    private final int numGenerations;
    private final double mutationRate;
    private final int elitismCount;
    private final int maxStagnation;
    private int currentStagnation;
    private final int startCity;
    private final int numTraveler;
    private final boolean isRestrictionNeeded;
    public long getWorkTime() {
        return workTime;
    }
    public int getBestTourCost() {
        return bestTourCost;
    }
    public List<Integer> getBestRoute() {
        return bestRoute;
    }

    public TspGaRestrict(int[][] distanceMatrix) throws IOException {
        ConfigDAO dao = new ConfigDAO();
        this.populationSize = dao.getPopulationSize();
        this.numGenerations = dao.getNumGenerations();
        this.mutationRate = dao.getMutationRate();
        this.elitismCount = dao.getElitismCount();
        this.maxStagnation = dao.geMaxStagnation();
        this.tournamentSize = dao.getTournamentSize();
        this.startCity = dao.getStartPoint();
        this.numTraveler = dao.getNumOfTraveler();
        this.distanceMatrix = distanceMatrix;
        this.isRestrictionNeeded = dao.isRestrictionsNeeded();
        this.currentStagnation = 0;
        runGA();
    }
    public List<Integer> solveTSP() {
        int numCities = distanceMatrix.length;
        List<List<Integer>> population = generateRandomPopulation(numCities);
        int lastBestDistance = calculateTotalDistance(Collections.min(population, (a1, b1) -> calculateTotalDistance(a1) - calculateTotalDistance(b1)));
        for (int generation = 0; generation < numGenerations; generation++) {
            population.sort((a, b) -> calculateTotalDistance(a) - calculateTotalDistance(b));
            int currentBestDistance = calculateTotalDistance(population.get(0));

            if (currentBestDistance == lastBestDistance) {
                currentStagnation++;
                if (currentStagnation >= maxStagnation) {
                    break;
                }
            } else {
                lastBestDistance = currentBestDistance;
                currentStagnation = 0;
            }

            List<List<Integer>> elitePopulation = new ArrayList<>(population.subList(0, elitismCount));
            List<List<Integer>> parents = new ArrayList<>();
            List<Integer> parent1;
            List<Integer> parent2;
            for (int i = 0; i < (populationSize - elitismCount); i++) {
                if (isRestrictionNeeded){
                    do {
                        parent1 = tournamentSelection(population);
                        parent2 = tournamentSelection(population);
                    } while (!(RestrictionChecker.checkRoute(parent1) && RestrictionChecker.checkRoute(parent2)));

                } else {
                    parent1 = tournamentSelection(population);
                    parent2 = tournamentSelection(population);
                }
                parents.add(parent1);
                parents.add(parent2);
            }
            List<List<Integer>> newPopulation = new ArrayList<>(elitePopulation);
            List<List<Integer>> children;
            for (int i = 0; i < parents.size(); i += 2) {
                if (isRestrictionNeeded){
                    do {
                        parent1 = parents.get(i);
                        parent2 = parents.get(i + 1);
                        children = crossover(parent1, parent2);
                        mutate(children.get(0));
                        mutate(children.get(1));
                    } while (!(RestrictionChecker.checkRoute(children.get(0)) && RestrictionChecker.checkRoute(children.get(1))));
                } else {
                    parent1 = parents.get(i);
                    parent2 = parents.get(i + 1);
                    children = crossover(parent1, parent2);
                    mutate(children.get(0));
                    mutate(children.get(1));
                }
                newPopulation.add(children.get(0));
                newPopulation.add(children.get(1));
            }
            population = newPopulation;
        }
        return Collections.min(population, (a, b) -> calculateTotalDistance(a) - calculateTotalDistance(b));
    }

    private List<List<Integer>> generateRandomPopulation(int numCities) {
        List<List<Integer>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<Integer> individual;
            if (isRestrictionNeeded){
                do {
                    individual = generateRandomRoute(numCities);
                } while (!RestrictionChecker.checkRoute(individual));
                population.add(individual);

            } else {
                individual = generateRandomRoute(numCities);
                population.add(individual);
            }
        }
        return population;
    }

    private List<Integer> generateRandomRoute(int numCities) {
        List<Integer> route = new ArrayList<>();
        for (int i = 1; i <= numCities; i++) {
            if (i != startCity) {
                route.add(i);
            }
        }
        Collections.shuffle(route);
        route.add(0, startCity);
        route.add(startCity);
        return route;
    }

    public int calculateTotalDistance(List<Integer> path) {
        int totalDistance = 0;
        int size = path.size();
        int from = path.get(0) - 1;

        for (int i = 1; i < size; i++) {
            int to = path.get(i) - 1;
            totalDistance += distanceMatrix[from][to];
            from = to;
        }
        totalDistance += distanceMatrix[from][path.get(0) - 1];
        return totalDistance;
    }

    private List<Integer> tournamentSelection(List<List<Integer>> population) {
        List<List<Integer>> tournament = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }
        return Collections.min(tournament, (a, b) -> calculateTotalDistance(a) - calculateTotalDistance(b));
    }

    private List<List<Integer>> crossover(List<Integer> parent1, List<Integer> parent2) {
        int size = parent1.size();
        Random random = new Random();
        List<List<Integer>> children = new ArrayList<>();
        int start = random.nextInt(size - 1) + 1; // Don't change the first element
        int index = start;
        List<Integer> child1 = new ArrayList<>(Collections.nCopies(size, -1));
        List<Integer> child2 = new ArrayList<>(Collections.nCopies(size, -1));
        int iterations = 0;

        do {
            child1.set(index, parent1.get(index));
            child2.set(index, parent2.get(index));
            index = parent2.indexOf(parent1.get(index));
            iterations++;
            if (iterations > size) {
                break;
            }
        } while (index != start);

        for (int i = 0; i < size; i++) {
            if (child1.get(i) == -1) {
                child1.set(i, parent2.get(i));
            }
            if (child2.get(i) == -1) {
                child2.set(i, parent1.get(i));
            }
        }
        child1.remove(0);
        child1.add(0, startCity);
        child1.remove(child1.size() - 1);
        child1.add(startCity);
        child2.remove(0);
        child2.add(0, startCity);
        child2.remove(child2.size() - 1);
        child2.add(startCity);
        children.add(child1);
        children.add(child2);
        return children;
    }

    private void mutate(List<Integer> individual) {
        if (Math.random() < mutationRate) {
            int size = individual.size();
            Random random = new Random();

            int pos1 = 1 + random.nextInt(size - 2); // Don't change the first element
            int pos2;
            do {
                pos2 = 1 + random.nextInt(size - 2); // Don't change the last element
            } while (pos1 == pos2);

            if (pos1 > pos2) {
                int temp = pos1;
                pos1 = pos2;
                pos2 = temp;
            }
            while (pos1 < pos2) {
                int temp = individual.get(pos1);
                individual.set(pos1, individual.get(pos2));
                individual.set(pos2, temp);
                pos1++;
                pos2--;
            }
        }
    }

    public void runGA() {
        long time = System.nanoTime();
        bestRoute = solveTSP();
        time = System.nanoTime() - time;
        bestTourCost = calculateTotalDistance(bestRoute);
        for (int i = 0; i < bestRoute.size(); i++) {
            if (i > distanceMatrix.length - numTraveler + 1) {
                int index = bestRoute.indexOf(i);
                bestRoute.remove(index);
                bestRoute.add(index, startCity);
            }
        }
        workTime = time;
    }
}
