package attempt2;

import javax.swing.*;
import java.awt.*;

public class SimulationPanel extends JPanel {
    public SimulationPanel () {
        setLayout(new BorderLayout());
        SimulationComponent simComp = new SimulationComponent();
        add(simComp, BorderLayout.CENTER);
        Thread updateComponent = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    simComp.repaint();
                }
            }
        });
        updateComponent.start();
    }
}
