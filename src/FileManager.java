import java.io.*;

public class FileManager {

    public static void saveData(String fileName, Object object) {

        try {

            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(fileName));

            out.writeObject(object);

            out.close();

        } catch (IOException e) {

            e.printStackTrace();
            
        }

    }

    public static Object loadData(String fileName) {

        try {

            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(fileName));

            Object object = in.readObject();

            in.close();

            return object;

        } catch (IOException | ClassNotFoundException e) {

            return null;

        }

    }

}


