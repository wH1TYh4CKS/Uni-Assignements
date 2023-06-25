package group3.Admin.Statistics;

import group3.PetShop;
import group3.Store.FileManager;
import group3.Store.UserType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class AgeGraph {
    private final String[] AGE_RANGES = {"0-17", "18-25", "26-35", "36-45", "46-55", "56-65", "66+"};
    private final String[] GENDERS = {"Male", "Female"};

    private final FileManager FILE_MANAGER = new FileManager();
    private final ChartFrame frame;

    public AgeGraph() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String[][] customers = FILE_MANAGER.readFile(UserType.CUSTOMER);
        int[] maleAges = new int[AGE_RANGES.length];
        int[] femaleAges = new int[AGE_RANGES.length];

        for (String[] customer : customers) {
            int age = Integer.parseInt(customer[2]);
            String ageRange = String.valueOf(getAgeRange(age));
            String gender = customer[3];

            if (gender.equalsIgnoreCase("male")) {
                maleAges[getAgeRange(age)]++;
            } else if (gender.equalsIgnoreCase("female")) {
                femaleAges[getAgeRange(age)]++;
            }
        }

        for (int i = 0; i < AGE_RANGES.length; i++) {
            dataset.addValue(maleAges[i], GENDERS[0], AGE_RANGES[i]);
            dataset.addValue(femaleAges[i], GENDERS[1], AGE_RANGES[i]);
        }

        JFreeChart chart = ChartFactory.createBarChart("Customer Age Distribution", "Age Range", "Number of Customers",
                dataset, PlotOrientation.VERTICAL, true, true, false);

        frame = new ChartFrame("Age Distribution", chart);
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.pack();
        frame.setLocation(frame.getWidth(),0);
    }

    public void show() {
        frame.setVisible(true);
    }

    private int getAgeRange(int age) {
        if (age >= 0 && age <= 17) {
            return 0;
        } else if (age >= 18 && age <= 25) {
            return 1;
        } else if (age >= 26 && age <= 35) {
            return 2;
        } else if (age >= 36 && age <= 45) {
            return 3;
        } else if (age >= 46 && age <= 55) {
            return 4;
        } else if (age >= 56 && age <= 65) {
            return 5;
        } else {
            return 6;
        }
    }

}
