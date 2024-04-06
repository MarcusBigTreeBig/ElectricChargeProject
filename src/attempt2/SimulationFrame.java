package attempt2;

import javax.swing.*;

public class SimulationFrame extends JFrame {
    public SimulationFrame () {
        setSize(400, 400);
        getContentPane().add(new SimulationPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
