import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerFactory;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDF;

import java.io.InputStream;

public class jena3 {
    String SOURCE = "http://www.semanticweb.org/anass/ontologies/movies";
    String NS = SOURCE + "#";
    public void displayActorsUsingInference(){
        OntModel model = ModelFactory.createOntologyModel();
        InputStream myfile = FileManager.get().open("data/movies.owl");
        if(myfile==null){
            throw new IllegalArgumentException("file not found!");
        }
        model.read(myfile,null);
        String actorURL = NS+"actor";
        Reasoner myReasoner = ReasonerRegistry.getOWLReasoner();
        myReasoner=myReasoner.bindSchema(model);
        InfModel inf = ModelFactory.createInfModel(myReasoner, model);

        // Get all statements where the subject is an actor
        OntClass actor = model.getOntClass(actorURL);
        ExtendedIterator actorIt = actor.listInstances();
        System.out.println("Actors:\n");
        while(actorIt.hasNext()){
            System.out.println("   "+actorIt.next().toString());
        }
    }
}
