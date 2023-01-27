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

public class Scenario2 {

    private Graph graph;
    public Propagation pro;

    public static int nbJours = 84; //NbJours
    private static HashSet<Node> infected = new HashSet<>(); //liste infectés
    private static HashSet<Node> copieMalades = new HashSet<>();

    private static double Beta = 1.0/7.0 ; // la probabilité de transmission
    private static double Mu = 1.0/14.0  ; // le taux pour que les individus infectieux guérissent

    private static long[] Nbinfectée = new long[nbJours+1]; // nombre d'inféctes

    public Scenario2(Graph graph){
        this.graph = graph; // le réseau DBLP
       // this.pro = new Propagation(this.graph);
    }

    public void simulation(){

        Node patient0;  //un individu infecté (patient zéro) en aléatoire
        //Récupérer une moitié des noeuds du graphe aléatoirement
        List<Node> immunisation = Toolkit.randomNodeSet(graph, graph.getNodeCount()/2);

        //Immunisation des 50% d'individus  aléatoirement
        for(Node noeud : immunisation) {
             noeud.setAttribute("health", "immunise");
         }

        //on choisi le patient0 aleatoirement
        do{
             patient0 = Toolkit.randomNode(graph);
        }while((patient0.getAttribute("health") == "immunise"));

        patient0.setAttribute("health", "infected");
        infected.add(patient0); // on l'ajoute à la liste

         for(int jour=1;jour<=nbJours;jour++){
            for(Node n:infected){
                double p = Math.random();

                if(p <  Beta ){ // la probabiloté de transimission, si elle est vérifie les noeuds deviennent  infecté
                    n.neighborNodes().forEach(v->{

                        if (!copieMalades.contains(v) && !immunisation.contains(v)) {
                         v.setAttribute("health", "infected");
                        copieMalades.add(v);

                    }});
                }
                 if(!copieMalades.contains(n))
                    copieMalades.add(n);
            }
             infected.clear();

            for(Node noeud:copieMalades){
                 double p = Math.random();
                 if(p < Mu){ //  si Mu est viréfié  "le taux pour que les individus infectieux guérissent"
                    noeud.setAttribute("health", "healthy");// le noeud devient healthy
                }
                else{// sinon on l'ajoute à la liste des malades
                    infected.add(noeud);
                    noeud.setAttribute("health","infected");
                }
            }
             // on selectione les infectés
             for (Node n:graph){
                if(n.getAttribute("health")=="infected"){
                    Nbinfectée[jour] = Nbinfectée[jour] + 1;
                }
            }
            copieMalades.clear();
        }
          // on affiche le nombre d'infectés
        for (int i = 0; i < nbJours; i++) {
            System.out.println(i + "   " + Nbinfectée[i]++);
        }
    }

    // géneration fichier pour afficher le graphe
    public void genereTextScenario2() {
        try {
            System.out.println("..");
            PrintWriter fichier = new PrintWriter(new FileWriter("Scénario/sc2.txt"));
            for (int i = 0; i < nbJours; i++) {
                fichier.write(i + "   " + Nbinfectée[i]++);
                fichier.println();
            }
            fichier.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void propagationSc2(){
        int jours = 90;
        long[] infections = new long[jours+1];
        Random random = new Random();
        ArrayList<Node> malades = new ArrayList<Node>();
        ArrayList<Node> copieMalades = new ArrayList<Node>();
        Node patient0;

        double degMoyGrp0=0.0;
        double degMoyGrp1=0.0;
        int nb=0;
        int nbreMoitierGraphe = (int) graph.getNodeCount()/2;
        Graph scenario02 = graph;

        //Récupérer une moitié des noeuds du graphe aléatoirement
        List <Node> immunisation = Toolkit.randomNodeSet(graph, graph.getNodeCount()/2);

        //Immunisation des 50% d'individus pris aléatoirement
        for(Node noeud : immunisation) {
            degMoyGrp0+= noeud.getDegree();
            noeud.setAttribute("health", "immunise");
            scenario02.removeNode(noeud);
        }
        degMoyGrp0 = degMoyGrp0 / nbreMoitierGraphe;
        System.out.println("le seuil épidémique du réseau modifié pour la stratégies d'immunisation du scenario 02 : "
                +Toolkit.averageDegree(scenario02)/dispersionDegre());


        //L'épidémie commence avec un individu infecté (patient zéro non immunisé)
        do{
            System.out.println("ih yedad ");
            patient0 = Toolkit.randomNode(graph);
        }while((patient0.getAttribute("health") == "immunise"));
        System.out.println("adagh ukessar ");

        patient0.setAttribute("health", "infected");
        malades.add(patient0);
        //calcule le nombre de malades pendant 90 jours (une sorte de simulation)
        for(int jour=1;jour<=jours;jour++){
            //comme un individu envoi 1 mail par semaine a ses collaborateur alors la probabilité
            // est de 1/7 de les infecté (voisins de cet individu(noeud))
            for(Node n:malades){
                if(random.nextInt(7)+1 == 1){
                    n.neighborNodes().forEach(v->{if (!copieMalades.contains(v) && !immunisation.contains(v)) {
                        //le voisin devient infecté
                        v.setAttribute("health", "infected");
                        // on ajoute ce voisin à la liste
                        copieMalades.add(v);

                    }});
                }
                //On ajoute le noeud infecté n à une liste s'il n'y est pas déjà
                if(!copieMalades.contains(n))
                    copieMalades.add(n);
            }
            //On vide la liste des malades
            malades.clear();

            //Chaque noeud infecté  peut  guérir avec une probabilité de 1/14,
            //soit rester infecté alors dans ce cas on l'ajoute à la liste des malades
            for(Node noeud:copieMalades){
                if(random.nextInt(14)+1 == 1){
                    noeud.setAttribute("health", "healthy");
                }
                else{
                    malades.add(noeud);
                    noeud.setAttribute("health","infected");
                }
            }
            //On stock le nombre de noeuds infectés ce jour là commme valeur de la case "jour" du tableau nbInfections
            for (Node n:graph){
                if(n.getAttribute("health")=="infected"){
                    infections[jour] = infections[jour] + 1;
                }
            }
            copieMalades.clear();
        }
        for(Node n: graph){
            if(!(immunisation.contains(n))){
                degMoyGrp1 += n.getDegree();
                nb++;
            }
        }
        degMoyGrp1 = degMoyGrp1/ nb;
        System.out.println("\ndegre moyen dans le scenario 2  : groupe 0 = " + degMoyGrp0 + " , groupe 1 = " + degMoyGrp1);
        try {
            PrintWriter fichier = new PrintWriter(new FileWriter("scenarios/sc2Barbasi.txt"));
            for (int i = 0; i < jours; i++) {
                // infections[i]  : represente le nombre de malades en fonction du jour i
                fichier.write(i + "   " + infections[i]++);
                fichier.println();
            }
            fichier.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






}
