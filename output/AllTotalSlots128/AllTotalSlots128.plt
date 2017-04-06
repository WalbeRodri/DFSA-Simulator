set xlabel "Número de Etiquetas"
set ylabel "Número de Slots"
set monochrome
set key vertical top left
#set logscale y
set grid
set pointsize 2
#set xtics (0, 10, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
plot "Eom-Lee-totalSlots-128.txt" u 1:2 t 'Eom-Lee' w lines, \
"Vogt-totalSlots-128.txt" u 1:2 t 'Vogt' w lines, \
"Vogt(Eom-Lee)-totalSlots-128.txt" u 1:2 t 'Vogt(Eom-Lee)' w lines, \
"IV-2-totalSlots-128.txt" u 1:2 t 'IV-2' w lines, \
"LowerBound-totalSlots-128.txt" u 1:2 t 'LowerBound' w lines, \
"Schoute-totalSlots-128.txt" u 1:2 t 'Schoute' w lines, \