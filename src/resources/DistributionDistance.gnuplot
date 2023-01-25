set terminal wxt
set terminal png
set encoding utf8
set title "La Distribution Des Distances Graphe Barabasi-Albert"
set xtics 2,2
set output "distributionDistanceBarabasiAlbert.png"
plot"distribution_des_distancesBarabasiAlbert.data" t"Distribution de Distance" with linesp lt 1 pt 1
