import java.util.ArrayList;

public class Charge extends Thread{

    public final static double k = 8.99E+09; //Coulomb's constant
    private static long tickSpeed = 100; //milliseconds
    private static double simTick = 0.001; //seconds

    private double v_x;
    private double v_y;
    private double x;
    private double y;
    private double q; //charge
    private double m; //mass

    public Charge (double pos_x, double pos_y, double vel_x, double vel_y, double charge, double mass) {
        x = pos_x;
        y = pos_y;
        v_x = vel_x;
        v_y = vel_y;
        q = charge;
        m = mass;
    }

    private Charge (double pos_x, double pos_y, double charge) {
        x = pos_x;
        y = pos_y;
        q = charge;
    }

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

            //implement lock for this later
            ArrayList<Charge> charges = getChargeInfo();

            //this is where the physics happens
            for (Charge c2: charges) {
                angle = Math.atan((y-c2.y)/(x-c2.x));
                r = Math.sqrt((x-c2.x)*(x-c2.x)+(y-c2.y)*(y-c2.y));
                a_tot = (k*q*c2.q)/(m*r*r);
                a_x += a_tot*Math.cos(Math.abs(angle))*Math.signum(x-c2.x);
                a_y += a_tot*Math.sin(Math.abs(angle))*Math.signum(y-c2.y);
            }
            v_x += a_x*simTick;
            v_y += a_y*simTick;
            x += v_x*simTick;
            y += v_y*simTick;
            //end of physics

            //incomplete
            System.out.println("x: " + x + " y: " + y);

            try {
                sleep(tickSpeed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized ArrayList<Charge> getChargeInfo () {
        //written for testing a single charge right now
        ArrayList<Charge> charges = new ArrayList<Charge>();
        charges.add(new Charge(0, 0, 1E-06));
        return charges;
    }
}
