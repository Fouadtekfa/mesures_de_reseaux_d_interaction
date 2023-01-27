set terminal png
set title "On ne fait rien pour empêcher l'épidémie"
set yrange [0 : 300000]
set output 'Scenario1.png'
set xlabel "Jour"
set ylabel "Nombre d'infectée par jour"
plot"scenario1.data" t "Scenario1"
