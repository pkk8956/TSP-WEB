package core.tsp;
import core.io.ConfigDAO;
import java.io.IOException;
import java.util.*;

public class TspBnbRestrict extends RestrictionChecker {
    private long workTime;
    private final int numTraveler;
    private final boolean isRestrictionNeeded;
    private final int startCity;
    private final int N;
    private final int[] final_path;
    private final boolean[] visited;
    private int final_res = Integer.MAX_VALUE;

    public TspBnbRestrict(int[][] costMatrix) throws IOException {
        ConfigDAO dao = new ConfigDAO();
        this.numTraveler = dao.getNumOfTraveler();
        this.isRestrictionNeeded = dao.isRestrictionsNeeded();
        this.startCity = dao.getStartPoint() - 1;
        this.N = costMatrix.length;
        this.final_path = new int[N + 1];
        this.visited = new boolean[N];
        TSP(costMatrix);
    }

    public List<Integer> getFinal_path() {
        List<Integer> finalRoute = new ArrayList<>(Arrays.stream(final_path)
                .boxed().toList());
        finalRoute.replaceAll(integer -> integer + 1);
        for (int i = 0; i < finalRoute.size(); i++) {
            if (i > N - numTraveler + 1) {
                int index = finalRoute.indexOf(i);
                finalRoute.remove(index);
                finalRoute.add(index, startCity + 1);
            }
        }
        return finalRoute;
    }
    private List<Integer> arrayAsList(int[] path){
        List<Integer> rote = new ArrayList<>(Arrays.stream(path)
                .boxed().toList());
        rote.replaceAll(integer -> integer + 1);
        return rote;
    }

    public long getWorkTime() {
        return workTime;
    }

    public int getFinal_res() {
        return final_res;
    }

    private void copyToFinal(int[] curr_path) {
        if (N >= 0) System.arraycopy(curr_path, 0, final_path, 0, N);
        final_path[N] = curr_path[0];
    }

    int firstMin(int[][] adj, int i) {
        int min = Integer.MAX_VALUE;
        for (int k = 0; k < N; k++)
            if (adj[i][k] < min && i != k)
                min = adj[i][k];
        return min;
    }

    private int secondMin(int[][] adj, int i) {
        int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
        for (int j=0; j<N; j++)
        {
            if (i == j)
                continue;

            if (adj[i][j] <= first)
            {
                second = first;
                first = adj[i][j];
            }
            else if (adj[i][j] <= second &&
                    adj[i][j] != first)
                second = adj[i][j];
        }
        return second;
    }
    private void TSPRec(int[][] adj, int curr_bound, int curr_weight, int level, int[] curr_path) {
        if (level == N)
        {
            if (adj[curr_path[level - 1]][curr_path[0]] != 0)
            {
                int curr_res = curr_weight +
                        adj[curr_path[level-1]][curr_path[0]];
                if (isRestrictionNeeded){
                    if (RestrictionChecker.checkRoute(arrayAsList(curr_path))){
                        if (curr_res < final_res)
                        {
                            copyToFinal(curr_path);
                            final_res = curr_res;
                        }
                    }
                } else {
                    if (curr_res < final_res)
                    {
                        copyToFinal(curr_path);
                        final_res = curr_res;
                    }
                }

            }
            return;
        }
        for (int i = 0; i < N; i++)
        {
            if (adj[curr_path[level-1]][i] != 0 && !visited[i]) {
                int temp = curr_bound;
                curr_weight += adj[curr_path[level - 1]][i];
                if (level==1)
                    curr_bound -= ((firstMin(adj, curr_path[level - 1]) +
                            firstMin(adj, i))/2);
                else
                    curr_bound -= ((secondMin(adj, curr_path[level - 1]) +
                            firstMin(adj, i))/2);
                if (curr_bound + curr_weight < final_res)
                {
                    curr_path[level] = i;
                    visited[i] = true;
                    TSPRec(adj, curr_bound, curr_weight, level + 1,
                            curr_path);
                }
                curr_weight -= adj[curr_path[level-1]][i];
                curr_bound = temp;
                Arrays.fill(visited,false);
                for (int j = 0; j <= level - 1; j++)
                    visited[curr_path[j]] = true;
            }
        }
    }

    private void TSP(int[][] adj) {
        long startTime = System.nanoTime();
        int[] curr_path = new int[N + 1];
        curr_path[0] = startCity;
        visited[startCity] = true;
        int curr_bound = 0;
        Arrays.fill(curr_path, -1);
        Arrays.fill(visited, false);
        for (int i = 0; i < N; i++) {
            if (i != startCity) {
                curr_bound += (firstMin(adj, i) + secondMin(adj, i));
            }
        }
        curr_bound = (curr_bound == 1) ? 1 : curr_bound / 2;
        curr_path[0] = startCity;
        visited[startCity] = true;
        TSPRec(adj, curr_bound, 0, 1, curr_path);
        workTime = System.nanoTime() - startTime;
    }
}