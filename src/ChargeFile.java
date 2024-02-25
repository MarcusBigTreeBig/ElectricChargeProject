import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

    /**
     * Creates a FileWriter and a FileReader
     *
     * @param fileName
     * @throws IOException
     */
    public ChargeFile (String fileName) throws IOException {
        name = fileName;
        writer = new FileWriter(name);
        reader = new FileReader(name);
    }
}
