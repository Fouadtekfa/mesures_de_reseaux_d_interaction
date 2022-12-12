package mesures_de_reseaux_d_interaction;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
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
            // (Degré_moyen)/(Nombre_de_noeuds)
            System.out.println("5-coefficient de clustering pour un réseau aléatoire de la même taille et du même degré moyen :"+ (Toolkit.averageDegree(g) / g.getNodeCount()));
        } catch( IOException e) {
            e.printStackTrace();
        }finally {
            fs.removeSink(g);
            System.out.println("===========Fin de lecteur==============");
        }


    }


}
