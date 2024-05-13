import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;
import org.apache.jena.query.*;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;

public class jena5 {
    String SOURCE = "http://www.semanticweb.org/anass/ontologies/movies";
    String NS = SOURCE + "#";


    public void displayAllActorsAndDirectors(){
        OntModel model = ModelFactory.createOntologyModel();
        InputStream myfile = FileManager.get().open("data/movies.owl");
        if (myfile == null) {
            throw new IllegalArgumentException("file not found!");
        }
        model.read(myfile, null);
        String queryString =readQueryFromFile("data/jena5.txt");
        Query query = QueryFactory.create(queryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
        try {

            Iterator myiter = qexec.execConstructTriples();
            while(myiter.hasNext()){
                System.out.println(myiter.next());
            }
//            ResIterator stmtIterator = resModel.listResourcesWithProperty(resModel.getProperty(NS+"ActorDirector"));
//            while (stmtIterator.hasNext()) {
//                Resource r = stmtIterator.next();
//                System.out.println(r);
//                Resource subject = stmt.getSubject();
//                Resource predicate = stmt.getPredicate();
//                Resource object = stmt.getObject().asResource();

            // Print the statement
//                System.out.println("Subject: " + subject.toString()+"  Predicate: " + predicate.toString()+ "  Object: " + object.toString());
//        }

        } finally { qexec.close() ; }
    }
    private static String readQueryFromFile(String queryFile) {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = FileManager.get().open(queryFile)) {
            if (inputStream != null) {
                String line;
                java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(inputStream, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
