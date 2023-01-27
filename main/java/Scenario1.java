import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.graphstream.algorithm.Toolkit.randomNode;

public class Scenario1 {

    private static int nbJours = 84; // nbJour 84 j'ai pris 1 mois = 28 jours
    private static HashSet<Node> infected = new HashSet<>(); // liste des infecté
    private static HashSet<Node> copieMalades = new HashSet<>(); //

    private static double Beta = 1.0/7.0 ; // la probabilité de transmission
    private static double Mu = 1.0/14.0 ; // le taux pour que les individus infectieux guérissent
    private static long[] Nbinfectée = new long[nbJours+1]; // tabeau pour stocker le nombre d'infectés

    private Graph graph ; // le graphe

    /**
     *
     * @param graph : le réseau dbl
     */
    public Scenario1 (Graph graph) {
        this.graph = graph;
    }


    public void simulationScenario1() {
        //un individu infecté (patient zéro) en aléatoire
        Node  patient0 = randomNode(graph);

        patient0.setAttribute("health", "infected");
        // on l'ajoute à la liste des infectés
        infected.add(patient0);

        //  une simulation  pendant 84 jours pour calculer le nombre d'infectés
         for (int day = 1; day <= nbJours; day++) {

            for (Node n : infected) {
                double p = Math.random(); // un nombre aléatoire
                if (p < Beta) { // si la probabilté est vérifié Beta = 1/7
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

             infected.clear();// on vide la liste
             for (Node n : copieMalades) {
                 double p = Math.random();
                 if (p < Mu) {
                    n.setAttribute("health", "healthy");
                } else {
                    infected.add(n);
                }
            }
         //On stock le nombre d'individus infectés dans le tableau Nbinfectée
             for (Node n : graph) {
                if (n.getAttribute("health") == "infected") {
                    Nbinfectée[day] = Nbinfectée[day]+1;
                }
            }
            copieMalades.clear();
        }
       // System.out.println("azul");
        // on affiche le nombre d'infectés / et nombre de jours
        for (int i = 0; i < nbJours; i++) {
            System.out.println(i + "   " + Nbinfectée[i]++);
         }

    }
    // géneration fichier ".txt" pour tracer le graphe
    public void genereTextScenario1() {
        try {
            System.out.println("..");
            // on crée le fichier
            PrintWriter fichier = new PrintWriter(new FileWriter("Scénario/sc1.txt"));
            for (int i = 0; i < nbJours; i++) {
                 fichier.write(i + "   " + Nbinfectée[i]++);// on ajoute les nombre infecté au fichier
                fichier.println();
            }
            fichier.close();// on ferme le fichier
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String simulation1(Graph g){
        System.out.println("Demarrage du 1 er scenario ...");

        String lst = " ";
        Random rand = new Random();

        //Toute la population est en bonne santé
        for(Node node : g )
            node.setAttribute("health","healthy");

        //Choisir un individu au hasard pour l'infecter
        Node patientZero = Toolkit.randomNode(g);
        patientZero.setAttribute("health","infected");

        ArrayList<Node> infected = new ArrayList<>() ;
        infected.add(patientZero);

        ArrayList<Node> tmp = new ArrayList<>();

        for(int i=0;i<jours;i++){
            for(Node node:infected){//parcourir tous les individus infectes
                if(!tmp.contains(node))
                    tmp.add(node);

                //la probabilité de recevoir le mail infecté est 1/7
                if(rand.nextInt(7)+1==1) {
                    for(Edge e : node){
                        //Parcours des voisins de node
                        Node voisin = e.getOpposite(node);
                        if(!tmp.contains(voisin)){
                            voisin.setAttribute("health","infected");
                            tmp.add(voisin);
                        }
                    }
                }
            }
            //vider le tableau infected,prepare les individus infectés pour lendemain
            infected.clear();
            //mettre à jour
            for(Node n:tmp){
                if(rand.nextInt(14)+1==1) {
                    n.setAttribute("health", "healthy");
                }
                else infected.add(n);
            }
            System.out.println("j " + i +"   "  );

            lst +=(i+1)+" "+infected.size()+"\n";

        }

        //listFinale.add(lst);

        //  System.out.println("Patient Zero : " +patientZero);

        return lst;

    }



}
