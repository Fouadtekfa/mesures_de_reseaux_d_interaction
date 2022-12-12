# TP 1 Mesures de réseaux d'interaction
- Fouad TEKFA
- M1 IWOCS
- 2022-2023


Dans ce TP il nous est demandé analyser un réseau de collaboration scientifique en informatique. Le réseau est extrait de DBLP et disponible sur SNAP.
1. Téléchargement et lecteur de donne avec GraphStream:  
Pour commencer j'ai tout d'abord téléchargé le fichier contenant la structure de notre réseau de DBLP qui es disponible sur [SNAP](https://snap.stanford.edu/data/com-DBLP.html),
puis pour les lire j'ai utilisé la classe [FileSourceEdge()](https://data.graphstream-project.org/api/gs-core/current/org/graphstream/stream/file/FileSourceEdge.html) de GraphStream en regardant la [Documentation](https://graphstream-project.org/doc/Tutorials/Reading-files-using-FileSource/) voici le pseudo code utilisé
```java
try {
        fs.readAll(filePath);
        System.out.println("debut de lecteur");
        } catch( IOException e) {
        e.printStackTrace();
        }finally {
        fs.removeSink(g);
        System.out.println("Fin de lecteur");
        }
```  
j'ai quand même essayé de visualiser le graphe qui était trop long a visualisé pour cette parti voici donc un capteur des résultats obtenu
![plot](./Capture1.png)
2. Les Mesure de base du réseau:  
2.1. Nombre de noeuds:     
pour calculer ne nombre totale de noeuds j'ai utilisé getNodeCount() qui nous retourne le nombre de noeud du notre graphe

2.2. Nombre de liens:   
Pour calculer le nombre de liens j'ai utilisé la fonction getEdgeCount() qui retourne le nombre d'arêtes du notre graphe   
   2.3. Degré moyen :
Pour calculer le degré moyen j'ai utilisé la fonction averageDegree après avoir importe la classe Toolkit   
```java
import org.graphstream.algorithm.Toolkit;

```
2.4. coefficient de clustering:   
j'ai utilisé verageClusteringCoefficient de la classe Toolkit qui nous retourne le coefficient de clustering de notre graphe passer en paramètre    
2.5. Coefficient de clustering pour un reseau aleatoire :   
pour calculer coefficient de clustering pour un réseau aléatoire de la même taille et du même degré moyen donc j'ai calculé (Degré_moyen)/(Nombre_de_noeuds)   
2.6. Résultats obtenue:
![Résultats obtenue](./Capture%202.png)

