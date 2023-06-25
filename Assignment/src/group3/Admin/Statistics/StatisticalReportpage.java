package group3.Admin.Statistics;

import group3.Admin.AdminPage;
import group3.Admin.Statistics.AgeChart;
import group3.Admin.Statistics.AgeGraph;
import group3.Admin.Statistics.GenderChart;
import group3.Admin.Statistics.GenderGraph;
import group3.PetShop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatisticalReportpage {

    private final JFrame frame;


    public StatisticalReportpage() {
        frame = new JFrame("Statistical Report Page");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);


        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        JButton AgeReportButton = new JButton("Customer Age Statistics");
        JButton GenderReportButton = new JButton("Show Gender Statistics");
        JButton BackButton = new JButton("Go Back");

        buttonPanel.add(GenderReportButton);
        buttonPanel.add(AgeReportButton);
        buttonPanel.add(BackButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        AgeReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgeChart ageChart = new AgeChart();
                ageChart.show();
                AgeGraph ageGraph = new AgeGraph();
                ageGraph.show();
            }
        });

        GenderReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenderChart genderChart = new GenderChart();
                genderChart.show();
                GenderGraph genderGraph = new GenderGraph();
                genderGraph.show();

            }
        });

        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AdminPage adminPage = new AdminPage();
                adminPage.show();
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

}