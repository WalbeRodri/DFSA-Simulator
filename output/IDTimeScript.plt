set xlabel "Número de Etiquetas"
set ylabel "Tempo de Identificação (milissegundos)"
set monochrome
set key vertical top left
#set logscale y
set grid
#set pointsize 1
#set xtics (10, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
plot "Eom-Lee-IDTime-128.txt" u 1:2 t 'Eom-Lee' w lines, \
"IV-2-IDTime-128.txt" u 1:2 t 'IV-2' w lines, \
"Vogt-IDTime-128.txt" u 1:2 t 'Vogt-Imp1' w lines, \
"Vogt(Eom-Lee)-IDTime-128.txt" u 1:2 t 'Vogt-Imp2' w lines, \
"LowerBound-IDTime-128.txt" u 1:2 t 'LowerBound' w lines, \
"Schoute-IDTime-128.txt" u 1:2 t 'Schoute' w lines, \