package fi.aalto.drumbeat.DrumbeatUserManager.entities;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;

import Java2OWL.J2OWLClass;
import Java2OWL.J2OWLProperty;
import fi.aalto.drumbeat.DrumbeatUserManager.utils.tags.DrumbeatFormField;
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


@J2OWLClass(name = "Employee", OWLSuperClasses = "Thing")
@RDFBean("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#Employee")
public class Employee extends AbstractData {
	String first_name="";
	String last_name="";
	String job_role="";
	String webID="http://";
	
	public Employee() {
		super(null);
	}

	
	public Employee(AbstractData parent) {
		super(parent);
	}

	@DrumbeatFormField 
	@J2OWLProperty ( name = "first_name" , setter="setFirst_name" )	
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#first_name") 
	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	@DrumbeatFormField
	@J2OWLProperty ( name = "last_name" , setter="setLast_name" )
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#last_name") 
	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	@DrumbeatFormField
	@J2OWLProperty ( name = "job_role" , setter="setJob_role" )
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#job_role") 
	public String getJob_role() {
		return job_role;
	}


	public void setJob_role(String job_role) {
		this.job_role = job_role;
	}

	
	@DrumbeatFormField
	@J2OWLProperty ( name = "webID" , setter="setWebID" )
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#webID") 
	public String getWebID() {
		return webID;
	}



	public void setWebID(String webID) {
		this.webID = webID;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	@Override
	public String toString() {
		return first_name+" "+last_name;
	}



	
}
