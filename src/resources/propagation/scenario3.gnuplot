set terminal png
set title "On ne fait rien pour empêcher l'épidémie"
set yrange [0 : 300000]
set output 'Scenario3.png'
set xlabel "Jour"
set ylabel "Nombre d'infectée par jour"
plot"testScenario3.data" t "Scenario3"
