import java.util.*;

public class AStarAlgo implements Algorithm {
    int iterationNum = 1;
    @Override
    public Solution solve_puzzle(Node start_node, boolean open) {
        PriorityQueue<Node> open_list_PQ = new PriorityQueue<>();
        Hashtable<Node, Node> open_list_HT = new Hashtable<>();
        HashSet<Node> close_list_hash_set = new HashSet<>();

        int num_of_nodes = 0;
        long start_time = System.currentTimeMillis();

        open_list_PQ.add(start_node);
        open_list_HT.put(start_node, start_node);
        close_list_hash_set.add(start_node);

        while (!open_list_PQ.isEmpty()) {
            // Debug
            if (open)
                UtilityClass.printOpenListConfiguration(open_list_PQ);

            Node n = open_list_PQ.poll();
            assert n != null;

            if (n.F() == Integer.MAX_VALUE)
                return null;

            open_list_HT.remove(n);
            close_list_hash_set.add(n);

            // Found the goal
            if (n.isGoal()) {
                return UtilityClass.path(n, num_of_nodes, n.F(), start_time);
            }

            // Iterate all the possible operators
            for (UtilityClass.Direction direction : UtilityClass.Direction.values()) {
                Node child = UtilityClass.expand_node(n, direction, iterationNum);

                if (child == null)
                    continue;
                num_of_nodes++;
                // Check for loop avoidance
                if (close_list_hash_set.contains(child))
                    continue;

                // Check for re-expand node
                Node n_in_open = open_list_HT.get(child);
                if (n_in_open != null)
                    if (n_in_open.G() < child.G())
                        continue;
                    else {
                        open_list_HT.remove(child);
                        open_list_PQ.remove(n_in_open);
                    }

                open_list_PQ.add(child);
            }
            iterationNum++;
        }
        // No path was found
       return null;
    }

}
