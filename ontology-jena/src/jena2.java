import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;

import java.io.InputStream;

public class jena2 {

//    String SOURCE = "http://www.semanticweb.org/anass/ontologies/movies";
//    String NS = SOURCE + "#";
//    String PREFIX_rdf="PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
//    String PREFIX_ont="PREFIX ont:<http://www.semanticweb.org/anass/ontologies/movies#>";
//    String PREFIX_rdfs="PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>";
    public void displayPersonsUsingQuery(){
        OntModel model = ModelFactory.createOntologyModel();
        InputStream myfile = FileManager.get().open("data/movies.owl");
        if(myfile==null){
            throw new IllegalArgumentException("file not found!");
        }
        model.read(myfile,null);
//        String queryString     = PREFIX_rdfs+PREFIX_rdf+PREFIX_ont+"SELECT ?s "
//                + "WHERE { "
//                + "?x "+"ont:name "+"?s"+" . }";
//        String queryString     = PREFIX_rdfs+PREFIX_rdf+PREFIX_ont+"SELECT ?s "
//                + "WHERE { "
//                + "?s "+"rdf:type "+"?y"+" . "
//                +"?y "+"rdfs:subClassOf "+"ont:person . }";
        String queryString = readQueryFromFile("data/jena2.txt");
        Query query = QueryFactory.create(queryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
        System.out.println("QUERY :");
        System.out.println(qexec.getQuery());
        System.out.println("Results :");
        try {
            ResultSet results = qexec.execSelect() ;
            while (results.hasNext())
            {
                QuerySolution soln = results.nextSolution() ;
//                RDFNode x = soln.get("s") ;       // Get a result variable by name.
                Resource r = soln.getResource("s") ; // Get a result variable - must be a resource
//                Literal l = soln.getLiteral("s") ;
                System.out.println(r);
            }
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
