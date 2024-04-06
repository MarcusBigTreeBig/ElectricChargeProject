package attempt2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationComponent extends JComponent {
    public SimulationComponent () {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        ArrayList<Particle> particles = Particle.getParticles();
        for (Particle p: particles) {
            p.draw(g2d);
        }
    }
}
