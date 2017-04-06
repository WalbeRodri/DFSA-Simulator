set xlabel "Número de Etiquetas"
set ylabel "Número de Iterações"
set monochrome
set key vertical top left
set logscale y
set grid
set pointsize 2
set xtics (100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
plot "Eom-Lee-Iterations-128.txt" u 1:2 t 'Eom-Lee' w lines