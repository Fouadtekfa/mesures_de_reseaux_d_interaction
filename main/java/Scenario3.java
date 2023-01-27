import org.apache.commons.math.random.RandomData;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static org.graphstream.algorithm.Toolkit.randomNodeSet;

public class Scenario3 {

    private Graph graph;
    private static int nbJours = 84;
    static int[] scenario3 = new int[nbJours];

    Random random = new Random();


    public Scenario3(Graph graph){
        this.graph = graph;
    }


    public void scenario3() {
        ArrayList<Node> CanBeInfectes = new ArrayList<>();// liste susceptible
        ArrayList<Node> etatS3 = new ArrayList<>(); // l'etat du scénario 3

         for (Node node : this.graph) {
            node.setAttribute("healthy", "true");
            node.setAttribute("imu", "false");
            CanBeInfectes.add(node);
        }

         List<Node> groupe0;
        double DegreSom=0,DegreSom1=0 ;
        int totalGrp1 = 0 ;
         groupe0 = randomNodeSet(this.graph, this.graph.getNodeCount() / 2);


          for (Node n : groupe0) {
            DegreSom += n.getDegree();
             if(n.getDegree()!=0){
                Node VoisinImu = n.getEdge(random.nextInt(n.getDegree())).getOpposite(n);
                VoisinImu.setAttribute("imu", "true");}
        }


        Iterator<Node> iteratorInfectes = CanBeInfectes.iterator();
         while (iteratorInfectes.hasNext()) {
            if (iteratorInfectes.next().getAttribute("imu") == "true") {
                iteratorInfectes.remove();

            }
        }

        double AveragesomDegreeCanbeInfectes = 0.0 ;
        double AveragesomDegreeCanbeInfectesCaree = 0.0 ;

         for (Node n : CanBeInfectes)
        {
            AveragesomDegreeCanbeInfectes += n.getDegree();
            AveragesomDegreeCanbeInfectesCaree += (n.getDegree()*n.getDegree());

        }

        AveragesomDegreeCanbeInfectes = AveragesomDegreeCanbeInfectes/CanBeInfectes.size();
        AveragesomDegreeCanbeInfectesCaree = AveragesomDegreeCanbeInfectesCaree/CanBeInfectes.size();

         System.out.print("Seuil epidimique stratégie d'immunisation sélective: "+AveragesomDegreeCanbeInfectes/AveragesomDegreeCanbeInfectesCaree +"\n");

         for(Node n : this.graph){
            if (n.getAttribute("imu") == "true"){
                DegreSom1 += n.getDegree();
                totalGrp1++;}
        }

         Node PremierInfecteS3 = CanBeInfectes.get(random.nextInt(CanBeInfectes.size()));
        PremierInfecteS3.setAttribute("healthy", "false");
        etatS3.add(PremierInfecteS3);


        for (int i = 0; i < nbJours; i++) {
            for (Node n : this.graph) {
                 if (n.getAttribute("healthy") == "false") {
                    for (Edge e : n) {
                        Node voisin = e.getOpposite(n);

                          if ((random.nextInt(7) == 1) && voisin.getAttribute("imu")!= "true") {
                            if (voisin.getAttribute("healthy") == "true") {
                                voisin.setAttribute("healthy", "false");
                                etatS3.add(voisin);
                            }
                        }
                    }
                }
            }

            Iterator<Node> iteratorS3 = etatS3.iterator();

             while (iteratorS3.hasNext()) {
                if (random.nextInt(14) == 1) {
                    iteratorS3.next().setAttribute("healthy", "true");
                    iteratorS3.remove();
                } else {
                    iteratorS3.next();
                }


            }

             scenario3[i] = etatS3.size();
        }

         for (int i = 0; i < nbJours; i++) {
            int a = i + 1;
            System.out.println(a + " " + scenario3[i]);
        }

         System.out.println("degree groupe 0 :  " + DegreSom/groupe0.size());
         System.out.println("degree groupe 1 : " + DegreSom1/totalGrp1);

    }




    public void genereTextScenario3() {
        try {
            System.out.println("..");
            // on crée le fichier
            PrintWriter fichier = new PrintWriter(new FileWriter("Scénario/sc3.txt"));
            for (int i = 0; i < nbJours; i++) {
                fichier.write(i + "   " + scenario3[i]++);// on ajoute les nombre infecté au fichier
                fichier.println();
            }
            fichier.close();// on ferme le fichier
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
