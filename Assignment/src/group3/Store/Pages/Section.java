package group3.Store.Pages;

import javax.swing.*;
import java.awt.*;

public class Section {
    protected final JPanel mainContainer;

    public Section(LayoutManager layoutManager) {
        mainContainer = new JPanel(layoutManager);
    }

    public JPanel getMainContainer() {
        return mainContainer;
    }
}
