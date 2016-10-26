package charts;

import simulator.SimulatorClass;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class XChart {

	public static void main(String[] args) throws Exception {
				
		double[] xData = new double[] { 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 };
		
		double[] yData = new double[xData.length];
		Thread simulator = new Thread(new SimulatorClass(yData, xData));
		simulator.start();
		
		simulator.join();
		
		// Create Chart
		XYChart chart = QuickChart.getChart("Estimadores RFID", "Número de etiquetas", "Slots em colisão", "Schoute", xData, yData);
		chart.getStyler().setYAxisMin(0.0);
		chart.getStyler().setXAxisMin(xData[0]);

		// Show it
		new SwingWrapper(chart).displayChart();

	}
}