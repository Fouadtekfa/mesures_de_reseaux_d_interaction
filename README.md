# TP 2 Mesures de réseaux d'interaction
- Fouad TEKFA
- M1 IWOCS
- 2022-2023


Dans ce TP il nous est demandé analyser un réseau de collaboration scientifique en informatique. Le réseau est extrait de DBLP et disponible sur SNAP.
# 1. Téléchargement et lecture de données avec GraphStream:
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
![capteure1](./capture_mesures/Capture1.png)
# 2. Les Mesures de base du réseau:
## 2.1. Nombre de noeuds:
pour calculer ne nombre totale de noeuds j'ai utilisé getNodeCount() qui nous retourne le nombre de noeud du notre graphe

## 2.2. Nombre de liens:
Pour calculer le nombre de liens j'ai utilisé la fonction getEdgeCount() qui retourne le nombre d'arêtes du notre graphe   
2.3. Degré moyen :
Pour calculer le degré moyen j'ai utilisé la fonction averageDegree après avoir importe la classe Toolkit
```java
import org.graphstream.algorithm.Toolkit;

```
## 2.4. coefficient de clustering:
j'ai utilisé verageClusteringCoefficient de la classe Toolkit qui nous retourne le coefficient de clustering de notre graphe passer en paramètre
## 2.5. Coefficient de clustering pour un reseau aleatoire :
pour calculer coefficient de clustering pour un réseau aléatoire de la même taille et du même degré moyen donc j'ai calculé (Degré_moyen)/(Nombre_de_noeuds)
## 2.6. Résultats obtenue:
![Résultats obtenue](./capture_mesures/Capture_2.png)
# 3. Troisième question
## 3.1. Le réseau est-il connexe ?
Un réseau est connexe si seulement si s'il est possible, à partir de n'importe quel noeud, de rejoindre les autres noeud et pour vérifier cela j'ai utilisé la méthode isConnected de la classe Toolkit qui qui m'a retourné vrai donc le réseau est connexe
```java
System.out.println(" Le réseau est-il connexe ?  ==>"+((Toolkit.isConnected(g)? "Oui" : "Non ")));
```
## 3.2 Un réseau aléatoire de la même taille et degré moyen sera-t-il connexe ?
Comme on a vu en cours :

![Régime connecté](./capture_mesures/Capture_4.png)


Non il n'est pas connexe car : le degré moyen <K> = 6.62208890914917 < ln(N) =12.666909386951092
## 3.3. À partir de quel degré moyen un réseau aléatoire avec cette taille devient connexe ?
Il faudra que le degré moyen sera supérieur à ln(N)=12.666909386951092
## 3.4. Résultats obtenue:
![Le réseau est-il connexe ](./capture_mesures/Capture_3.png)

# Quatrième question

## 4.1. Calcule de la distribution des degrés
La distribution de degrés $`p_k = \frac{N_k}{N}`$ est la probabilité qu'un nœud choisi au hasard ait degré $`k`$. On peut utiliser [`Toolkit.degreeDistribution()`]

Afin de pouvoir tracer avec gnuplot j'ai généré un [fichier](./src/resources/dd_dblp.dat) Contenant la distribution des degrés en utilisant le script suivant :
```java
   int[] dd = Toolkit.degreeDistribution(g);
        for (int k = 0; k < dd.length; k++) {
        if (dd[k] != 0) {
        bw.write(String.format(Locale.US, "%6d%20.8f%n", k, (double)dd[k] / g.getNodeCount()));
        }
```

## 4.2. En échelle linéaire :
On utilise ce [script](/src/resources/lineaire.gnu) pour tracer la distribution en échelle linéaire.

![En échelle linéaire ](./src/resources/echelle_lineaire.png)

## 4.2. En échelle Log Log:
On utilise ce [script](/src/resources/ehelle_log_log.gnu) pour tracer la distribution en échelle Log Log.

![En échelle Log Log ](./src/resources/echelle_log_log.png)

En traçant la distribution de degrés en échelle log-log on observe une ligne droite pendant plusieurs ordres de grandeur. Cela nous indique une loi de puissance :

```math   
p_k = C k^{-\gamma}
```
## 4.3. La distribution de Poisson avec la même moyenne pour comparaison :
On utilise ce [script](/src/resources/La_distribution_de_Poisson_avec_la_meme_moyenne.gnu) pour tracer La distribution de Poisson avec la même moyenne :

