set terminal png
set title "Degree distribution"
set xlabel 'k'
set ylabel 'p(k)'
set output 'La_distribution_de_Poisson_avec_la_meme_moyenne.png'

set logscale xy
set yrange [1e-6:1]

# Poisson
lambda = 6.62208890914917
poisson(k) = lambda ** k * exp(-lambda) / gamma(k + 1)

plot 'dd_dblp.dat' title 'DBLP', \
  poisson(x) title 'Poisson law', \

