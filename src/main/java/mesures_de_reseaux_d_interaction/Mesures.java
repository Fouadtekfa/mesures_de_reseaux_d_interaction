package mesures_de_reseaux_d_interaction;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Hello world!
 *
 */
public class Mesures

{
    public static Graph generateurRandom(double nbNoeuds , double degreeMoyen){
        System.out.println("La génération du graphe aléatoire ");
        Graph graph = new SingleGraph("RandomGraph");
        Generator gen = new RandomGenerator(degreeMoyen);
        gen.addSink(graph);
        gen.begin();
        for (int i = 0; i < nbNoeuds; i++)
            gen.nextEvents();
        gen.end();
        System.out.println("Fin génération du graphe aléatoire");
        return graph;
    }


    public static Graph generateurBarabasiAlbert(int nbNoeuds ,int degreeMoyen) {
        System.out.println("La génération du graphe Barabàsi-Albert ");
        Graph graph = new SingleGraph("Barabàsi-Albert");
            // Between 1 and  degreeMoyen new links per node added.
        Generator gen = new BarabasiAlbertGenerator(degreeMoyen);
                    // Generate nbNoeuds nodes:
        gen.addSink(graph);
        gen.begin();

        for (int i = 0; i < nbNoeuds; i++) {
            gen.nextEvents();
        }
        System.out.println("Fin génération du graphe Barabàsi-Albert");
      return graph;
    }

    public static void main( String[] args )

