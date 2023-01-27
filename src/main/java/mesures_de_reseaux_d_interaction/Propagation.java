package mesures_de_reseaux_d_interaction;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        for(Node node : g ) node.setAttribute("status","healthy");

        //Choisir un individu au hasard pour l'infecter
        Node patient0 = Toolkit.randomNode(g);
        patient0.setAttribute("status","infected");
        Set<Node> malades = new HashSet<>();// liste des infecté

        malades.add(patient0);

        HashSet<Node> copieMalades = new HashSet<>(); //

        for(int i=1;i<=jours;i++){
            for(Node node:malades){//parcourir tous les individus infectes
                if(!copieMalades.contains(node))
                    copieMalades.add(node);

                //la probabilité de recevoir le mail infecté est 1/7
                double p1 = Math.random(); // un nombre aléatoire
                if (p1 < Beta) { // si la probabilté est vérifié Beta = 1/7
                    for(Edge e : node){
                        //Parcours des voisins de node
                        Node voisin = e.getOpposite(node);
                        if(!copieMalades.contains(voisin)){
                            voisin.setAttribute("status","infected");
                            copieMalades.add(voisin);
                        }
                    }
                }
            }
            //vider le tableau infected,prepare les individus infectés pour lendemain
            malades.clear();

            //Mettre à jour la copier des malades si y a des grisions
            for(Node n:copieMalades){
                double p2 = Math.random();
                //verifiers la proba de grisions
                if (p2 < Mu) {
                    //si oui le noeus est plus malade
                    n.setAttribute("status", "healthy");
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
        double Beta = 1.0/7.0 ; // β est la probabilité de transmettre dans une une journée
        double Mu = 1.0/14.0 ;//  μ est  le taux de guérison
        long[] NbMalades = new long[jours+1]; // tableau pour stocker le nombre d'infectés i la journer t[i] le nombre d'infectés
        System.out.println("simulation de  Scenario2");
        //  tout d'abord l'initialisation de Tout les noueds a un état sain
        for(Node node : g ) node.setAttribute("status","healthy");
        //50 % des individus mettre à jour en permanence leur anti-virus (immunisation aléatoire)
        // 50% ==> le nombre de noeudes/2 sont immunisée aléatoirement
        List<Node> immunisation = Toolkit.randomNodeSet(g, g.getNodeCount()/2);
        for(Node noeud : immunisation) {
            noeud.setAttribute("status", "protected");
        }

        //Choisir un noeud au hasard pour l'infecter qui serra le patient zéro a condition que il n'es pas immunisé
        Node patient0 ;
        do{

            patient0 = Toolkit.randomNode(g);

        }while((patient0.getAttribute("status") == "protected"));

        System.out.println(patient0.getAttribute("status"));

        //une fois que on trouve un noeud non immunisée on vas l'infictée
        patient0.setAttribute("status","infected");
        //affichage
        System.out.println(patient0.getAttribute("status"));
        // liste des infectés qui serra mis a jour tous les jours pour pouvoir calculer le nombre jour par jour
        Set<Node> malades = new HashSet<>();
        //on ajoute le patient0 a la liste des malades
        malades.add(patient0);
        //copie des malades
        HashSet<Node> copieMalades = new HashSet<>(); //


        for(int i=1;i<=jours;i++){

            for(Node n:malades){

                double p1 = Math.random();
                if(p1 <  Beta ){ // Vérifier la probabilité de transmission si  p < 1/7
                    n.neighborNodes().forEach(voisin->{if (!copieMalades.contains(voisin) && !immunisation.contains(voisin)) {
                        voisin.setAttribute("status", "infected");
                        copieMalades.add(voisin);

                    }});
                }
                //On ajoute le noeud infecté n à une liste s'il n'y est pas déjà
                if(!copieMalades.contains(n))
                    copieMalades.add(n);
            }


            //on supprime la listes des malades pour faire un calcule de jour par jour
            malades.clear();
                  //Mettre à jour la copier des malades si y a des grisions
            for(Node n:copieMalades){
                double p2 = Math.random();
                if (p2 < Mu) { //verifiers la proba de grisions
                    //si oui le nod est plus malade
                    n.setAttribute("status", "healthy");
                }
                else malades.add(n);
                n.setAttribute("status","infected");
            }
            //propagation du virus jour par jour
            NbMalades[i] = malades.size() ;
            System.out.println("Jour"+i+" ====> "+NbMalades[i] );

        }
        //supprimer  la liste de malades pour faire  la  propagation du virus jour par jour
        malades.clear();
        // System.out.println(copieMalades.toString());
        copieMalades.clear();
        // System.out.println(copieMalades.toString());
        //mettre les donner de tableau dans un fichier avec la méthode savetab
        savetab(nom_fichiers,NbMalades);

    }

    public static void  SimulationScenario3(Graph g , String nom_fichiers){
        Random numRandom = new Random();

        double Beta = 1.0/7.0 ; // β est la probabilité de transmettre dans une une journée
        double Mu = 1.0/14.0 ;//  μ est  le taux de guérison

        long[] NbMalades = new long[90+1]; // tableau pour stocker le nombre d'infectés i la journer t[i] le nombre d'infectés

        System.out.println("simulation de  Scenario3");
        for(Node node : g ) node.setAttribute("status","healthy");

        double TotalDegreGroupe0 = 0;
        double TotalDegreGroupe1 = 0;
        List<Node> moitieIndiv = Toolkit.randomNodeSet(g,g.getNodeCount()/2);//  50 % des individus (pour cas 3)
        List<Node> immunisation =   new ArrayList();//Toolkit.randomNodeSet(g, g.getNodeCount()/2);

        for(Node node : moitieIndiv) {
            Node nodeImmunise = node.getEdge(numRandom.nextInt(node.getDegree())).getOpposite(node);
            nodeImmunise.setAttribute("statut","protected");
            immunisation.add(nodeImmunise);
            TotalDegreGroupe0 += node.getDegree();
            TotalDegreGroupe1 += nodeImmunise.getDegree();

        }
        System.out.println("Le degré moyen des groupes 0 est :"+TotalDegreGroupe0/moitieIndiv.size()+"\n");
        System.out.println("Le degré moyen des groupes 1 est :"+TotalDegreGroupe1/moitieIndiv.size()+"\n");


        Node patient0 ;
        do{
            patient0 = Toolkit.randomNode(g);

        } while((patient0.getAttribute("status") == "protected"));

        patient0.setAttribute("status","infected");
        Set<Node> malades = new HashSet<>();
        malades.add(patient0);
        HashSet<Node> copieMalades = new HashSet<>(); //

        for(int i=1;i<=90;i++) {

            for(Node n:malades) {

                double p1 = Math.random();
                if(p1 <  Beta ) {
                    n.neighborNodes().forEach(voisin->{
                        if(!copieMalades.contains(voisin) && !moitieIndiv.contains(voisin)) {
                            voisin.setAttribute("status", "infected");
                            copieMalades.add(voisin);
                        }

                    });
                    if(!copieMalades.contains(n))
                        copieMalades.add(n);
                }
            }


            for(Node n_:copieMalades){
                double p2 = Math.random();
                if (p2 < Mu) {
                    n_.setAttribute("status", "healthy");
                }
                else {
                    malades.add(n_);
                    n_.setAttribute("status","infected");

                }

            }

            NbMalades[i] = malades.size() ;
            System.out.println( i +" " + NbMalades[i]);
            malades.clear();
        }
        malades.clear();
        copieMalades.clear();
        savetab(nom_fichiers, NbMalades);


    }


    public static  void  savetab( String name,  long [] tab){

        try {

            String filepath = System.getProperty("user.dir") + File.separator + "./src/resources/propagation/"+name+".data";
            FileWriter fw = new FileWriter(filepath);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < jours; i++) {
                bw.write(i + "   " + tab[i]++ +" \n");

            }

            bw.close();
            System.out.println("Fin  simulation de  "+name);
            System.out.println("Le fichier "+name+" à été généré avec succès dans le répertoire des ressources/propagation");
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
        //SimulationScenario1(g,"scenario1");
       // SimulationScenario2(g,"scenario2");
       SimulationScenario3(g,"Scenario3");


    }








}
