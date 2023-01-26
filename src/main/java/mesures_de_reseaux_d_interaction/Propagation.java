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
import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.positionFromObject;

public class Propagation {

    public static int jours = 90;

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

    public static void  SimulationScenario1(Graph g , String nom_fichiers){

        double Beta = 1.0/7.0 ; // la probabilité de transmission
        double Mu = 1.0/14.0 ;//  taux guérissant
        long[] NbMalades = new long[jours+1]; // tabeau pour stocker le nombre d'infectés
        System.out.println("simulation de  Scenario1");


        // initialisation de Tout les node a non malade
        for(Node node : g ) node.setAttribute("health","healthy");

        //Choisir un individu au hasard pour l'infecter
        Node patient0 = Toolkit.randomNode(g);
        patient0.setAttribute("health","infected");
        Set<Node> malades = new HashSet<>();// liste des infecté

        malades.add(patient0);

        HashSet<Node> copieMalades = new HashSet<>(); //

        for(int i=1;i<=jours;i++){
            for(Node node:malades){//parcourir tous les individus infectes
                if(!copieMalades.contains(node))
                    copieMalades.add(node);

                //la probabilité de recevoir le mail infecté est 1/7
                double p = Math.random(); // un nombre aléatoire
                if (p < Beta) { // si la probabilté est vérifié Beta = 1/7
                    for(Edge e : node){
                        //Parcours des voisins de node
                        Node voisin = e.getOpposite(node);
                        if(!copieMalades.contains(voisin)){
                            voisin.setAttribute("health","infected");
                            copieMalades.add(voisin);
                        }
                    }
                }
            }
            //vider le tableau infected,prepare les individus infectés pour lendemain
            malades.clear();
            //mettre à jour
            for(Node n:copieMalades){
                double p = Math.random();
                if (p < Mu) {
                    n.setAttribute("health", "healthy");
                }
                else malades.add(n);
            }
            NbMalades[i] = malades.size() ;

            System.out.println("Jour"+i+" ====> "+NbMalades[i] );

        }
       // System.out.println(copieMalades.toString());
        copieMalades.clear();
       // System.out.println(copieMalades.toString());

         savetab(nom_fichiers,NbMalades);

    }
    public static void  SimulationScenario2(Graph g , String nom_fichiers){

        double Beta = 1.0/7.0 ; // la probabilité de transmission
        double Mu = 1.0/14.0 ;//  taux guérissant
        long[] NbMalades = new long[jours+1]; // tabeau pour stocker le nombre d'infectés
        System.out.println("simulation de  Scenario1");
     //   50 % des individus mettre à jour en permanence leur anti-virus (immunisation aléatoire)
        List<Node> immunisation = Toolkit.randomNodeSet(g, g.getNodeCount()/2);
        for(Node noeud : immunisation) {
            noeud.setAttribute("health", "immunise");
        }

        // initialisation de Tout les node a non malade
        for(Node node : g ) node.setAttribute("health","healthy");

        //Choisir un individu au hasard pour l'infecter
        Node patient0 = Toolkit.randomNode(g);
        patient0.setAttribute("health","infected");
        Set<Node> malades = new HashSet<>();// liste des infecté

        malades.add(patient0);

        HashSet<Node> copieMalades = new HashSet<>(); //

        for(int i=1;i<=jours;i++){
            for(Node node:malades){//parcourir tous les individus infectes
                if(!copieMalades.contains(node))
                    copieMalades.add(node);

                //la probabilité de recevoir le mail infecté est 1/7
                double p = Math.random(); // un nombre aléatoire
                if (p < Beta) { // si la probabilté est vérifié Beta = 1/7
                    for(Edge e : node){
                        //Parcours des voisins de node
                        Node voisin = e.getOpposite(node);
                        if(!copieMalades.contains(voisin)){
                            voisin.setAttribute("health","infected");
                            copieMalades.add(voisin);
                        }
                    }
                }
            }
            //vider le tableau infected,prepare les individus infectés pour lendemain
            malades.clear();
            //mettre à jour
            for(Node n:copieMalades){
                double p = Math.random();
                if (p < Mu) {
                    n.setAttribute("health", "healthy");
                }
                else malades.add(n);
            }
            NbMalades[i] = malades.size() ;

            System.out.println("Jour"+i+" ====> "+NbMalades[i] );

        }
        // System.out.println(copieMalades.toString());
        copieMalades.clear();
        // System.out.println(copieMalades.toString());

        savetab(nom_fichiers,NbMalades);

    }





    public static  void  savetab( String name,  long [] tab){
    try {
        System.out.println("Fin  simulation de  "+name);
        PrintWriter fichier = new PrintWriter(new FileWriter(name+".data"));
        for (int i = 0; i < jours; i++) {
            fichier.write(i + "   " + tab[i]++);
            fichier.println();
        }

        fichier.close();
    } catch (Exception e) {
        e.printStackTrace();
    }


}




    public static void main( String[] args ){


 Graph g =readgraphe("./src/resources/com-dblp.ungraph.txt");
Graph g2=g;
Graph g3=g;

        double beta = (double) 1/7 ;
        double mu = (double) 1/14;
        double lambda = (double) beta/mu;
        double averageDegree = Toolkit.averageDegree(g);
        System.out.println("Taux de propagation du virus  λ = β/µ= 14/7= " +lambda );
        System.out.println("Seuil épidémique λc = 〈k〉/〈k2> = " +averageDegree/disDegre(g));
        System.out.println("le seuil théorique d'un réseau aléatoire du même degré moyen : λc = 1/<K>+1 =" + 1/(1+averageDegree));
       //simulationScenario1(g);
       SimulationScenario1(g,"test6");

       // saveGnuPlot( g,  txt);
         //gralPropagationAlgo(1,g,"test1");
        // simulation1(g);
       // gralPropagationAlgo(2,g2,"test2");
        // simulation1(g);
        ///gralPropagationAlgo(3,g3,"test3");
    }








}
