package off;

/*
* 
Jyrki Oraskari, Aalto University, 2016 

This research has partly been carried out at Aalto University in DRUMBEAT 
“Web-Enabled Construction Lifecycle” (2014-2017) —funded by Tekes, 
Aalto University, and the participating companies.

The MIT License (MIT)
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/


/*
import org.apache.commons.rdf.api.Graph ;
import org.apache.commons.rdf.api.RDFTermFactory;
import org.apache.commons.rdf.jsonldjava.JsonLdGraph;
import org.apache.commons.rdf.jsonldjava.JsonLdRDFTermFactory;
import org.apache.jena.commonsrdf.JenaCommonsRDF ;
import org.apache.jena.commonsrdf.RDFTermFactoryJena;
import org.apache.jena.riot.Lang ;
import org.apache.jena.riot.RDFDataMgr ;
import org.apache.jena.sparql.graph.GraphFactory ;

import com.github.jsonldjava.core.RDFDataset;
import com.github.jsonldjava.utils.JsonUtils;

import fi.aalto.drumbeat.DrumbeatUserManager.events.EventCommunication;
*/

public class GraphDatabase {
	/*
	private EventCommunication communication = EventCommunication.getInstance();
	final private int otto_sender_id=2;
	static private Optional<GraphDatabase> singleton=Optional.empty();    
    static public GraphDatabase getInstance()
    {
    	if(!singleton.isPresent())
    		singleton=Optional.of(new GraphDatabase());
    	return singleton.get();
    }
    
    public GraphDatabase()
    {
    	communication.register(this);
    }

	private Optional<org.apache.jena.graph.Graph> optional_jGraph = Optional.empty(); 
	private Optional<Graph> optional_graph = Optional.empty(); 
	
	
	public Graph getGraph()
	{
		if(!optional_graph.isPresent())
		{
			org.apache.jena.graph.Graph jGraph= getJena_Graph();
			optional_graph = Optional.of(JenaCommonsRDF.fromJena(jGraph)) ;
		}
		return optional_graph.get();
	}

	public org.apache.jena.graph.Graph getJena_Graph()
	{
		if(!optional_jGraph.isPresent())
			optional_jGraph = Optional.of(GraphFactory.createGraphMem()) ;
		return optional_jGraph.get();
	}


	@SuppressWarnings("deprecation")
	@Override
	public String toString(){
		 Graph graph = getGraph();
		
		JsonLdRDFTermFactory json_f=new JsonLdRDFTermFactory();
        RDFDataset rdf_d=new RDFDataset();
        
        JsonLdGraph json_graph=json_f.asGraph(rdf_d);
        
        graph.getTriples().forEach(json_graph::add);
        
        try {
			return JsonUtils.toPrettyString(json_graph);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return "";
	}
	
	public String data2String()
	{
		org.apache.jena.graph.Graph jGraph = getJena_Graph();
		StringWriter writer = new StringWriter();
        // And its in the Jena graph
        RDFDataMgr.write(writer, jGraph, Lang.TTL) ;
        return writer.toString();
	}
	

	Optional<String> filename=Optional.empty();
	public void init(String filename) {
		if(this.filename.isPresent())
		{
			System.err.println("Initialized already!");
			return;
		}
		
		
		this.filename = Optional.of(filename);
		org.apache.jena.graph.Graph jGraph = getJena_Graph();
		try {
			if(new File(filename).exists())
			  RDFDataMgr.read(jGraph, filename) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void write_triples()
	{
		org.apache.jena.graph.Graph jGraph = getJena_Graph(); 
        // And its in the Jena graph
        RDFDataMgr.write(System.out, jGraph, Lang.TTL) ;
	}
	
	
	public void save() {
		if(!filename.isPresent())
		{
			System.err.println("No filename");
			return;
		}	
		  communication.tell_newRESTData(otto_sender_id);
		org.apache.jena.graph.Graph jGraph = getJena_Graph();
		try {
			FileOutputStream fo=new FileOutputStream(filename.get());
			RDFDataMgr.write(fo, jGraph, Lang.TTL) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		GraphDatabase g=GraphDatabase.getInstance();
		
		g.init("c:/jo/users/users.ttl");
		Graph graph=g.getGraph();
		 RDFTermFactory rft = new RDFTermFactoryJena() ;
	        graph.add(rft.createIRI("http://drumbeat/company/"+"123"),
	                  rft.createIRI("http://drumbeat/name"),
	                  rft.createLiteral("name")) ;
		System.out.println(g);
		g.save();
		//g.write_triples();
	}
*/
}
