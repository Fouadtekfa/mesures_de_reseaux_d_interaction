import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.algorithm.generator.RandomGenerator;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import static org.graphstream.algorithm.Toolkit.*;

public class MesuresRI {
    private Graph graph;

    /**
     * constrecteur
     * @param graph : un graphe
     */
    public MesuresRI(Graph graph) {
        this.graph = graph;
    }

    /**
     *
     * @return un double : qui la somme des distances d'un noeud s vers les autres noeuds
     *
     * d'abord  je crée une liste pour stocker les noeuds choisi avec
     * random(le choisi les noeuds avec randomNode de la class Toolkit)
     * puis je parcours  le graphe en largeur avec BreadthFirstIterator et je calcule la distance de chaque noeud
     *
     */
    public double sommeDistance(){
        double distance=0;
        ArrayList listNoeud = new ArrayList();

        for (int i = 0; i < 1000; i++) {
            Node node;
            do {
                node = Toolkit.randomNode(graph);
            }while (listNoeud.contains(node));
            listNoeud.add(node);
            BreadthFirstIterator bf = new BreadthFirstIterator(node);
             while (bf.hasNext()) {
                 distance += (bf.getDepthOf(bf.next()));
             }
        }
        return distance;
    }

    /**
     *
     * @return renvoie un double qui  la distance moyenne
     */
    public double distanceMoyenne() {
        double distance = sommeDistance();
        double max = graph.getNodeCount() * 1000;
        return distance/max;
    }

    /**
     *
     * @return renvoie un tableu de double
     */
    public double[] distributionDistance() {
        double distProba[] = new double[50];
        int nbNoeud = graph.getNodeCount() * 1000;
        for (int i = 0; i < 1000; i++) {
             Node noeud = Toolkit.randomNode(graph);
             BreadthFirstIterator bf = new BreadthFirstIterator(noeud);
            while (bf.hasNext()) {
                distProba[bf.getDepthOf(bf.next())]++;
             }
        }
        int j = 0;
        while (distProba[j] != 0) {
            System.out.println(j + " " + distProba[j]/nbNoeud);
            j++;
        }
         return  distProba;
    }

    /***
     * cette méthode à pour but de génerer un
     * fichier à partir des donnée calculé par
     * la méthode distributionDistance
     */
    public void genérationFichierDist(){

          double distProba [] = distributionDistance();
          int nbNoeud = graph.getNodeCount() * 1000;
           try {
                PrintWriter fichier = new PrintWriter(new FileWriter("Distribution/dist1.txt"));
                int j = 0;
                while (distProba[j] != 0) {
                     fichier.write(j + " " + distProba[j]/nbNoeud);
                    fichier.println();
                    j++;
                }
                fichier.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /***
     *
     * @param nbNoeuds : nombre de noeuds
     * @param degreeMoyen : le degre moyenne
     * @return : renvoie le graphe aléatoire
     *
     */
    public Graph generateurRandom(double nbNoeuds, double degreeMoyen){
         System.setProperty("org.graphstream.ui", "swing");
         Graph graph = new SingleGraph("RandomGraph");
         Generator gen = new RandomGenerator(degreeMoyen);
            gen.addSink(graph);
             gen.begin();
             for (int i = 0; i < nbNoeuds; i++)
                gen.nextEvents();
            gen.end();
         return graph;
    }

    /***
     *
     * @param nbNoeuds : nombre de noeuds
     * @param degreeMoyen : le degre Moyen
     * @return un graphe de BarabasiAlbert
     */
    public Graph generateurBarabasiAlbert(int nbNoeuds, int degreeMoyen) {
         System.setProperty("org.graphstream.ui", "swing");
         SingleGraph graph = new SingleGraph("BarabasiGen");
         Generator gen = new BarabasiAlbertGenerator(degreeMoyen);
        gen.addSink(graph);
         gen.begin();
         for (int i = 0; i < nbNoeuds; i++)
            gen.nextEvents();
        gen.end();
        return graph;
    }

    /**
     *
     * @param g : elle prends un graphe en paramétre
     *
     * cette méthode à pour but d'afficher
     * les meusers de base de chaque graphe passé en paramétre
     */
    public  void  mesureGenerator(Graph g) {

        System.out.println("Nombre de noeuds          : " + g.getNodeCount());
        System.out.println("Nombre de liens           : " + g.getEdgeCount());
        System.out.println("Degre moyen               : " + averageDegree(g));
        System.out.println("Coefficient de clustering : " + averageClusteringCoefficient(g));
        System.out.println("le graphe est connexe : " + isConnected(g));

        }

    }
