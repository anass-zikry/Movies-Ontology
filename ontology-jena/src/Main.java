import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
//        System.out.println("Question 1:");
//        jena1 myJena1 = new jena1();
//        myJena1.displayPersons();
//        System.out.println("Question 2:");
//        jena2 myJena2 = new jena2();
//        myJena2.displayPersonsUsingQuery();
//        System.out.println("Question 3:");
//        jena3 myJena3 = new jena3();
//        myJena3.displayActorsUsingInference();
//        System.out.println("Question 4:");
//        jena4 myJena4 = new jena4();
//        myJena4.displayMovieDetails("Ibrahim_labyad");
        System.out.println("Question 5:");
        jena5 myJena5= new jena5();
        myJena5.displayAllActorsAndDirectors();
//        System.out.println("Question 6:");
//        jena6 myJena6 = new jena6();
//        myJena6.rules();
//        test t1 = new test();
//        t1.test();
    }
}