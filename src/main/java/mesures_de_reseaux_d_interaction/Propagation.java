package mesures_de_reseaux_d_interaction;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
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

    public static void SimulationScenario1(Graph g, String nom_fichiers) {

        double Beta = 1.0 / 7.0; // la probabilité de transmission
        double Mu = 1.0 / 14.0;//  taux guérissant
        long[] NbMalades = new long[jours + 1]; // tabeau pour stocker le nombre d'infectés
        System.out.println("simulation de  Scenario1");


        // initialisation de Tout les node a non malade
        for (Node node : g) node.setAttribute("status", "healthy");

        //Choisir un individu au hasard pour l'infecter
        Node patient0 = Toolkit.randomNode(g);
        patient0.setAttribute("status", "infected");
        Set<Node> malades = new HashSet<>();// liste des infecté

        malades.add(patient0);

        HashSet<Node> copieMalades = new HashSet<>(); //

        for (int i = 1; i <= jours; i++) {
            for (Node node : malades) {//parcourir tous les individus infectes
                if (!copieMalades.contains(node))
                    copieMalades.add(node);

                //la probabilité de recevoir le mail infecté est 1/7
                double p1 = Math.random(); // un nombre aléatoire
                if (p1 < Beta) { // si la probabilté est vérifié Beta = 1/7
                    for (Edge e : node) {
                        //Parcours des voisins de node
                        Node voisin = e.getOpposite(node);
                        if (!copieMalades.contains(voisin)) {
                            voisin.setAttribute("status", "infected");
                            copieMalades.add(voisin);
                        }
                    }
                }
            }
            //vider le tableau infected,prepare les individus infectés pour lendemain
            malades.clear();

            //Mettre à jour la copier des malades si y a des grisions
            for (Node n : copieMalades) {
                double p2 = Math.random();
                //verifiers la proba de grisions
                if (p2 < Mu) {
                    //si oui le noeus est plus malade
                    n.setAttribute("status", "healthy");
                } else malades.add(n);
            }
            NbMalades[i] = malades.size();

            System.out.println("Jour" + i + " ====> " + NbMalades[i]);

        }
        // System.out.println(copieMalades.toString());
        copieMalades.clear();
        // System.out.println(copieMalades.toString());

        savetab(nom_fichiers, NbMalades);

    }

    public static void SimulationScenario2(Graph g, String nom_fichiers) {
        double Beta = 1.0 / 7.0; // β est la probabilité de transmettre dans une une journée
        double Mu = 1.0 / 14.0;//  μ est  le taux de guérison
        long[] NbMalades = new long[jours + 1]; // tableau pour stocker le nombre d'infectés i la journer t[i] le nombre d'infectés
        System.out.println("simulation de  Scenario2");
        //  tout d'abord l'initialisation de Tout les noueds a un état sain
        for (Node node : g) node.setAttribute("status", "healthy");
        //50 % des individus mettre à jour en permanence leur anti-virus (immunisation aléatoire)
        // 50% ==> le nombre de noeudes/2 sont immunisée aléatoirement
        List<Node> immunisation = Toolkit.randomNodeSet(g, g.getNodeCount() / 2);
        for (Node noeud : immunisation) {
            noeud.setAttribute("status", "protected");
        }

        //Choisir un noeud au hasard pour l'infecter qui serra le patient zéro a condition que il n'es pas immunisé
        Node patient0;
        do {

            patient0 = Toolkit.randomNode(g);

        } while ((patient0.getAttribute("status") == "protected"));

       // System.out.println(patient0.getAttribute("status"));

        //une fois que on trouve un noeud non immunisée on vas l'infictée
        patient0.setAttribute("status", "infected");
        //affichage
     //   System.out.println(patient0.getAttribute("status"));
        // liste des infectés qui serra mis a jour tous les jours pour pouvoir calculer le nombre jour par jour
        Set<Node> malades = new HashSet<>();
        //on ajoute le patient0 a la liste des malades
        malades.add(patient0);
        //copie des malades
        HashSet<Node> copieMalades = new HashSet<>(); //
       //liste pour stocker les perssone non immuniser
        List<Node> l1 = new ArrayList<>();
        for (Node node : g) {
            l1.add(node);
        }
        Iterator<Node> iteratorInfectes = l1.iterator();
        while (iteratorInfectes.hasNext()) {
            if (iteratorInfectes.next().getAttribute("status") == "protected") {
                iteratorInfectes.remove();

            }
        }


        // Supprime les noeuds immunisés si on les trouve dans la liste des personnes susceptibles.

    // Calculer de seuil épidémique par a rapport au 50% non immunisée
    double AveragesomDegreeCanbeInfectes = 0.0 ; // som de degrée <k>
    double AveragesomDegreeCanbeInfectesCaree = 0.0 ; // somme  <k²>

    for (Node n : l1)
    {
        AveragesomDegreeCanbeInfectes += n.getDegree();
        AveragesomDegreeCanbeInfectesCaree += (n.getDegree()*n.getDegree());

    }
    AveragesomDegreeCanbeInfectes = AveragesomDegreeCanbeInfectes/l1.size();
    AveragesomDegreeCanbeInfectesCaree = AveragesomDegreeCanbeInfectesCaree/l1.size();

    System.out.print("le seuil épidémique du réseau de Scenario 2 est avec liste : "+AveragesomDegreeCanbeInfectes/AveragesomDegreeCanbeInfectesCaree +"\n");

        for (int i = 1; i <= jours; i++) {

            for (Node n : malades) {

                double p1 = Math.random();
                if (p1 < Beta) { // Vérifier la probabilité de transmission si  p < 1/7
                    n.neighborNodes().forEach(voisin -> {
                        if (!copieMalades.contains(voisin) && !immunisation.contains(voisin)) {
                            voisin.setAttribute("status", "infected");
                            copieMalades.add(voisin);

                        }
                    });
                }
                //On ajoute le noeud infecté n à une liste s'il n'y est pas déjà
                if (!copieMalades.contains(n))
                    copieMalades.add(n);
            }


            //on supprime la listes des malades pour faire un calcule de jour par jour
            malades.clear();
            //Mettre à jour la copier des malades si y a des grisions
            for (Node n : copieMalades) {
                double p2 = Math.random();
                if (p2 < Mu) { //verifiers la proba de grisions
                    //si oui le nod est plus malade
                    n.setAttribute("status", "healthy");
                } else malades.add(n);
                n.setAttribute("status", "infected");
            }
            //propagation du virus jour par jour
            NbMalades[i] = malades.size();
           System.out.println("Jour" + i + " ====> " + NbMalades[i]);

        }
        //supprimer  la liste de malades pour faire  la  propagation du virus jour par jour
        malades.clear();
        // System.out.println(copieMalades.toString());
        copieMalades.clear();
        // System.out.println(copieMalades.toString());
        //mettre les donner de tableau dans un fichier avec la méthode savetab
        savetab(nom_fichiers, NbMalades);

    }




    public  static void SimulationScenario3(Graph graph , String nom_fichiers ) {
    Random random = new Random();
    System.out.println("simulation de  Scenario3 ");
    long[] NbMalades = new long[jours+1];//// tableau pour stocker le nombre d'infectés i la journer t[i] le nombre d'infectés
    ArrayList<Node> Non_immunise = new ArrayList<>();// liste susceptible
    ArrayList<Node> malades = new ArrayList<>(); // l'etat du scénario 3
      //initialisation
    for (Node node : graph) {
        node.setAttribute("status", "healthy");
        Non_immunise.add(node);
    }

    List<Node> groupe0; //la liste des 50 % des individus pour but de convaincre un de leurs contacts de mettre à jour en permanence son anti-virus (immunisation sélective).
    double DegreSom=0,DegreSom1=0 ;
    int totalGrp1 = 0 ;
    groupe0 = randomNodeSet(graph, graph.getNodeCount() / 2);

//
    for (Node n : groupe0) {
        DegreSom += n.getDegree();
        //si il as bien un voisin
        if(n.getDegree()!=0){
            //choisir un voisin random
            Node VImmunesee = n.getEdge(random.nextInt(n.getDegree())).getOpposite(n);
            //immunisée le voisin
            VImmunesee.setAttribute("status", "protected");}
    }

    Iterator<Node> iteratorInfectes = Non_immunise.iterator();
       // Supprime les noeuds immunisés si on les trouve dans la liste des personnes susceptibles.
    while (iteratorInfectes.hasNext()) {
        if (iteratorInfectes.next().getAttribute("status") == "protected") {
            iteratorInfectes.remove();

        }
    }
    // Calculer de seuil épidémique par a rapport au 50% non immunisée
    double AveragesomDegreeCanbeInfectes = 0.0 ; //  <k>
    double AveragesomDegreeCanbeInfectesCaree = 0.0 ; //   <k²>

    for (Node n : Non_immunise)
    {
        AveragesomDegreeCanbeInfectes += n.getDegree();
        AveragesomDegreeCanbeInfectesCaree += (n.getDegree()*n.getDegree());

    }

    AveragesomDegreeCanbeInfectes = AveragesomDegreeCanbeInfectes/Non_immunise.size();
    AveragesomDegreeCanbeInfectesCaree = AveragesomDegreeCanbeInfectesCaree/Non_immunise.size();

    System.out.print("le seuil épidémique du réseau de Scenario 3 est : "+AveragesomDegreeCanbeInfectes/AveragesomDegreeCanbeInfectesCaree +"\n");

    //calcule de la somme de degrée des noeuds est immunisés
        for(Node n : graph){
            //si un noeud est immunisés
            if (n.getAttribute("status") == "protected"){
            DegreSom1 += n.getDegree();
            //incrémentée le cmpt de groupe 1 (immunisée)
            totalGrp1++;
        }
    }

      //Choisir un individu au hasard pour l'infecter qui n'est pas immunisée
        Node patient0 = Non_immunise.get(random.nextInt(Non_immunise.size()));
        patient0.setAttribute("status", "infected");
        malades.add(patient0);//ajout de patient0 a la liste des malades
        System.out.println("====Troisième question=======");
        System.out.println("calcule de degré moyen des groupes 0 et 1 ");
        System.out.println("Le degré moyen de groupes 0  :  " + DegreSom/groupe0.size());
        System.out.println("Le degré moyen de groupes 1 : " + DegreSom1/totalGrp1);
    for (int i = 0; i < jours; i++) {
        for (Node n : graph) {
            if (n.getAttribute("status") == "infected") {
                for (Edge e : n) {
                    Node voisin = e.getOpposite(n);
                   // β=1/ 7 est la probabilité de transmettre dans une une journée
                    if ((random.nextInt(7) == 1) && voisin.getAttribute("status")!= "protected") {
                        if (voisin.getAttribute("status") == "healthy") {
                            voisin.setAttribute("status", "infected");
                            malades.add(voisin);
                        }
                    }
                }
            }
        }

        Iterator<Node> iteratorS3 = malades.iterator();

        while (iteratorS3.hasNext()) {
            //μ = 1 / 14 le taux de guérison
            if (random.nextInt(14) == 1) {
                iteratorS3.next().setAttribute("status", "healthy");
                iteratorS3.remove();
            } else {
                iteratorS3.next();
            }


        }

        NbMalades[i] = malades.size();

        int af = i + 1;
        System.out.println(af + " " + NbMalades[i]);
    }

    savetab(nom_fichiers,NbMalades);

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


    /**
     *
     * @param nbNoeuds
     * @param degreeMoyen
     * @return graphe
     */
    public static Graph generateurBarabasiAlbert(int nbNoeuds ,int degreeMoyen) {
        System.out.println("La génération du graphe Barabàsi-Albert ");
        Graph graph = new SingleGraph("Barabàsi-Albert");
        // Between 1 and  degreeMoyen new links per node added.
        Generator gen = new BarabasiAlbertGenerator(degreeMoyen);
        // Generate nbNoeuds nodes:
        gen.addSink(graph);
        gen.begin();

        /*for (int i = 0; i < nbNoeuds; i++) {
            gen.nextEvents();
        }*/
        while(graph.getNodeCount()<nbNoeuds) {
            gen.nextEvents();
        }
        gen.end();
        System.out.println("Fin génération du graphe Barabàsi-Albert");
        return graph;
    }

    /**
     *
     * @param nbNoeuds
     * @param degreeMoyen
     * @return graphe
     */
    public static Graph generateurRandom(double nbNoeuds , double degreeMoyen){
        System.out.println("La génération du graphe aléatoire ");
        Graph graph = new SingleGraph("RandomGraph");
        Generator gen = new RandomGenerator(degreeMoyen);
        gen.addSink(graph);
        gen.begin();
       /* for (int i = 0; i < nbNoeuds; i++)
            gen.nextEvents();

        */
        while(graph.getNodeCount()<nbNoeuds) {
            gen.nextEvents();
        }
        gen.end();
        System.out.println("Fin génération du graphe aléatoire");
        return graph;
    }






    public static void main( String[] args ){


 Graph g =readgraphe("./src/resources/com-dblp.ungraph.txt");
        Graph g2=g;

        double beta = (double) 1/7 ;
        double mu = (double) 1/14;
        double lambda = (double) beta/mu;
        double averageDegree = Toolkit.averageDegree(g);
        System.out.println("Taux de propagation du virus  λ = β/µ= 14/7= " +lambda );
        System.out.println("Seuil épidémique λc = 〈k〉/〈k2> = " +averageDegree/disDegre(g));
        System.out.println("le seuil théorique d'un réseau aléatoire du même degré moyen : λc = 1/<K>+1 =" + 1/(1+averageDegree));
        //SimulationScenario1(g,"scenario1");
       // SimulationScenario2(g,"scenario2TD");
        // SimulationScenario3(g2,"Scenario3T");

       // scenario3(g,"Scenario3TT");
      // Graph gRandom= generateurRandom(g.getNodeCount(),6);
       //Graph gR2=gRandom;
       //Graph gR3=gRandom;
        //SimulationScenario1(gRandom,"Randomscenario1");
        //SimulationScenario2(gR2,"Randomscenario2");
        //SimulationScenario3(gR3,"RandomScenario3");
        Graph gBA= generateurBarabasiAlbert (g.getNodeCount(),6);
        //Graph gBA1=gBA;
        Graph gBA2=gBA;
       // SimulationScenario1(gBA,"BarabasiAlbertscenario1");
        //SimulationScenario2(gBA1,"BarabasiAlbertscenario2");
        SimulationScenario3(gBA2,"BarabasiAlbertScenario3");



    }








}