![La distribution de Poisson avec la même moyenne  ](./src/resources/La_distribution_de_Poisson_avec_la_meme_moyenne.png)

## 4.4. Tracer la distribution de Poisson avec la même moyenne
On utilise ce [script](/src/resources/plot_dd.gnu) pour tracer la distribution et estimer l'exposant de la loi de puissance.
    ![distribution des degrés](src/resources/dd_dblp.png)

# Cinquième question :
Maintenant on va calculer la distance moyenne dans le réseau. Le calcul des plus courts chemins entre 
toutes les paires de nœuds prendra plusieurs heures pour cette taille de réseau. C'est pourquoi on va 
estimer la distance moyenne par échantillonnage en faisant un parcours en largeur à partir de 1000 sommets 
choisis au hasard. L'hypothèse des six degrés de séparation se confirme-t-elle ? Est-ce qu'il s'agit d'un 
réseau petit monde ? Quelle sera la distance moyenne dans un réseau aléatoire avec les mêmes caractéristiques ? 
Tracez également la distribution des distances. Formulez une hypothèse sur la loi de cette distribution.
 ## 5.1. Calcule de la distance moyenne dans ce réseau :
Pour estimer la distance moyenne par échantillonnage, j'ai suivi les étapes suivantes :
- J'ai utilisé la méthode randomNodeSet de la classe Toolkit pour avoir les 1000 sommets au hasard.  
- J'ai utilisé l'algorithme de parcours en largeur BFI pour avoir la longueur du plus court chemin entre deux nœuds.
- J'ai calculé la somme des distances d'un nœud (le nœud source) vers tous les autres nœuds de l'échantillon.
- j'ai divisé la somme obtenue par le nombre de nœuds * 1000 pour obtenir une estimation de la distance moyenne de notre réseau.
### Voici les résultats obtenus:
    la somme = 2.175195856E9
    Max = 3.1708E8
    Distance Moyenne = 6.860085328623692

## 5.2. L'hypothèse des six degrés: 
Oui, l'hypothèse des six degrés de séparation se confirme vu que la Distance Moyenne = 6.860085328623692.
## 5.3. Petit Monde: 
Comme on a vu en cours la propriété petit monde est définie par : 

![Petit Monde](capture_mesures/Capture_petit_monde.png)

Oui, il s'agit d'un réseau petit monde, car Distance moyenne ≈ ln(N)/ln(k)

## 5.4.La distance moyenne dans un réseau aléatoire avec les mêmes caractéristiques:
D'après le cours Un réseau aléatoire avec les mêmes caractéristiques aura une distance moyenne similaire.

## 5.5. La distribution des distances
J'ai créé la méthode sauvegardeDistributionDistance(int NodeCount, List<Node> randomnode  ) afin de calcule la distribution de la distance puis generer le fichiers DistributionDistance.data.  
 
pour tracer la distribution des distances avec le [fichier](/src/resources/DistributionDistance.data) généré, j'ai utilisé ce [script](/src/resources/DistributionDistance.gnuplot) : 
   
![ distribution des distances ](src/resources/distributionDistance.png)
  
## 5.6. Hypothèse sur la loi de cette distribution :
- D'après le graphe, on peut déduire que la loi de distribution de distance suit une loi binomiale.

# Sixième question :
Utilisez les générateurs de GraphStream pour générer un réseau aléatoire et un réseau avec la méthode d'attachement préférentiel
(Barabasi-Albert) qui ont la même taille et le même degré moyen.
Refaites les mesures des questions précédentes pour ces deux réseaux. 
Les résultats expérimentaux correspondent-ils aux prédictions théoriques ? 
Comparez avec le réseau de collaboration. Que peut-on conclure ?
## 6.1 générer un réseau aléatoire 
- generateurRandom(double nbNoeuds , double degreeMoyen) : cette méthode et pour but de générer un réseau aléatoire, elle prend deux paramètres nombre de Nœuds et le dégrée Moyen, en utilisant la classe RandomGenerator de graphStream.     
## 6.2 générer un réseau avec la méthode d'attachement préférentiel (Barabasi-Albert) :
- generateurBarabasiAlbert(int nbNoeuds ,int degreeMoyen) : cette méthode et pour but de générer un réseau Barabasi-Albert , elle prend deux paramètres nombre de Nœuds et le dégrée Moyen, en utilisant la classe Barabasi-Albert de graphStream. 
## 6.3.Refaire les mesures des questions précédentes pour ces deux réseaux : 

