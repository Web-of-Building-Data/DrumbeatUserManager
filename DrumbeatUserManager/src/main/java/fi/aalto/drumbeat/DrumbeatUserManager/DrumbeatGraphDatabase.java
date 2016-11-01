package fi.aalto.drumbeat.DrumbeatUserManager;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Optional;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.impl.jena.ModelFactoryImpl;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.Resource;

import com.google.common.eventbus.Subscribe;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.viceversatech.rdfbeans.RDFBeanManager;
import com.viceversatech.rdfbeans.exceptions.RDFBeanException;

import fi.aalto.drumbeat.DrumbeatUserManager.entities.Company;
import fi.aalto.drumbeat.DrumbeatUserManager.events.CompanyDataChange_Event;
import fi.aalto.drumbeat.DrumbeatUserManager.events.CompanyRead_Event;
import fi.aalto.drumbeat.DrumbeatUserManager.events.EventBusCommunication;
import fi.aalto.drumbeat.DrumbeatUserManager.events.ModelTurtles_Event;

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


public class DrumbeatGraphDatabase {
	private EventBusCommunication communication = EventBusCommunication.getInstance();
	final private Model model;
	final private com.hp.hpl.jena.rdf.model.Model jena_model;

	static private Optional<DrumbeatGraphDatabase> singleton = Optional.empty();

	static public DrumbeatGraphDatabase getInstance() {
		if (!singleton.isPresent())
			singleton = Optional.of(new DrumbeatGraphDatabase());
		return singleton.get();
	}

	public DrumbeatGraphDatabase() {
		ModelFactory modelFactory = new ModelFactoryImpl();
		model = modelFactory.createModel();
		model.open();
		jena_model = (com.hp.hpl.jena.rdf.model.Model) model.getUnderlyingModelImplementation();
		communication.register(this);
		read_data();
	}

	// Just for tests
	@SuppressWarnings("unused")
	private void listRDFContent() {
		ClosableIterator<Statement> iterator = model.iterator();
		StringBuffer sb = new StringBuffer();
		while (iterator.hasNext()) {
			Statement stmt = iterator.next();
			//if (!stmt.getPredicate().toString().endsWith("bindingClass"))
				sb.append(stmt.getSubject() + " " + stmt.getPredicate() + " " + stmt.getObject());
			sb.append("\n");
		}
	}

	public com.hp.hpl.jena.rdf.model.Model filterJavaFromModel() {
		com.hp.hpl.jena.rdf.model.Model result = com.hp.hpl.jena.rdf.model.ModelFactory.createDefaultModel();

		try {

			StmtIterator statements = jena_model.listStatements();
			while (statements.hasNext()) {
				com.hp.hpl.jena.rdf.model.Statement stmt = statements.next();

				if (!stmt.getPredicate().toString().endsWith("bindingClass")) {
					result.add(stmt);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void sendNtriplesContent() {
		System.out.println("Send triples...");
		StringWriter writer = new StringWriter();
		com.hp.hpl.jena.rdf.model.Model ret = filterJavaFromModel();
		ret.write(writer, "TURTLE");
		communication.post(new ModelTurtles_Event(writer.toString()));

	}

	public String getJSON_LDContent() {
		System.out.println("Send triples...");
		StringWriter writer = new StringWriter();
		com.hp.hpl.jena.rdf.model.Model ret = filterJavaFromModel();
		ret.write(writer, "JSON-LD");
		return writer.toString();
	}

	@Subscribe
	public void onCompanyChangeEvent(CompanyDataChange_Event company_event) {
		System.out.println("Data save: Company");
		RDFBeanManager manager = new RDFBeanManager(model);
		jena_model.removeAll(); // clear old

		try {
			Resource r = manager.add(company_event.getCompany());
			System.out.println("----------------------------");
			listRDFContent();
			System.out.println("----------------------------");
		} catch (RDFBeanException ex) {
			ex.printStackTrace();
		}
		sendNtriplesContent();
		save_data(company_event.getCompany());
	}

	private void save_data(Company company) {
		try {
			XStream xstream = new XStream();
			String xml = xstream.toXML(company);
			try {
				String path = "/var/drumbeat_admin";
				if (isWindows())
					path = "c:" + path;
				File f = new File(path);
				java.nio.file.Path path_base = Paths.get(path);
				if (!f.exists()) {

					Files.createDirectory(path_base);
				} else if (!f.isDirectory()) {
					System.err.println("Is not a path: " + path);
					return;
				}
				Files.write(Paths.get(path_base.toString()+"/company.xml"), xml.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void read_data() {
		try {
			XStream xstream = new XStream(){
			    @Override
			    protected MapperWrapper wrapMapper(MapperWrapper next) {
			        return new MapperWrapper(next) {
			            @Override
			            public boolean shouldSerializeMember(Class definedIn, String fieldName) {
			                if (definedIn == Object.class) {
			                    return false;
			                }
			                return super.shouldSerializeMember(definedIn, fieldName);
			            }
			        };
			    }
			};
			try {
				String path = "/var/drumbeat_admin";
				if (isWindows())
					path = "c:" + path;
				File f = new File(path);
				java.nio.file.Path path_base = Paths.get(path);
				if (!f.exists()) {

					Files.createDirectory(path_base);
				} else if (!f.isDirectory()) {
					System.err.println("Is not a path: " + path);
					communication.post(new CompanyRead_Event(1, new Company()));
					return;
				}
				byte[] data=Files.readAllBytes(Paths.get(path_base.toString()+"/company.xml"));
				String xml=new String(data);
				Company company = (Company) xstream.fromXML(xml);				
				communication.post(new CompanyRead_Event(1, company));
				return;
			} 
			
			catch(NoSuchFileException e)
			{
				// OK
			}
			catch (IOException e) {
				
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		communication.post(new CompanyRead_Event(1, new Company()));
	}

	private static boolean isWindows() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("win") >= 0);
	}

	public static void main(String[] args) {
		DrumbeatGraphDatabase db = DrumbeatGraphDatabase.getInstance();
	}
}
