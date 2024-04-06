package attempt2;

import java.awt.*;
import java.util.ArrayList;

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

    public static void resetParticles () {
        //todo: have some way of shutting all down
        particles = new ArrayList<Particle>();
    }

    public synchronized static void addParticle (Particle p) {
        particles.add(p);
    }

    public synchronized static ArrayList<Particle> getParticles () {
        return (ArrayList<Particle>) particles.clone();
    }

    public Particle (int id, double pos_x, double pos_y, double vel_x, double vel_y, double mass, double charge) {
        this.id = id;
        x = pos_x;
        y = pos_y;
        v_x = vel_x;
        v_y = vel_y;
        m = mass;
        q = charge;
        addParticle(this);
    }

    public void draw (Graphics2D g) {
        g.drawOval((int)x, (int)y, 20, 20);
    }

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
