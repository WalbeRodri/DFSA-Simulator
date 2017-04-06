set xlabel "Número de Etiquetas"
set ylabel "Custo FLOP Total"
set monochrome
set key vertical top left
set logscale y
set grid
set pointsize 2
set xtics (100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
plot "Eom-Lee-FLOP-128.txt" u 1:2 t 'Eom-Lee-128' w lines, \
"Eom-Lee-FLOP-64.txt" u 1:2 t 'Eom-Lee-64' w lines, \