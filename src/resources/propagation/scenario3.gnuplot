set terminal png
set title "immunisation sélective"
set yrange [0 : 300000]
set output 'Scenario3.png'
set xlabel "Jour"
set ylabel "Nombre d'infectée par jour"
plot"Scenario3.data" t "Scenario3"
