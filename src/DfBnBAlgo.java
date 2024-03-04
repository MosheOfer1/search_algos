import java.util.*;

public class DfBnBAlgo implements Algorithm {
    int iterationNum = 1;
    @Override
    public Solution solve_puzzle(Node start_node, boolean open) {
        Stack<Node> open_list_ST = new Stack<>();
        Hashtable<String ,Node> open_list_HT = new Hashtable<>();

        Solution result = null;
        int N = start_node.getN();
        int M = start_node.getM();

        int threshold = Math.min(Integer.MAX_VALUE, UtilityClass.factorial(N * M - 1));
        int num_of_nodes = 0;
        long start_time = System.currentTimeMillis();

        Node n = new Node(start_node);
        open_list_ST.add(n);
        open_list_HT.put(Arrays.deepToString(n.tiles), n);

        while (!open_list_ST.isEmpty()) {
            // Debug
            if (open)
                UtilityClass.printOpenListConfiguration(open_list_ST);

            n = open_list_ST.pop();

            // Loop avoidance check
            if (n.isMarkAsOut()) {
                open_list_HT.remove(Arrays.deepToString(n.tiles));
            }
            else {
                n.markAsOut();
                open_list_ST.add(n);

                // List of all the possible operators
                List<Node> children = new ArrayList<>();

                for (UtilityClass.Direction direction : UtilityClass.Direction.values()) {
                    Node child = UtilityClass.expand_node(n, direction, iterationNum);

                    if (child != null)
                        children.add(child);
                }

                iterationNum++;

                // Sort in increasing order by the F value
                children.sort(Comparator.comparingInt(Node::F));

                //int number_of_children = children.size();
                for (int i = 0; i < children.size(); i++) {
                    Node child = children.get(i);
                    num_of_nodes++;

                    if (child.F() >= threshold) {
                        // Remove child and all the nodes after it from children
                        children.subList(i , children.size()).clear();
                        break;
                    }

                    // Re-expand avoidance
                    else if (open_list_HT.containsKey(Arrays.deepToString(child.tiles))) {
                        Node n_in_open_list = open_list_HT.get(Arrays.deepToString(child.tiles));
                        if (n_in_open_list.isMarkAsOut()) {
                            children.remove(child);
                            i--;
                        }
                        else {
                            if (n_in_open_list.F() <= child.F()) {
                                children.remove(child);
                                i--;
                            } else {
                                open_list_ST.remove(n_in_open_list);
                                open_list_HT.remove(Arrays.deepToString(n_in_open_list.tiles));
                            }
                        }
                    }

                    // If we reached here, f(g) < t
                    // Check for goal
                    else if (child.isGoal()) {
                        threshold = child.F();
                        result = UtilityClass.path(child, num_of_nodes, child.F(), start_time);
                        System.out.println(result);
                        System.out.println(threshold);
                        // Remove child and all the nodes after it from children
                        children.subList(i + 1, children.size()).clear();
                    }
                }

                // Insert children in a reverse order to the Stack and the HashTable
                for (int i = children.size() - 1; i >= 0; i--) {
                    Node node = children.get(i);
                    open_list_ST.add(node);
                    open_list_HT.put(Arrays.deepToString(node.tiles), node);
                }
            }
        }
        return result;
    }
}
