package mesures_de_reseaux_d_interaction;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Consumer;

import static org.graphstream.algorithm.Toolkit.randomNode;
import static org.graphstream.algorithm.Toolkit.randomNodeSet;

public class Propagation {

    public static int jours = 84;

    private static HashSet<Node> malades = new HashSet<>(); //liste infectés
    private static HashSet<Node> copieMalades = new HashSet<>();

    private static double Beta = 1.0 / 7.0; // la probabilité de transmission
    private static double Mu = 1.0 / 14.0; // le taux pour que les individus infectieux guérissent

    private static long[] infecteJ = new long[jours + 1]; // pour stockr le nombre d'individus infectés par jour
    public static Random random = new Random();

    public static Graph readgraphe(String Path) {
        System.setProperty("org.graphstream.ui", "swing");
        Graph g = new DefaultGraph("g");
        //    String filePath = "./src/resources/com-dblp.ungraph.txt";
        // String filePath = "path";
        FileSource fs = new FileSourceEdge();
        fs.addSink(g);

        try {
            fs.readAll(Path);
            System.out.println("=================debut de lecteur de ===============");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fs.removeSink(g);
            System.out.println("===========Fin de lecteur==============");
        }

        return g;

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


    public static void simulationScenario1( Graph graphe ) {

        //patient0 est le premier patient infecté on va le generer aléatoirement


        Node  patient0 = Toolkit.randomNode(graphe);

        patient0.setAttribute("health", "infected");
        malades.add(patient0);

        //calculer le nombre de malades pendant 84 jours (une sorte de simulation)
        for (int j = 1; j <= jours; j++) {

            for (Node n : malades) {
                //comme un individu envoi 1 mail par semaine a ses collaborateur alors la probabilité
                // est de 1/7 de les infecté (voisins de cet individu(noeud))
              //  double p = Math.random(); // un nombre aléatoire
                //if (p < Beta) { // si la probabilté est vérifié Beta = 1/7
                if (random.nextInt(7) + 1 == 1){
                    n.neighborNodes().forEach(v->{
                        if (!copieMalades.contains(v)) {
                            //le voisin devient infecté
                            v.setAttribute("health", "infected");
                            copieMalades.add(v);
                        }});
                }
                //On ajoute le noeud infecté n à une liste  s'il n'y est pas déjà
                if (!copieMalades.contains(n))
                    copieMalades.add(n);
            }

            malades.clear();// on vide la liste
            for (Node n : copieMalades) {
                double p = Math.random();
                if (p < Mu) {
                    n.setAttribute("health", "healthy");
                } else {
                    malades.add(n);
                }
            }
            //On stock le nombre d'individus infectés dans le tableau Nbinfectée
            for (Node n : graphe) {
                if (n.getAttribute("health") == "infected") {
                    infecteJ[j] = infecteJ[j]+1;
                }
            }
            copieMalades.clear();
        }
        //affichage de nb infectée par jour
        for (int i = 0; i <=jours; i++) {
            System.out.println(i + "   " + infecteJ[i]++);
        }

        try {
            System.out.println("simulation Scenario1");
            PrintWriter fichier = new PrintWriter(new FileWriter("Scenario1.data"));
            for (int i = 0; i <= jours; i++) {
                fichier.write(i + "   " + infecteJ[i]++);
                fichier.println();
            }
            fichier.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        simulationScenario1(g);



    }








}
