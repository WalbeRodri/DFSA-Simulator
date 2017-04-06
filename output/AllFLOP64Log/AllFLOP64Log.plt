set xlabel "Número de Etiquetas"
set ylabel "FLOP"
set monochrome
set key vertical top left
set logscale y
set grid
plot "Eom-Lee-FLOP-64.txt" u 1:2 t 'Eom-Lee' w lines, \
"Vogt-FLOP-64.txt" u 1:2 t 'Vogt' w lines, \
"Vogt(Eom-Lee)-FLOP-64.txt" u 1:2 t 'Vogt(Eom-Lee)' w lines, \
"IV-2-FLOP-64.txt" u 1:2 t 'IV-2' w lines, \
"LowerBound-FLOP-64.txt" u 1:2 t 'LowerBound' w lines, \
"Schoute-FLOP-64.txt" u 1:2 t 'Schoute' w lines, \