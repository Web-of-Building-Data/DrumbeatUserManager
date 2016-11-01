package fi.aalto.drumbeat.DrumbeatUserManager.vo;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.ModelFactory;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.Resource;

import com.viceversatech.rdfbeans.RDFBeanManager;
import com.viceversatech.rdfbeans.exceptions.RDFBeanException;

import fi.aalto.drumbeat.DrumbeatUserManager.entities.Company;
import fi.aalto.drumbeat.DrumbeatUserManager.entities.Employee;

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


public class Testeri4RDF {

	public Testeri4RDF()
	{
		ModelFactory modelFactory = RDF2Go.getModelFactory();
		Model model = modelFactory.createModel();
		model.open();        
		
		RDFBeanManager manager = new RDFBeanManager(model);
		Company c=new Company();
		c.setName("Name");
		
		Employee e=new Employee(null);
		e.setFirst_name("John");
		e.setLast_name("Smith");
		e.setJob_role("Super");
		
		c.getPersonnel().add(e);
		try {
			@SuppressWarnings("unused")
			Resource r = manager.add(c);
		} catch (RDFBeanException ex) {
			ex.printStackTrace();
		}
		/*try {
			Company co=manager.get(c._getId(), Company.class);
			co.setName("joo");
			
		} catch (RDFBeanException e1) {
			e1.printStackTrace();
		}*/
		
		ClosableIterator<Statement> iterator=model.iterator();
		while (iterator.hasNext())
		{
			 Statement stmt      = iterator.next();
			 //if(!stmt.getPredicate().toString().endsWith("bindingClass"))
			 System.out.println(stmt.getSubject()+" "+stmt.getPredicate()+" "+stmt.getObject());
		}
		
		

	}
	
	public static void main(String[] args) {
		new Testeri4RDF();
	}
}
