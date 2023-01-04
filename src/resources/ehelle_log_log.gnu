set terminal png
set title "Degree distribution"
set xlabel 'k'
set ylabel 'p(k)'
set logscale x
set logscale y
set output 'echelle_log_log.png'
plot"dd_dblp.dat" title 'La distributio en échelle Log log'
