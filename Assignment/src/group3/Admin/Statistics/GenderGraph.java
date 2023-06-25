package group3.Admin.Statistics;

import group3.PetShop;
import group3.Store.FileManager;
import group3.Store.UserType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class GenderGraph {
    private final String[] AGE_RANGES = {"0-17", "18-25", "26-35", "36-45", "46-55", "56-65", "66+"};
    private final String[] GENDERS = {"Male", "Female"};

    private final FileManager FILE_MANAGER = new FileManager();
    private final ChartFrame frame;

    public GenderGraph() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String[][] customers = FILE_MANAGER.readFile(UserType.CUSTOMER);
        int maleCount = 0;
        int femaleCount = 0;

        for (String[] customer : customers) {
            String gender = customer[3];
            if (gender.equalsIgnoreCase("male")) {
                maleCount++;
            } else if (gender.equalsIgnoreCase("female")) {
                femaleCount++;
            }
        }

        dataset.addValue(maleCount, GENDERS[0], "");
        dataset.addValue(femaleCount, GENDERS[1], "");

        JFreeChart chart = ChartFactory.createBarChart("Customer Gender Distribution", "", "Number of Customers", dataset,
                PlotOrientation.VERTICAL, true, true, false);

        frame = new ChartFrame("Gender Distribution", chart);
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.pack();
        frame.setLocation(0,0);
    }

    public void show() {
        frame.setVisible(true);
    }

}