    {
        System.setProperty("org.graphstream.ui", "swing");
        Graph g = new DefaultGraph("g");
        String filePath = "./src/resources/com-dblp.ungraph.txt";

        FileSource fs = new FileSourceEdge();
        fs.addSink(g);

        try {
            fs.readAll(filePath);
           // g.display();
            System.out.println("=================debut de lecteur===============");
           System.out.println("=======Quelques mesures de base:======");
            //getNodeCount() qui nous retourne le nombre de noeud que contient le graphe g
            System.out.println("1- Nombre de noeuds :" +g.getNodeCount() );
           //getEdgeCount qui nous retourne le nombre d'arêtes que de notre graphe g dans notre cas
            //c'est le nombre de liens
            System.out.println("2-Nombre de liens :" + g.getEdgeCount());
            // averageDegree de la classe Toolkit qui nous retourne le degré moyen de graphe g
            System.out.println("3-Degré moyen :" + Toolkit.averageDegree(g));
            //verageClusteringCoefficient de la classe Toolkit qui nous retourne le coefficient de clustering
            System.out.println("4-coefficient de clustering :" + Toolkit.averageClusteringCoefficient(g));
            //(Degré_moyen)/(Nombre_de_noeuds)
            System.out.println("5-coefficient de clustering pour un réseau aléatoire de la même taille et du même degré moyen :"+ (Toolkit.averageDegree(g) / g.getNodeCount()));
            //Troisième question
            System.out.println("=====Troisième question=======");
            //la methode isConnected de la calasse Toolkitnos permet de vérifier si le réseau  est connexe ou pas
            System.out.println(" Le réseau est-il connexe ?  ==>"+((Toolkit.isConnected(g)? "Oui" : "Non ")));
            //pour cette parti il faut calculer log (nombre de noeuds) et le degrer moyenn et faire une comparaison
           double Degre_moyen= Toolkit.averageDegree(g);
           double ln_de_nb_noeuds = Math.log(g.getNodeCount());
            System.out.println("Un réseau aléatoire de la même taille et degré moyen sera-t-il connexe ?");
           if(Degre_moyen>ln_de_nb_noeuds){
               System.out.println("  Oui il est connexe car:  " +("degrè moyen = ")+Degre_moyen +" > " +("log(Nombre de noeuds) =") +ln_de_nb_noeuds);
           }else{
               System.out.println("  Non il n'est pas connexe car:  " +("degrè moyen = ")+Degre_moyen +" < " +("log(Nombre de noeuds) =")+ln_de_nb_noeuds);
           }
           System.out.println("===================Quatrième question========= ");

            int[] dd = Toolkit.degreeDistribution(g);
            try {
                String filepath = System.getProperty("user.dir") + File.separator + "./src/resources/dd_dblp.dat";
                FileWriter fw = new FileWriter(filepath);
                BufferedWriter bw = new BufferedWriter(fw);

            for (int k = 0; k < dd.length; k++) {
                if (dd[k] != 0) {
                    bw.write(String.format(Locale.US, "%6d%20.8f%n", k, (double)dd[k] / g.getNodeCount()));
                }
            }
                bw.close();
                System.out.println("Le fichier dd_dblp.dat a été généré avec succès dans le répertoire des ressources ");
            }
            catch (IOException e) {
                e.printStackTrace();
            }


            System.out.println(" =====Question5 calculer de distance moyenne=======");

            double Totale_distance=0;
            List<Node> randomnode= Toolkit.randomNodeSet(g,1000);

            for (int i = 0; i < 1000; i++) {
                Node node = randomnode.get(i);
                BreadthFirstIterator bf = new BreadthFirstIterator(node);
                while (bf.hasNext()) {
                    Totale_distance += (bf.getDepthOf(bf.next()));

                }
            }

             double DistanceMoyenne= Totale_distance/(g.getNodeCount() * 1000);
              System.out.println("Distance Moyenne = " +DistanceMoyenne);


        } catch( IOException e) {
            e.printStackTrace();
        }finally {
            fs.removeSink(g);
            System.out.println("===========Fin de lecteur==============");
        }

        Graph random =generateurRandom(g.getNodeCount(), Toolkit.averageDegree(g));
        Graph BarabasiAlbert=generateurBarabasiAlbert(g.getNodeCount(),(int)Toolkit.averageDegree(g));
        System.out.println("=======Quelques mesures de base graphe random:======");
        System.out.println("1- Nombre de noeuds :" +random.getNodeCount() );
        //getEdgeCount qui nous retourne le nombre d'arêtes que de notre graphe g dans notre cas
        //c'est le nombre de liens
        System.out.println("2-Nombre de liens :" + random.getEdgeCount());
        // averageDegree de la classe Toolkit qui nous retourne le degré moyen de graphe g
        System.out.println("3-Degré moyen :" + Toolkit.averageDegree(random));
        //verageClusteringCoefficient de la classe Toolkit qui nous retourne le coefficient de clustering
        System.out.println("4-coefficient de clustering :" + Toolkit.averageClusteringCoefficient(random));
        //(Degré_moyen)/(Nombre_de_noeuds)
        System.out.println("5-coefficient de clustering pour un réseau aléatoire de la même taille et du même degré moyen :"+ (Toolkit.averageDegree(random) / random.getNodeCount()));
        System.out.println("Un réseau aléatoire de la même taille et degré moyen sera-t-il connexe ?");
        System.out.println(" Le réseau est-il connexe ?  ==>"+((Toolkit.isConnected(random)? "Oui" : "Non ")));
        if( (Toolkit.averageDegree(random))>(Math.log(random.getNodeCount()))){
            System.out.println("  Oui il est connexe car:  " +("degrè moyen = ")+ Toolkit.averageDegree(random) +" > " +("log(Nombre de noeuds) =") +Math.log(random.getNodeCount()));
        }else{
            System.out.println("  Non il n'est pas connexe car:  " +("degrè moyen = ")+ Toolkit.averageDegree(random) +" < " +("log(Nombre de noeuds) =")+Math.log(random.getNodeCount()));
        }
        System.out.println("=======Quelques mesures de base graphe Barabàsi-Albert :======");
        System.out.println("1- Nombre de noeuds :" +BarabasiAlbert.getNodeCount() );
        //getEdgeCount qui nous retourne le nombre d'arêtes que de notre graphe g dans notre cas
        //c'est le nombre de liens
        System.out.println("2-Nombre de liens :" + BarabasiAlbert.getEdgeCount());
        // averageDegree de la classe Toolkit qui nous retourne le degré moyen de graphe g
        System.out.println("3-Degré moyen :" + Toolkit.averageDegree(BarabasiAlbert));
        //verageClusteringCoefficient de la classe Toolkit qui nous retourne le coefficient de clustering
        System.out.println("4-coefficient de clustering :" + Toolkit.averageClusteringCoefficient(BarabasiAlbert));
        //(Degré_moyen)/(Nombre_de_noeuds)
        System.out.println("5-coefficient de clustering pour un réseau aléatoire de la même taille et du même degré moyen :"+ (Toolkit.averageDegree(BarabasiAlbert) / BarabasiAlbert.getNodeCount()));
        System.out.println(" Le réseau est-il connexe ?  ==>"+((Toolkit.isConnected(BarabasiAlbert)? "Oui" : "Non ")));
        System.out.println("Un réseau aléatoire de la même taille et degré moyen sera-t-il connexe ?");
        if( (Toolkit.averageDegree(BarabasiAlbert))>(Math.log(BarabasiAlbert.getNodeCount()))){
            System.out.println("  Oui il est connexe car:  " +("degrè moyen = ")+ Toolkit.averageDegree(BarabasiAlbert) +" > " +("log(Nombre de noeuds) =") +Math.log(BarabasiAlbert.getNodeCount()));
        }else{
            System.out.println("  Non il n'est pas connexe car:  " +("degrè moyen = ")+ Toolkit.averageDegree(BarabasiAlbert) +" < " +("log(Nombre de noeuds) =")+Math.log(BarabasiAlbert.getNodeCount()));
        }
    }


}
