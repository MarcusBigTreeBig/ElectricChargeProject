package attempt2;

/**
 * Runs the simulation
 */

public class Main {
    public static void main(String[] args) {
        Particle.resetParticles();
        Particle p1 = new Particle(1, 100, 100, 0, 0, 1E-03, -3E-04);
        Particle p2 = new Particle(2, 150, 150, 0, 0, 1E-03, -3E-06);
        Particle p3 = new Particle(3, 150, 50, 0, 0, 1E-03, -3E-06);
        Particle p4 = new Particle(4, 50, 150, 0, 0, 1E-03, -3E-06);
        Particle p5 = new Particle(5, 50, 50, 0, 0, 1E-03, -3E-06);
        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
        new SimulationFrame();
    }
}
