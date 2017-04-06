set xlabel "Número de Etiquetas"
set ylabel "Número de Slots"
set monochrome
set key vertical top left
#set logscale y
set grid
set pointsize 2
plot "IV-2-totalSlots-128.txt" u 1:2 t 'IV-2 EQ.3 Imp. 1' w lines, \
"IV-2-totalSlots-1288.txt" u 1:2 t 'IV-2 EQ.3 Imp. 2' w lines, \