set terminal png
set title "50 % des individus mis à jour en permanence leur anti-virus (immunisation aléatoire)"
set yrange [0 : 300000]
set output 'Scenario2.png'
set xlabel "Jour"
set ylabel "Nombre d'infectée par jour"
plot"scenario2.data" t "Scenario2"
