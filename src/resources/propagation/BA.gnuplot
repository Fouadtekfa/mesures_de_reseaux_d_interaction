set terminal postscript eps enhanced color 15
set encoding utf8
set terminal png enhanced font 'Roboto,'
set output "BarabasiAlbertscenario1.png"
set title "BarabasiAlbertscenario1"
set yrange [0 : 300000]
set xlabel "Jour" 
set ylabel "Nombre d'infectée par jour"
plot "BarabasiAlbertscenario1.data" t "Scenario1", \
     "BarabasiAlbertscenario2.data" title "Scenario2"


