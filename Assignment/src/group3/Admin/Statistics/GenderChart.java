package group3.Admin.Statistics;

import group3.PetShop;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GenderChart {
    private final ChartFrame frame;
    public GenderChart() {
        File file = new File("Customer.txt");
        Map<String, Integer> ageCounts = new HashMap<String, Integer>();
        int totalCustomers = 0;

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                if (data.length >= 2) {
                    String age = data[3].trim();
                    ageCounts.put(age, ageCounts.getOrDefault(age, 0) + 1);
                    totalCustomers++;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (String gender : ageCounts.keySet()) {
            double percentage = (double) ageCounts.get(gender) / totalCustomers * 100;
            dataset.setValue(gender + " (" + String.format("%.1f", percentage) + "%)", percentage);
        }

        JFreeChart chart = ChartFactory.createPieChart3D("Gender Distribution", dataset, true, true, false);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setForegroundAlpha(0.5f);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(Color.WHITE);
        plot.setLabelGenerator(null);

        frame = new ChartFrame("Gender Pie Chart", chart);
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.pack();
        frame.setLocation(0+frame.getWidth(), 0);
    }

    public void show() {
        frame.setVisible(true);
    }

}
