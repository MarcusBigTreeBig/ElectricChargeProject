package attempt2;

import java.awt.*;
import java.util.ArrayList;

/**
 * Particle for the Electric Field Simulation.
 * extends Thread, so each particle can determine it's own movements at the same time.
 * Each particle has a charge and a mass, and x and y coordinates, and x and y components of velocity.
 * Particles are assumed to be point particles.
 * The particles interact with each other particles with a Coulomb interaction, and no other forces.
 */

public class Particle extends Thread {

    //simulation information
    public final static double k = 8.99E+09; //Coulomb's constant
    private static long tickSpeed = 1; //measured in milliseconds
    private static double simTick = 0.001; //measured in seconds

    //shared information
    private static ArrayList<Particle> particles;

    //physical information
    private double x;
    private double y;
    private double v_x;
    private double v_y;
    private double m; //mass
    private double q; //charge

    private int id;
    private int displayRadius;
    private boolean displayNumbers;

    /**
     * sets the initial state of the simulation
     */
    public static void resetParticles () {
        //todo: have some way of shutting all down
        particles = new ArrayList<Particle>();
    }

    /**
     * Adds a particle to the list of particles in the simulation
     *
     * @param p
     */
    public synchronized static void addParticle (Particle p) {
        particles.add(p);
    }

    /**
     *
     * @return a clone of the list containing all the particles
     * particles themselves are not cloned
     */
    public synchronized static ArrayList<Particle> getParticles () {
        return (ArrayList<Particle>) particles.clone();
    }

    /**
     * Creates a particle, and adds itself to the list of all particles
     *
     * @param id
     * @param pos_x
     * @param pos_y
     * @param vel_x
     * @param vel_y
     * @param mass
     * @param charge
     */
    public Particle (int id, double pos_x, double pos_y, double vel_x, double vel_y, double mass, double charge, boolean displayNumbers) {
        this.id = id;
        x = pos_x;
        y = pos_y;
        v_x = vel_x;
        v_y = vel_y;
        m = mass;
        q = charge;
        displayRadius = 10;
        this.displayNumbers = displayNumbers;
        addParticle(this);
    }

    /**
     * particle draws itself
     *
     * @param g
     */
    public void draw (Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawOval((int)x-displayRadius, (int)y-displayRadius, displayRadius*2, displayRadius*2);
        g.setColor(q > 0 ? Color.RED : Color.YELLOW);
        g.fillOval((int)x-displayRadius, (int)y-displayRadius, displayRadius*2, displayRadius*2);
        int fontSize = 8;
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
        if (displayNumbers) {
            g.drawString(String.format("%6.2E", q) + " C", (int) x + 2*displayRadius, (int) y - displayRadius + fontSize/2);
            g.drawString(String.format("%6.2E", m) + " kg", (int) x + 2*displayRadius, (int) y - displayRadius - fontSize/2);
        }
        g.drawString(id+"", (int)x-fontSize/4, (int)y+fontSize/4);
    }

    /**
     * Determines the movement of this particle based on the locations of the other particles
     */
    @Override
    public void run () {
        double a_x;
        double a_y;
        double a_tot;
        double r;
        double angle;
        while (true) {

            a_x = 0;
            a_y = 0;

            //***beginning of critical section***

            ArrayList<Particle> particles = Particle.getParticles();

            //***start of physics***

            //calculating total forces by adding each individual force from each particle
            for (Particle p: particles) {
                if (p.id != this.id) {
                    angle = Math.atan((y-p.y)/(x-p.x));
                    r = Math.sqrt((x-p.x)*(x-p.x)+(y-p.y)*(y-p.y));//distance formula
                    a_tot = (k*q*p.q)/(m*r*r);//Coulomb equation
                    a_x += a_tot*Math.cos(Math.abs(angle))*Math.signum(x-p.x);
                    a_y += a_tot*Math.sin(Math.abs(angle))*Math.signum(y-p.y);
                }
            }

            //updating position and velocity information
            v_x += a_x*simTick;
            v_y += a_y*simTick;
            x += v_x*simTick;
            y += v_y*simTick;

            //***end of physics***

            //TODO: wait on countdown latch
            try {
                sleep(tickSpeed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //***end of critical section***

        }
    }

}
