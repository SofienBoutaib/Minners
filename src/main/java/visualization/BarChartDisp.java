/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

/**
 *
 * @author asus
 */
    import org.jfree.chart.ChartFactory ;
    import org.jfree.chart.ChartPanel ;
    import org.jfree.chart.JFreeChart ;
    import org.jfree.chart.plot.PlotOrientation ;
    import org.jfree.data.category.CategoryDataset ;
    import org.jfree.data.category.DefaultCategoryDataset ;

    import javax.swing.BorderFactory ;
    import javax.swing.JFrame ;
    import java.awt.Color ;
    import java.awt.EventQueue ;
import java.util.ArrayList;

    public class BarChartDisp extends JFrame {

        public BarChartDisp(ArrayList<String> namesMethod, ArrayList<Integer> NumOcc) {

            initUI(namesMethod,NumOcc);
        }

        private void initUI(ArrayList<String> namesMethod, ArrayList<Integer> NumOcc) {

            CategoryDataset dataset = createDataset(namesMethod,NumOcc);

            JFreeChart chart = createChart(dataset);
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            chartPanel.setBackground(Color.white);
            add(chartPanel);

            pack();
            setTitle("Bar chart");
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        private CategoryDataset createDataset(ArrayList<String> namesMethod, ArrayList<Integer> NumOcc) {

            var dataset = new DefaultCategoryDataset();
            for (int j = 0; j < namesMethod.size(); j++) {
            dataset.setValue(NumOcc.get(j), namesMethod.get(j).toString(),namesMethod.get(j).toString());
      
            }

            return dataset;
        }

        private JFreeChart createChart(CategoryDataset dataset) {

            JFreeChart barChart = ChartFactory.createBarChart(
                    "Olympic gold medals in London",
                    "",
                    "Gold medals",
                    dataset,
                    PlotOrientation.VERTICAL,
                    false, true, false);

            return barChart;
        }
    }
