package mesures_de_reseaux_d_interaction;

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
        g.display();

        try {
            fs.readAll(filePath);
            System.out.println("debut de lecteur");
        } catch( IOException e) {
            e.printStackTrace();
        }finally {
            fs.removeSink(g);
            System.out.println("Fin de lecteur");
        }

    }

}
