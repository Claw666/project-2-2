package Group1.GUI;

import Group1.FileReader.Scenario;
import Group1.States.GuardState;

import javax.swing.*;
import java.util.List;

 public class ViewController extends JFrame {

    public ViewController(Scenario scenario, List<GuardState> guardStates) {
        Map map = new Map(scenario, guardStates);

        // graphical stuff
        add(map);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Group 1 - Catch or be catched! ");
        setSize(scenario.getWidth() + 20, scenario.getHeight() + 45); // height + 20 due to menu bar on top
        setLocationByPlatform(true);
        setVisible(true);
    }

    public void updateState() {
        revalidate();
        repaint();
    }
}


