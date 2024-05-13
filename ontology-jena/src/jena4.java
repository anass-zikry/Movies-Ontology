import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.InputStream;

public class jena4 {
    String SOURCE = "http://www.semanticweb.org/anass/ontologies/movies";
    String NS = SOURCE + "#";


    public void displayMovieDetails(String movieName) {
        OntModel model = ModelFactory.createOntologyModel();
        InputStream myfile = FileManager.get().open("data/movies.owl");
        if (myfile == null) {
            throw new IllegalArgumentException("file not found!");
        }
        model.read(myfile, null);
        String movieURL = NS + movieName;

        OntResource movieResource = model.getOntResource(movieURL);
        if (movieResource == null) {
            System.out.println("Error: Cannot find movie");
        } else {
            String titleURL = NS + "title";
            String yearURL = NS + "year";
            String countryURL = NS + "country";
            String hasGenreURL = NS + "hasGenre";
            String hasActorURL = NS + "hasActor";
            String genreURL=NS+"genre";
            String nameURL=NS+"name";
            OntProperty titleProperty = model.getOntProperty(titleURL);
            OntProperty yearProperty = model.getOntProperty(yearURL);
            OntProperty countryProperty = model.getOntProperty(countryURL);
            OntProperty hasGenreProperty = model.getOntProperty(hasGenreURL);
            OntProperty hasActorProperty = model.getOntProperty(hasActorURL);
            System.out.println("Movie Title:" + movieResource.getPropertyValue(titleProperty));
            System.out.println("Year:" + movieResource.getPropertyValue(yearProperty));
            System.out.println("Country:" + movieResource.getPropertyValue(countryProperty));
            StmtIterator hasGenreIterator = movieResource.listProperties(hasGenreProperty);
            StmtIterator hasActorIterator = movieResource.listProperties(hasActorProperty);
            System.out.println("Genres:");
            while (hasGenreIterator.hasNext()) {
                Statement stm = hasGenreIterator.next();
                Resource genreResource = stm.getResource();
                OntProperty genreProperty=model.getOntProperty(genreURL);
                Statement genreStmt = genreResource.getProperty(genreProperty);
                String genre=genreStmt.getObject().toString();
                System.out.println(genre);
            }
            System.out.println("Actors:");
            while (hasActorIterator.hasNext()) {
                Statement stm = hasActorIterator.next();
                Resource actorResource = stm.getResource();
                OntProperty nameProperty = model.getOntProperty(nameURL);
                Statement actorStmt = actorResource.getProperty(nameProperty);
                String name =actorStmt.getObject().toString();
                System.out.println(name);
            }
        }
    }
}
