package capers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import static capers.Utils.*;

/** Represents a dog that can be serialized.
 * @author Leonard
*/
public class Dog implements Serializable{ // TODO

    /** Folder that dogs live in. */
    static final File DOG_FOLDER = Utils.join(CapersRepository.CAPERS_FOLDER, "dogs"); // TODO (hint: look at the `join`
                                         //      function in Utils)

    /** Age of dog. */
    private int age;
    /** Breed of dog. */
    private String breed;
    /** Name of dog. */
    private String name;

    /**
     * Creates a dog object with the specified parameters.
     * @param name Name of dog
     * @param breed Breed of dog
     * @param age Age of dog
     */
    public Dog(String name, String breed, int age) {
        this.age = age;
        this.breed = breed;
        this.name = name;
    }

    /**
     * Reads in and deserializes a dog from a file with name NAME in DOG_FOLDER.
     *
     * @param name Name of dog to load
     * @return Dog read from file
     */
    public static Dog fromFile(String name) {
         File[] Files = DOG_FOLDER.listFiles();
         File nameDog = null;
         for (File x : Files) {
             if (x.getName().equals(name)) {
                 nameDog = x;
             }
         }
         return readObject(nameDog, Dog.class);
    }

    /**
     * Increases a dog's age and celebrates!
     */
    public void haveBirthday() {
        age += 1;
        System.out.println(toString());
        System.out.print("Happy birthday! Woof! Woof!");
    }

    /**
     * Saves a dog to a file for future use.
     */
    public void saveDog() {
        // TODO (hint: don't forget dog names are unique)
        File[] Files = DOG_FOLDER.listFiles();
        File nameDog = null;
        boolean flag = true;
        if (Files != null) {
            for (File x : Files) {
                if (x.getName().equals(this.name)) {
                    nameDog = x;
                    flag = false;
                }
            }
        }
        if (flag){
            File newDog = Utils.join(DOG_FOLDER,this.name);
            if (!newDog.exists()) {
                try {
                    newDog.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            writeObject(newDog, this);
        } else {
            writeObject(nameDog, this);
        }

    }

    @Override
    public String toString() {
        return String.format(
            "Woof! My name is %s and I am a %s! I am %d years old! Woof!",
            name, breed, age);
    }

}
