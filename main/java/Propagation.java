import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
public class Propagation {
    Graph graph;

    public Propagation(Graph graph) {
        this.graph = graph;
    }

    public double dispersionDegre() {
        double k2 = 0.0;
        int[] distribution = Toolkit.degreeDistribution(graph);
        for (int i = 0; i < distribution.length; i++) {
            if (distribution[i] > 0) {
                k2 += Math.pow(i, 2) * ((double) distribution[i] / graph.getNodeCount());
            }
        }
        return k2;
    }


}
