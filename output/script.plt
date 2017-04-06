set xlabel "Número de Etiquetas"
set ylabel "Número de Slots"
set monochrome
set key vertical top left
#set logscale y
set grid
set pointsize 2
set xtics (0, 10, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
plot "Eom-Lee-totalSlots-64.txt" u 1:2 t 'Eom-Lee' w linespoints pt 4, \
"Vogt-totalSlots-64.txt" u 1:2 t 'Vogt' w linespoints pt 6, \
"Vogt-Imp1-totalSlots-64.txt" u 1:2 t 'Vogt-Imp1' w linespoints pt 10, \
"Vogt(Eom-Lee)-totalSlots-64.txt" u 1:2 t 'Vogt(Eom-Lee)' w linespoints pt 12, \
"IV-2-totalSlots-64.txt" u 1:2 t 'IV-2' w linespoints pt 14, \
"LowerBound-totalSlots-64.txt" u 1:2 t 'LowerBound' w linespoints pt 6, \
"Schoute-totalSlots-64.txt" u 1:2 t 'Schoute' w linespoints pt 8, \