import java.util.*;

public class IDAStarAlgo implements Algorithm {
    int iterationNum = 1;
    @Override
    public Solution solve_puzzle(Node start_node, boolean open) {
        Stack<Node> open_list_ST = new Stack<>();
        Hashtable<Node, Node> open_list_HT = new Hashtable<>();

        int threshold = start_node.H();
        int num_of_nodes = 0;
        long start_time = System.currentTimeMillis();

        while (threshold != Integer.MAX_VALUE) {
            int minF = Integer.MAX_VALUE;
            Node n = new Node(start_node);
            open_list_ST.add(n);
            open_list_HT.put(n, n);
            while (!open_list_ST.isEmpty()) {
                // Debug
                if (open)
                    UtilityClass.printOpenListConfiguration(open_list_ST);

                n = open_list_ST.pop();

                // Loop avoidance check
                if (n.isMarkAsOut()) {
                    open_list_HT.remove(n);
                }

                else {
                    n.markAsOut();
                    open_list_ST.add(n);

                    // Iterate all the possible operators
                    for (UtilityClass.Direction direction : UtilityClass.Direction.values()) {
                        Node child = UtilityClass.expand_node(n, direction, iterationNum);

                        if (child == null)
                            continue;
                        num_of_nodes++;

                        if (child.F() > threshold) {
                            minF = Math.min(minF, child.F());
                            continue;
                        }
                        // Re-expand avoidance
                        if (open_list_HT.containsKey(child)) {
                            Node n_in_open_list = open_list_HT.get(child);
                            if (n_in_open_list.F() > child.F()) {
                                open_list_ST.remove(n_in_open_list);
                                open_list_HT.remove(n_in_open_list);
                            }
                            else {
                                continue;
                            }
                        }

                        // Check for goal
                        if (child.isGoal()) {
                            return UtilityClass.path(child, num_of_nodes, child.F(), start_time);
                        }

                        open_list_ST.add(child);
                        open_list_HT.put(child, child);
                    }
                    iterationNum++;
                }
            }
            threshold = minF;
        }
        return null;
    }
}
