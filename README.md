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
![plot](./Capture%20d’écran%201.png)


