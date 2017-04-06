set xlabel "Número de Etiquetas"
set ylabel "Tempo de Identificação (milissegundos)"
set monochrome
set key vertical top left
set grid
plot "Eom-Lee-IDTime-128.txt" u 1:2 t 'Eom-Lee' w lines, \
"Vogt-IDTime-128.txt" u 1:2 t 'Vogt' w lines, \
"Vogt(Eom-Lee)-IDTime-128.txt" u 1:2 t 'Vogt(Eom-Lee)' w lines, \
"IV-2-IDTime-128.txt" u 1:2 t 'IV-2' w lines, \
"LowerBound-IDTime-128.txt" u 1:2 t 'LowerBound' w lines, \
"Schoute-IDTime-128.txt" u 1:2 t 'Schoute' w lines, \