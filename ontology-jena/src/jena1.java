import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.InputStream;

public class jena1 {
    String SOURCE = "http://www.semanticweb.org/anass/ontologies/movies";
    String NS = SOURCE + "#";
    public void displayPersons(){
        OntModel model = ModelFactory.createOntologyModel();
        InputStream myfile = FileManager.get().open("data/movies.owl");
        if(myfile==null){
            throw new IllegalArgumentException("file not found!");
        }
        model.read(myfile,null);
        String personURL = NS+"person";
        OntClass personClass = model.getOntClass(personURL);
        ExtendedIterator personIterator = personClass.listInstances();
        System.out.println("Instances of class person:\n");
        while(personIterator.hasNext()){
            System.out.println("   "+personIterator.next().toString());
        }
    }

}
