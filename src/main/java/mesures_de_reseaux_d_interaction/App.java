package mesures_de_reseaux_d_interaction;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

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
            int[] dd = Toolkit.degreeDistribution(g);
            try {
                String filepath = System.getProperty("user.dir") + File.separator + "dd_dblp.dat";
                FileWriter fw = new FileWriter(filepath);
                BufferedWriter bw = new BufferedWriter(fw);
            for (int k = 0; k < dd.length; k++) {
                if (dd[k] != 0) {
                    bw.write(String.format(Locale.US, "%6d%20.8f%n", k, (double)dd[k] / g.getNodeCount()));
                }
            }
                bw.close();
                System.out.println(" le fichier dd_dblp.dat est bien générer!!!!!!!!!!");
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch( IOException e) {
            e.printStackTrace();
        }finally {
            fs.removeSink(g);
            System.out.println("===========Fin de lecteur==============");
        }
///Le réseau est-il connexe ? Un réseau aléatoire de la même taille et degré moyen sera-t-il connexe ? À partir de quel degré moyen un réseau aléatoire avec cette taille devient connexe ?

    }


}
