import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Implements the file where all the location data of the charges is stored.
 * Has a FileWriter and a FileReader to manage the information.
 * Has a lock for the object, that has to be acquired to use the methods designed for Charges.
 * Has a read only method for GUI's that does not need a lock.
 */
public class ChargeFile {

    private String name;
    private FileWriter writer;
    private FileReader reader;
    private ChargeBST charges;

    /**
     * Creates a FileWriter and a FileReader inside the object
     *
     * @param fileName
     * @throws IOException
     */
    public ChargeFile (String fileName) throws IOException {
        name = fileName;
        writer = new FileWriter(name);
        reader = new FileReader(name);
        charges = null;
    }

    /**
     * writes down all of the information of the charges to the file
     */
    private synchronized void updateFile () {

    }

    /**
     *
     * @return a line for each charge
     */
    @Override
    public synchronized String toString () {
        String s = "";
        ArrayList<Charge> chargeList = getChargeInfo();
        for (Charge q: chargeList) {
            s += q.getID() + ", " + q.getCharge() + ", " + q.getX() + ", " + q.getY();
        }
        return s;
    }

    /**
     * updates the information of one of the charges
     *
     * @param charge
     */
    public synchronized void updateCharge (Charge charge) {
        if (charges == null) {
            charges = new ChargeBST(charge);
        }else{
            charges.add(new Charge(charge.getID(), charge.getCharge(), charge.getX(), charge.getY()));//create new charge to avoid issues with having to many pointers
        }
        System.out.println(this);
    }

    /**
     *
     * @return All charges in the simulation as an ArrayList
     */
    public synchronized ArrayList<Charge> getChargeInfo () {
        if (charges == null) {
            return null;
        }
        return charges.traverse();
    }

    /**
     * Contains all the information of the charges
     * The BST will have other BST's as it's left and right children
     * Information will be searched for very frequently from this object, so it's good to have an efficient search
     */
    private class ChargeBST {

        private Charge charge;
        private ChargeBST left;
        private ChargeBST right;

        /**
         * Creates a tree with a single node
         * @param charge
         */
        public ChargeBST (Charge charge) {
            this.charge = charge;
            this.left = null;
            this.right = null;
        }

        /**
         *
         * @return all of the charges in the tree as an ArrayList
         */
        public ArrayList<Charge> traverse () {
            ArrayList<Charge> list = new ArrayList<Charge>();
            list.add(charge);
            if (left != null) {
                list.addAll(left.traverse());
            }
            if (right != null) {
                list.addAll(right.traverse());
            }
            return list;
        }

        /**
         * Adds an element to the tree
         * Will replace if element is already there
         *
         * @param toAdd
         */
        public void add (Charge toAdd) {
            ChargeBST node = this;
            ChargeComparator comp = new ChargeComparator();
            while (node.charge.getID() != toAdd.getID() && node.left != null && node.right != null) {
                if (comp.compare(toAdd, node.charge) < 0) {
                    if (node.left == null) {
                        node.left = new ChargeBST(toAdd);
                    }else{
                        node = node.left;
                    }
                }else{
                    if (node.right == null) {
                        node.right = new ChargeBST(toAdd);
                    }else{
                        node = node.right;
                    }
                }
            }
            if (node.charge.getID() == toAdd.getID()) {
                node.charge = toAdd;
            }
        }

        /**
         * Compares 2 charges by ID
         */
        private class ChargeComparator implements Comparator<Charge> {
            @Override
            public int compare(Charge o1, Charge o2) {
                return o1.getID() - o2.getID();
            }
        }

    }
}

/*
Format of appearance of data in file:

<iD>, <q>, <x>, <y>

Example:

1, -1E-06, 0.012, -1.056

 */
