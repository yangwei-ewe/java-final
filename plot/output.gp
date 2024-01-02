#set xlabel "single"
set ylabel "# / sec"
set terminal png font " Times_New_Roman,12 "
set output "output.png"
set term png size 1440,960
set key left
#set yrange [0:150]
set datafile separator ','

min_distance=50

plot \
   "data.csv" using ($0 * min_distance):2:xticlabels(1) with linespoints linewidth 4 title "single", \
   "data.csv" using ($0 * min_distance):2:($2) with labels offset 0,1 notitle
