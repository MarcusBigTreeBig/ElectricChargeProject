package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Runs the simulation
 */

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("src/input_files/demo.txt"));
        Particle.setUsesGUI(input.nextInt() == 1);
        Particle.setRunTime(input.nextInt());
        Particle.setTickSpeed(input.nextLong());
        Particle.setSimTick(input.nextDouble());
        Particle.resetParticles();
        int i = 1;
        LinkedList<Particle> particles = new LinkedList<Particle>();
        while (input.hasNextLine()) {
            particles.add(new Particle(i, input.nextDouble(), input.nextDouble(), input.nextDouble(), input.nextDouble(), input.nextDouble(), input.nextDouble(), input.nextInt() == 1));
            i++;
        }
        Particle.startSim();
    }
}
