package code;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Particle for the Electric Field Simulation.
 * extends Thread, so each particle can determine it's own movements at the same time.
 * Each particle has a charge and a mass, and x and y coordinates, and x and y components of velocity.
 * Particles are assumed to be point particles.
 * The particles interact with each other particles with a Coulomb interaction, and no other forces.
 */

public class Particle extends Thread {

    //simulation information
    public final static double k = 8.99E+09; //Coulomb's conant
    private static long tickSpeed; //measured in milliseconds
    private static double simTick; //measured in seconds
    private static long runTime; //measured in seconds
    private static boolean usesGUI;
    private static SimulationFrame frame;

    //shared information
    private static ArrayList<Particle> particles;
    private static CyclicBarrier barrier;

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
    private boolean running;

    /**
     * sets the initial state of the simulation
     */
    public static void resetParticles () {
        particles = new ArrayList<Particle>();
    }

    /**
     *
     * @param ts the tick speed of the program
     */
    public synchronized static void setTickSpeed (long ts) {
        tickSpeed = ts;
    }

    /**
     *
     * @param st the tick speed of the simulation
     */
    public synchronized static void setSimTick (double st) {
        simTick = st;
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
     * sets if the simulations should display a gui
     *
     * @param uses
     */
    public static synchronized void setUsesGUI (boolean uses) {
        usesGUI = uses;
    }

    /**
     * sets how long the simulation will run for
     *
     * @param time
     */
    public static synchronized void setRunTime (int time) {
        runTime = time;
    }

    /**
     * starts the simulation based on the particles created in the class
     *
     * creates a barrier
     * starts all particle threads
     * starts a thread watching for completion of the simulation
     */
    public synchronized static void startSim () {
        barrier = new CyclicBarrier(particles.size());
        Thread checkIfComplete = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean running = true;
                long startTime = System.currentTimeMillis();
                while (running) {
                    running = System.currentTimeMillis() < startTime+runTime*1000;
                }
                try {
                    stopSim();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        checkIfComplete.start();
        for (Particle p: particles) {
            p.start();
        }
        if (usesGUI) {
            frame = new SimulationFrame();
        }
    }

    /**
     * stops all threads, and outputs the gained information to a file
     */
    private static synchronized void stopSim () throws IOException {
        for (Particle p: particles) {
            p.running = false;
        }
        FileWriter file = new FileWriter(new File("src/output_files/output.txt"));
        for (Particle p: particles) {
            file.write(p.x + ", " + p.y + ", " + p.v_x + ", " + p.v_y + ", " + p.m + ", " + p.q + "\n");
            file.flush();
        }
        System.exit(0);
    }

    /**
     *
     * @return a clone of the list containing all the particles
     * particles themselves are cloned
     */
    public synchronized static ArrayList<Particle> getParticles () {
        ArrayList<Particle> cloneList = new ArrayList<Particle>();
        for (Particle p: particles) {
            cloneList.add(new Particle(p.id, p.x, p.y, p.m, p.q, p.displayNumbers));
        }
        return cloneList;
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
        running = true;
        addParticle(this);
    }

    /**
     * Creates a particle that does not get added to the static list of particles
     *
     * @param pos_x
     * @param pos_y
     * @param mass
     * @param charge
     */
    private Particle (int id, double pos_x, double pos_y, double mass, double charge, boolean displayNumbers) {
        this.id = id;
        x = pos_x;
        y = pos_y;
        m = mass;
        q = charge;
        displayRadius = 10;
        this.displayNumbers = displayNumbers;
        running = false;
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
        long beginTime;
        long endTime;
        while (running) {

            a_x = 0;
            a_y = 0;

            beginTime = System.currentTimeMillis();

            //***beginning of critical section***

            ArrayList<Particle> particles = Particle.getParticles();
            try {
                barrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }

            //***start of physics***

            //calculating total forces by adding each individual force from each particle
            for (Particle p: particles) {
                double tolerance = 0.3*Math.sqrt(v_x*v_x+v_y*v_y)*simTick;
                if (p.id != this.id && (!Utilities.doubleEquals(x, p.x, tolerance) || !Utilities.doubleEquals(y, p.y, tolerance))) {//don't do calculations if too close together
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
            //***end of critical section***

            //***wait for tick and other particles to have turn
            endTime = System.currentTimeMillis();
            if (endTime < beginTime+tickSpeed) {
                try {
                    sleep(beginTime+tickSpeed-endTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
