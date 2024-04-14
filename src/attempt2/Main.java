package attempt2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Runs the simulation
 */

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("src/attempt2/input.txt"));
        Particle.setTickSpeed(input.nextLong());
        Particle.setSimTick(input.nextDouble());
        Particle.resetParticles();
        int i = 1;
        LinkedList<Particle> particles = new LinkedList<Particle>();
        while (input.hasNextLine()) {
            particles.add(new Particle(i, input.nextDouble(), input.nextDouble(), input.nextDouble(), input.nextDouble(), input.nextDouble(), input.nextDouble(), input.nextInt() == 1));
            i++;
        }
        Particle.configureBarrier();
        for (Particle p: particles) {
            p.start();
        }
        new SimulationFrame();
    }
}
