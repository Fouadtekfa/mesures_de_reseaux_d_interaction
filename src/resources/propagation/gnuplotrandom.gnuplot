set terminal postscript eps enhanced color 15
set encoding utf8
set terminal png enhanced font 'Roboto,'
set output "random.png"
set title "Random"
set yrange [0 : 300000]
set xlabel "Jour" 
set ylabel "Nombre d'infectée par jour"
plot "Randomscenario1.data" t "Scenario1", \
     "Randomscenario2.data" t "Scenario2", \
     "RandomScenario3.data" t "Scenario3" 


