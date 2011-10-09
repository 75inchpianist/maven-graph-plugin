package net.sf.maven.plugin.graph.graphml;

import junit.framework.TestCase;
import net.sf.maven.plugin.graph.domain.ArtifactDependency;
import net.sf.maven.plugin.graph.domain.ArtifactRevisionIdentifier;
import net.sf.maven.plugin.graph.domain.MockArtifact;
import net.sf.maven.plugin.graph.graph.Graph;
import net.sf.maven.plugin.graph.graph.Vertex;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * User: janssk1
 * Date: 10/5/11
 * Time: 12:36 PM
 */
public class GraphMLGeneratorTest extends TestCase {

    private static final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private Validator schemaValidator;

    private final GraphMLGenerator graphMLGenerator = new GraphMLGenerator();

    @Override
    protected void setUp() throws Exception {
        Schema schema = factory.newSchema();

        // create a Validator instance, which can be used to validate an instance document
        schemaValidator = schema.newValidator();

    }

    public void testGraphMl() throws IOException, SAXException, ParserConfigurationException {
        Graph graph = new Graph(new ArtifactRevisionIdentifier("a", "a", "1.0"));
        Vertex root = graph.getRoot();
        root.setArtifact(new MockArtifact());
        root.addDependency(new ArtifactRevisionIdentifier("a", "b", "1"), new ArtifactDependency(new ArtifactRevisionIdentifier("a", "b", "2"), "default"));
        generateAndValidate(graph);
    }

    private void generateAndValidate(Graph graph) throws IOException, SAXException {
        StringWriter writer = new StringWriter();
        graphMLGenerator.serialize(graph, writer);
        schemaValidator.validate(new StreamSource(new StringReader(writer.toString())));
    }
}
