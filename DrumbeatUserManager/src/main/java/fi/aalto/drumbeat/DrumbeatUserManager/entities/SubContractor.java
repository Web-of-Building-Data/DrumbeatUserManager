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


@J2OWLClass(name = "Subcontractor", OWLSuperClasses = "Thing")
@RDFBean("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#Subcontractor")
public class SubContractor extends AbstractData {
	String name="";
	
	String role_in_projext="";
	//Remote
	String subcontractor_company_url="http://";

	public SubContractor() {
		super(null);
	}

	public SubContractor(AbstractData parent) {
		super(parent);
	}
	
	@DrumbeatFormField
	@J2OWLProperty ( name = "name" , setter="setName" )
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#name") 
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@DrumbeatFormField
	@J2OWLProperty ( name = "subcontractor_company" , setter="setSubcontractor_company" )
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#subcontractor_company") 
	public String getSubcontractor_company_url() {
		return subcontractor_company_url;
	}

	public void setSubcontractor_company_url(String subcontractor_company_url) {
		this.subcontractor_company_url = subcontractor_company_url;
	}

	
	@DrumbeatFormField
	@J2OWLProperty ( name = "role_in_project" , setter="setRole_in_projext" )
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#role_in_project") 
	public String getRole_in_projext() {
		return role_in_projext;
	}

	public void setRole_in_projext(String role_in_projext) {
		this.role_in_projext = role_in_projext;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "Subcontractor: "+name;
	}

	
}
