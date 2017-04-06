set xlabel "Número de Etiquetas"
set ylabel "Número de Slots"
set monochrome
set key vertical top left
#set logscale y
set grid
set pointsize 2
plot "ILCM-sbs.txt" u 1:2 t 'ILCM-sbs' w linespoints pointtype 0