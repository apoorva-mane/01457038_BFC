package BFC;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import java.util.ArrayList;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;
import org.jfree.chart.ChartUtilities;
public class UploadChart extends ApplicationFrame{
	static long up_start,up_end,d_start,d_end;
	static String title;
public UploadChart(String paramString,long us,long ue,long ds,long de){
	super(paramString);
	up_start = us;
	up_end = ue;
	d_start = ds;
	d_end = de;
	JPanel localJPanel = createDemoPanel();
    localJPanel.setPreferredSize(new Dimension(800, 370));
    JScrollPane jsp = new JScrollPane(localJPanel);
	setContentPane(localJPanel);
}
private static CategoryDataset createDataset(){
	DefaultCategoryDataset localDefaultCategoryDataset = new DefaultCategoryDataset();
	localDefaultCategoryDataset.addValue((up_end-up_start),"Upload Time","Download Time");
	localDefaultCategoryDataset.addValue((d_end-d_start),"Download Time","Download Time");
	return localDefaultCategoryDataset;
}
public void windowClosing(WindowEvent we){
	this.setVisible(false);
}
private static JFreeChart createChart(CategoryDataset paramCategoryDataset){
	JFreeChart localJFreeChart = ChartFactory.createBarChart(title,"Upload/Download", "Time (M.S)", paramCategoryDataset, PlotOrientation.VERTICAL, true, true, false);
    CategoryPlot localCategoryPlot = (CategoryPlot)localJFreeChart.getPlot();
    localCategoryPlot.setDomainGridlinesVisible(true);
    localCategoryPlot.setRangeCrosshairVisible(true);
    localCategoryPlot.setRangeCrosshairPaint(Color.blue);
    NumberAxis localNumberAxis = (NumberAxis)localCategoryPlot.getRangeAxis();
    localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    BarRenderer localBarRenderer = (BarRenderer)localCategoryPlot.getRenderer();
    localBarRenderer.setDrawBarOutline(false);
    GradientPaint localGradientPaint1 = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
    GradientPaint localGradientPaint2 = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, new Color(0, 64, 0));
    GradientPaint localGradientPaint3 = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, new Color(64, 0, 0));
    localBarRenderer.setSeriesPaint(0, localGradientPaint1);
    localBarRenderer.setSeriesPaint(1, localGradientPaint2);
    localBarRenderer.setSeriesPaint(2, localGradientPaint3);
    localBarRenderer.setLegendItemToolTipGenerator(new StandardCategorySeriesLabelGenerator("Tooltip: {0}"));
    CategoryAxis localCategoryAxis = localCategoryPlot.getDomainAxis();
    localCategoryAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.5235987755982988D));
    return localJFreeChart;
}
public static JPanel createDemoPanel(){
	JFreeChart localJFreeChart = createChart(createDataset());
	return new ChartPanel(localJFreeChart);
}
}