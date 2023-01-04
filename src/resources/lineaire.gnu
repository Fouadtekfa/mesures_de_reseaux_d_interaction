set terminal png
set title " La distribution des degrés  en échelle linéaire"
set xlabel "k"
set ylabel "p(k)"
set output "echelle_lineaire.png"
plot"dd_dblp.dat" title 'La distributio en échelle Linéaire' with lines linewidth 3

