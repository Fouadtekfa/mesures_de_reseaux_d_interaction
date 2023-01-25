set terminal png
set title "Degree distribution Barabasi-Albert "
set xlabel 'k'
set ylabel 'p(k)'
set output 'dd_dblpBarabasiAlbert.png'

set logscale xy
set yrange [1e-6:1]

# Poisson
lambda = 6.62208890914917
poisson(k) = lambda ** k * exp(-lambda) / gamma(k + 1)

# on va fitter une fonction linéaire en log-log

f(x) = lc - gamma * x
fit f(x) 'dd_dblpBarabasiAlbert.dat' using (log($1)):(log($2)) via lc, gamma

c = exp(lc)
power(k) = c * k ** (-gamma)

plot 'dd_dblpBarabasiAlbert.dat' title 'DBLP', \
  poisson(x) title 'Poisson law', \
  power(x) title 'Power law'
