import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class test {
    String SOURCE = "http://www.semanticweb.org/anass/ontologies/movies";
    String NS = SOURCE + "#";
    String PREFIX_rdf="PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    String PREFIX_ont="PREFIX ont:<http://www.semanticweb.org/anass/ontologies/movies#>";
    String PREFIX_rdfs="PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>";
    String PREFIX_xsd = "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>";
    public void test(){
        OntModel model = ModelFactory.createOntologyModel();
        InputStream myfile = FileManager.get().open("data/movies.owl");
        if(myfile==null){
            throw new IllegalArgumentException("file not found!");
        }
        model.read(myfile,null);
        String queryString     = PREFIX_rdfs+PREFIX_rdf+PREFIX_ont+PREFIX_xsd+"CONSTRUCT{\n" +
                "\t?movie ont:title ?title .\n" +
                "\t?movie ont:hasGenre ont:Thriller .\n" +
                "\t}\n" +
                "\tWHERE{\n" +
                "\t?movie rdf:type ont:movie .\n" +
                "\t?movie ont:title ?title .\n" +
                "\t?movie ont:hasGenre ont:Thriller .\n" +
                "\t}";
        Query query = QueryFactory.create(queryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
        System.out.println("QUERY :");
        System.out.println(qexec.getQuery());
        System.out.println("Results :");
        try {

//            ResultSet results = qexec.execSelect() ;
//            boolean result = qexec.execAsk();
//            Model dirM = qexec.execDescribe();
//            System.out.println(dirM);
            Iterator myiter = qexec.execConstructTriples();
            while(myiter.hasNext()){
                System.out.println(myiter.next());
            }
//            while (results.hasNext())
//            {
//
//                QuerySolution soln = results.nextSolution() ;
////                RDFNode x = soln.get("s") ;       // Get a result variable by name.
//                Resource r = soln.getResource("person") ; // Get a result variable - must be a resource
////                Literal l = soln.getLiteral("movieTitle") ;
////                Literal l2 = soln.getLiteral("nationality") ;
////                Resource r2 = soln.getResource("nationality") ;
////                System.out.println(x.toString());
////                System.out.println("Director:");
//                System.out.println(r);
////                System.out.println("MovieTitle:");
////                System.out.println(l);
////                System.out.println("Nationality:");
////                System.out.println(l2);
//            }
        } finally { qexec.close() ; }

    }
}
