set xlabel "Número de Etiquetas"
set ylabel "Custo FLOP Total"
set monochrome
set key vertical top left
set logscale y
set grid
set pointsize 2
plot "Eom-Lee-FLOP-64.txt" u 1:2 t 'Eom-Lee' w lines