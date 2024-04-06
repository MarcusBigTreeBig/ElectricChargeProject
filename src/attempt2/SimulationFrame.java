package attempt2;

import javax.swing.*;

/**
 * Frame that the simulation is displayed on
 */

public class SimulationFrame extends JFrame {
    /**
     * creates a frame
     */
    public SimulationFrame () {
        setSize(400, 400);
        getContentPane().add(new SimulationPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