| type de graphe | Nombre de noeuds | Nombre de liens    | Degré moyen      | coefficient de clustering    | connexe | Distance Moyenne                                                                                       |
|-------------|------------------|-----|------------------|-----|---------|--------------------------------------------------------------------------------------------------------|
| DBLP            |  317080          | 1049866    |   6.62208890914917  |   2.0884599814397534E-5  | oui     |6.860085328623692 |  
| RandomGraph | 317080           |  1049503   | 6.19799613952637 |  1.8928034176551887E-5   | Non     |          6.912789999369244                                                                                              |
| Barabàsi-Albert | 317080           | 1109288    | 6.99689674377441 | 3.8303829341658014E-4    | Oui     |      5.062236192758925                                                                                                  |

### distribution des degrés (RandomGraph ):  
On utilise ce [script](src/resources/dd_dblp_Random.gnuplot) pour tracer la distribution et estimer l'exposant de la loi de puissance. 

   ![distribution des degrés (RandomGraph )](src/resources/dd_dblp_random.png)

### distribution des degrés (Barabàsi-Albert ):
On utilise ce [script](src/resources/dd_dblp_B_A.gnuplot) pour tracer la distribution et estimer l'exposant de la loi de puissance

   ![distribution des degrés (Barabàsi-Albert )](src/resources/dd_dblpBarabasiAlbert.png)

###  La distribution des distances (RandomGraph ):
![ distribution des distances RandomGraph ](src/resources/distributionDistanceRandom.png)
###  La distribution des distances (Barabàsi-Albert ):
![ distribution des distances Barabàsi-Albert  ](src/resources/distributionDistanceBarabasiAlbert.png) 

## 6.4. Les résultats expérimentaux et théoriques: 
   - Les résultats sont preceque similaires entre les meseures théorique et pratique.  

## 6.5. Comparaison : 





# TP 3 Propagation

Nos collaborateurs scientifiques communiquent souvent par mail. Malheureusement pour eux, les pièces jointes de ces mails contiennent parfois des virus informatiques. On va étudier la propagation d'un virus avec les hypothèses suivantes :

- Un individu envoie en moyenne un mail par semaine à chacun de ses collaborateurs.
- Un individu met à jour son anti-virus en moyenne deux fois par mois. Cela nettoie son système mais ne le protège pas de nouvelles infections car le virus mute.
L'épidémie commence avec un individu infecté (patient zéro).
## 1.  :  
Quel est le taux de propagation du virus ? Quel est le seuil épidémique du réseau ? Comparez avec le seuil théorique d'un réseau aléatoire du même degré moyen.
### 1.1. Taux de propagation du virus :
Pour prédire si la maladie persiste, on définit le taux de
propagation λ par  :
```math               
λ = β / µ
``` 
- β est la probabilité de transmettre dans une unité de temps dans notre cas :
```math
β = 1/7   
```
avec t = 1 jour 
- μ est le taux de guérison dans notre cas:
```math                                       
β = 1 / 14                   
```   
avec t = 1 mois
Donc :
```math                                       
λ = β/µ = 14/7= 2                      
```               

### 1.2 Seuil épidémique : 
```math
λ_c = 〈k〉/〈k2〉≈0.045984724362225844 ≈ 0,046 
```


### 1.3 le seuil théorique d'un réseau aléatoire du même degré moyen :
```math
 λ_c = 1/{<K>+1} ≈ 0.13119762993051035 ≈0.131 
```
### 1.4.Résultats obtenue: 
![](./Capture_propagation/Capture1_propagation.png)

### 2. 
Simulez la propagation du virus jour par jour pendant trois mois avec les scénarios suivants :

On ne fait rien pour empêcher l'épidémie
On réussit à convaincre 50 % des individus de mettre à jour en permanence leur anti-virus (immunisation aléatoire)
On réussit à convaincre 50 % des individus de convaincre un de leurs contacts de mettre à jour en permanence son anti-virus (immunisation sélective).
Pour chacun des trois scénarios, tracez l'évolution de la fraction d'infectés de la population non immunisée. Que peut-on conclure ?
