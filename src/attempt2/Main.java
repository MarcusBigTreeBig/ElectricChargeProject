package attempt2;

public class Main {
    public static void main(String[] args) {
        Particle.resetParticles();
        Particle p1 = new Particle(1, 150, 100, 0, 62.83, 1E-03, 0.021957);
        Particle p2 = new Particle(2, 100, 100, 0, 0, 1E-03, -1E-06);
        p1.start();
        p2.start();
        new SimulationFrame();
    }
}
