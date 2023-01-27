import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;
import org.graphstream.algorithm.generator.RandomGenerator;
import java.io.IOException;
import java.util.*;

import static org.graphstream.algorithm.Toolkit.*;

public class Test {

    public static void main(String[] args) {



        /**
         *  question 1 : la lecture de fichier
         */

        //System.setProperty("org.graphstream.ui", "swing");
        Graph g = new DefaultGraph("g");
        FileSource fs = new FileSourceEdge();
        fs.addSink(g);

        try {
            fs.readAll("./src/fichier.txt");
        } catch( IOException e) {
            e.printStackTrace();
        }

        /**
         *  question 2 : les mesure de base
         */
        // nombre de noeud

        System.out.println("nombre total de noeud : "+g.getNodeCount());
        // nombre de liens
        System.out.println("nombre total de liens : "+g.getEdgeCount());
        // degre moyen
        System.out.println("degre moyen : "+averageDegree(g));
        // mon propre calcul de dégre moyen
        double degre = (2 * g.getEdgeCount() )  / g.getNodeCount() ;
        System.out.println("mon propre calcule de  dégre moyen :"+degre);
        // le coefficient de clustering
        System.out.println("le coefficient de clustering :"+averageClusteringCoefficient(g));
        // le coefficient de clustering pour un réseau aléatoire
        System.out.println("le coefficient de clustering pour un réseau aléatoire de la même taille et du même degré moyen :"+averageDegree(g)/g.getNodeCount());

        /***
         *   question 3 : connexe,..
         */
        // réseau est-il connexe
        /*
        System.out.println("réseau est-il connexe ? " +isConnected(g));
        System.out.println(" le réseau est bien connexe");
        //Un réseau aléatoire de la même taille et degré moyen sera-t-il connexe
        //System.out.println(averageDegree(g)*2>Math.log(g.getNodeCount()));

        /**
         * question 4 : la distribution des degrés
         */
        //System.out.println(Arrays.toString(degreeDistribution(g)));
        /*
        int sommeNoeud = 0;
        int[] dd = Toolkit.degreeDistribution(g);
        for (int k = 0; k < dd.length; k++) {
            if (dd[k] != 0) {
                System.out.printf(Locale.US, "%6d%20.8f%n", k, (double)dd[k]/g.getNodeCount());
            }
            sommeNoeud = sommeNoeud + dd[k] ;
        }
        
        /**
         *  question 5 : la distance moyenne
         */
        // System.out.println(sommeNoeud);

        MesuresRI mesure =  new MesuresRI(g);
        System.out.println("la somme :"+mesure.sommeDistance());
        System.out.println("la distance Moyenne: "+mesure.distanceMoyenne());
        System.out.println(" la distribution distance "+mesure.distributionDistance()) ;
        mesure.genérationFichierDist();

        /**
         *  question 6 : les génerateurs
         */
        //generateurRandom

       // Graph g1 = mesure.generateurRandom(g.getNodeCount(), averageDegree(g));
        //System.out.println("les meseures de graphe g1 généré aleatoirement ");
        //mesure.mesureGenerator(g1);



        //generateur BarabasiAlbert
       // Graph g2 = mesure.generateurBarabasiAlbert(g.getNodeCount(), (int)averageDegree(g));
        //System.out.println("les meseures de graphe g2 Barbasi ");
        //mesure.mesureGenerator(g2);

//  *****************************  Propagation **********************************************************
/*
        Propagation propagation = new Propagation(g);
        double k = Toolkit.averageDegree(g);
        double seuilDBLP = k / propagation.dispersionDegre();
        System.out.println("<k> :"+k );
        System.out.println("<k²> :"+propagation.dispersionDegre());

        System.out.println("Le seuil épidémique du réseau (DBLP) λc = " + k +" / "+ propagation.dispersionDegre() + " = " +  seuilDBLP);

        Scenario1 scenari1 = new Scenario1(g);
        //scenari1.simulation();
        //scenari1.genereTextScenario1();

        Scenario2 scenario2 = new Scenario2(g);
        //scenario2.simulation();
        //scenario2.genereTextScenario2();

        Scenario3 scenario3 = new Scenario3(g);
        //scenario3.scenario3();
        //scenario3.genereTextScenario3();


        // --------------------------------------------------------

        // Graphe attachement préférentiel de même caractéristique que le réseau
        // --------------------------------------------------------

        //Test des 3 scenario pour le reseau Aleatoire
        System.out.println("Scénario 1 pour reseau aleatoire : ");
        Scenario1 scenarioReseauAlea = new Scenario1(g1);

        scenarioReseauAlea.simulation();

        /*
        System.out.println("Scénario 2 pour reseau aleatoire : ");
        Scenario2 scenario2ReseauAlea = new Scenario2(g1);

        scenario2ReseauAlea.genereTextScenario2();

        System.out.println("Scénario 3 pour reseau aleatoire : ");

        Scenario3 scenario3ReseauAlea = new Scenario3(g1);
        scenario3ReseauAlea.scenario3();



        //Test des 3 scenario pour le reseau Barbasi

        System.out.println("Scénario 1 pour reseau barbasi : ");
        Scenario1 scenarioReseauBarbasi = new Scenario1(g2);
        scenarioReseauBarbasi.simulation();

        System.out.println("Scénario 2 pour reseau barbasi : ");
        Scenario2 scenario2ReseauBarbasi = new Scenario2(g2);
        scenario2ReseauBarbasi.simulation();


        System.out.println("Scénario 3 pour reseau barbasi : ");

        Scenario3 scenario3ReseauBarbasi = new Scenario3(g2);
        scenario3ReseauBarbasi.scenario3();*/

    }
}
