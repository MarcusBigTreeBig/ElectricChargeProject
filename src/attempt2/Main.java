package attempt2;

/**
 * Runs the simulation
 */

public class Main {
    public static void main(String[] args) {
        Particle.resetParticles();
        Particle p1 = new Particle(1, 500, 400, 0, 0, 1E+09, -1E-03, true);
        Particle p2 = new Particle(2, 800, 400, 0, 31.415926, 1E-03,3.2935E-05, true);
        Particle p3 = new Particle(3, 830, 400, 0, 188.50, 1E-06, -3.6E-06, false);
        p1.start();
        p2.start();
        p3.start();
        new SimulationFrame();
    }
}
