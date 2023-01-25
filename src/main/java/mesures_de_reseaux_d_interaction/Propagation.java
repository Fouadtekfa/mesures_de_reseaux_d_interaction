package mesures_de_reseaux_d_interaction;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;

import java.io.IOException;

public class Propagation {





    public  static  Graph readgraphe(String Path){
        System.setProperty("org.graphstream.ui", "swing");
        Graph g = new DefaultGraph("g");
    //    String filePath = "./src/resources/com-dblp.ungraph.txt";
       // String filePath = "path";
        FileSource fs = new FileSourceEdge();
        fs.addSink(g);

        try {
            fs.readAll(Path);
            System.out.println("=================debut de lecteur de ===============");
        } catch( IOException e) {
            e.printStackTrace();
        }finally {
            fs.removeSink(g);
            System.out.println("===========Fin de lecteur==============");
        }

         return  g;

    }


    public static double disDegre(Graph g) {
        double k2 = 0.0;

        int[] distribution = Toolkit.degreeDistribution(g);
        for (int i = 0; i < distribution.length; i++) {
            if (distribution[i] > 0) {
                k2 += Math.pow(i, 2) * ((double) distribution[i] / g.getNodeCount());
            }
        }
        return k2;
    }


    public static void main( String[] args ){


 Graph g =readgraphe("./src/resources/com-dblp.ungraph.txt");

        double beta = (double) 1/7 ;
        double mu = (double) 1/14;
        double lambda = (double) beta/mu;
        double averageDegree = Toolkit.averageDegree(g);
        System.out.println("Taux de propagation du virus  λ = β/µ= 14/7= " +lambda );
        System.out.println("Seuil épidémique λc = 〈k〉/〈k2> = " +averageDegree/disDegre(g));
        System.out.println("le seuil théorique d'un réseau aléatoire du même degré moyen : λc = 1/<K>+1 =" + 1/(1+averageDegree));






    }








}
