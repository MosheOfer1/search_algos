import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class DFIDAlgo implements Algorithm {
    HashSet<Node> open_list_HS = new HashSet<>();
    int num_of_nodes = 0;
    int iterationNum = 1;
    long start_time;
    boolean open;
    boolean isCutOff;

    @Override
    public Solution solve_puzzle(Node start_node, boolean open) {
        this.open = open;
        start_time = System.currentTimeMillis();
        int i = 1;
        while (true) {
            Node n = new Node(start_node);
            open_list_HS.clear();
            Solution solution = recursive_limited_DFS(n, i++);
            if (!isCutOff)
                return solution;
        }
    }


    private Solution recursive_limited_DFS(Node n, int limit) {
        if (n.isGoal())
            return UtilityClass.path(n, num_of_nodes, n.F(), start_time);
        else if (limit == 0) {
            isCutOff = true;
            return null;
        }
        else {
            open_list_HS.add(n);
            isCutOff = false;

            // Debug
            if (open) {
                UtilityClass.printOpenListConfiguration(open_list_HS);
                System.out.println("Num: "+num_of_nodes);
                System.out.println("Depth: "+limit);
            }

            Node child;
            for (UtilityClass.Direction direction : UtilityClass.Direction.values()) {
                child = UtilityClass.expand_node(n, direction, iterationNum);

                if (child == null)
                    continue;

                num_of_nodes++;

                if (open_list_HS.contains(child))
                    continue;

                Solution result = recursive_limited_DFS(child, limit - 1);

                if (result != null)
                    return result;
            }
            iterationNum++;
            open_list_HS.remove(n);

            // Failed or Cutoff
            return null;
        }
    }
}
