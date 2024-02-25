import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A charge for the simulation of an electric field.
 * Does not interact with the magnetic field.
 * Assumes a point charge.
 *
 * Each charge is a thread, so each charge can move simultaneously.
 * The location of all charges is stored in a static file all can access.
 * A lock will need to be acquired for a charge to access the location data.
 * The file is protected by a lock.
 *
 * A charge cannot move a small increment twice before each charge has moved.
 */

public class Charge extends Thread{

    public final static double k = 8.99E+09; //Coulomb's constant
    private static long tickSpeed = 1; //measured in milliseconds
    private static double simTick = 0.001; //measured in seconds

    private static ChargeFile locationFile = null;

    private static int nextID = 0;
    private int iD; //to keep organized in file

    private double v_x;
    private double v_y;
    private double x;
    private double y;
    private double q; //charge
    private double m; //mass

    /**
     * Creates a charge that will move in the field
     *
     * @param pos_x
     * @param pos_y
     * @param vel_x
     * @param vel_y
     * @param charge
     * @param mass
     */
    public Charge (double pos_x, double pos_y, double vel_x, double vel_y, double charge, double mass) throws IOException {
        x = pos_x;
        y = pos_y;
        v_x = vel_x;
        v_y = vel_y;
        q = charge;
        m = mass;
        iD = ++nextID;

        if (locationFile == null) {
            locationFile = new ChargeFile("locationFile.txt");
        }

        run();
    }

    /**
     * Used to keep data together when reading a charge from a file
     *
     * @param pos_x
     * @param pos_y
     * @param charge
     */
    private Charge (double pos_x, double pos_y, double charge) {
        x = pos_x;
        y = pos_y;
        q = charge;
    }

    /**
     * Infinitely updates location and velocity information, then waits for all other charges
     */
    @Override
    public void run() {
        double a_x;
        double a_y;
        double a_tot;
        double r;
        double angle;
        while (true) {
            a_x = 0;
            a_y = 0;

            //*** beginning of critical section ***

            //implement lock for this later
            ArrayList<Charge> charges = getChargeInfo();

            //*** start of physics ***

            //calculating the total force by adding each individual force from each charge
            for (Charge c2: charges) {
                angle = Math.atan((y-c2.y)/(x-c2.x));
                r = Math.sqrt((x-c2.x)*(x-c2.x)+(y-c2.y)*(y-c2.y));//distance formula
                a_tot = (k*q*c2.q)/(m*r*r);//Coulomb equation
                a_x += a_tot*Math.cos(Math.abs(angle))*Math.signum(x-c2.x);
                a_y += a_tot*Math.sin(Math.abs(angle))*Math.signum(y-c2.y);
            }

            //updating position and velocity information
            v_x += a_x*simTick;
            v_y += a_y*simTick;
            x += v_x*simTick;
            y += v_y*simTick;

            //*** end of physics ***

            //incomplete, will need to display more information and write to a file
            System.out.println("x: " + x + " y: " + y);

            //*** end of critical section ***

            //ensure all charges run before continuing, and tick time is met before continuing
            try {
                sleep(tickSpeed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     *
     * @return All charges in the simulation as a ArrayList
     */
    public synchronized ArrayList<Charge> getChargeInfo () {
        //written for testing a single charge right now
        ArrayList<Charge> charges = new ArrayList<Charge>();
        charges.add(new Charge(0, 0, 1E-06));
        return charges;
    }
}
