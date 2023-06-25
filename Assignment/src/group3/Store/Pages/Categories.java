package group3.Store.Pages;

import com.sun.xml.internal.ws.util.StringUtils;
import group3.PetShop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

public class Categories extends Section {
    private final List<String> checkedList;

    public Categories() {
        super(new GridBagLayout());
        mainContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        JLabel name = new JLabel("<html><h2>Filter By</h2></html>");
        mainContainer.add(name, c);

        checkedList = new ArrayList<>();
        for (String animalType : PetShop.getProducts().getAnimalTypes()) {
            JCheckBox checkBox = new JCheckBox(StringUtils.capitalize(animalType.replaceAll("_", " ").toLowerCase()));
            checkBox.setFont(new Font(checkBox.getFont().getName(), checkBox.getFont().getStyle(), 14));
            checkBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkedList.add(animalType);
                }
                else checkedList.remove(animalType);

                PetShop.getWindow().updatePage(PetShop.getWindow().getHomePage().getMainContainer());
            });
            c.gridy++;
            mainContainer.add(checkBox, c);
        }
    }

    public List<String> getCheckedList() {
        return checkedList;
    }
}
