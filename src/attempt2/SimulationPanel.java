package attempt2;

import javax.swing.*;
import java.awt.*;

/**
 * Panel containing everything that needs to be displayed for the simulation
 */

public class SimulationPanel extends JPanel {

    /**
     * creates the panel for the simulation
     */
    public SimulationPanel () {
        setLayout(new BorderLayout());
        SimulationComponent simComp = new SimulationComponent();
        add(simComp, BorderLayout.CENTER);

        //create a thread to regularly tell the Component that displays the locations of the particles to repaint
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
