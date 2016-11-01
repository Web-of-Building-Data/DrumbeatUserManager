package fi.aalto.drumbeat.DrumbeatUserManager.entities;

import java.util.HashSet;
import java.util.Set;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;

import Java2OWL.J2OWLClass;
import Java2OWL.J2OWLProperty;
import fi.aalto.drumbeat.DrumbeatUserManager.events.EventBusCommunication;
import fi.aalto.drumbeat.DrumbeatUserManager.utils.tags.DrumbeatFormField;
import fi.aalto.drumbeat.DrumbeatUserManager.vo.RoleMembersCollection;

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


@J2OWLClass(name = "ProjectRole", OWLSuperClasses = "Thing")
@RDFBean("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#ProjectRole")
public class ProjectRole extends AbstractData {
	String name = "";

	RoleMembersCollection rolemembers_collection = new RoleMembersCollection(this);
	Set<Employee> employees = new HashSet<Employee>();

	public ProjectRole() {
		super(null);
	}

	public ProjectRole(AbstractData parent) {
		super(parent);
	}

	public RoleMembersCollection getRolemembers_collection() {
		return rolemembers_collection;
	}

	public void setRolemembers_collection(RoleMembersCollection rolemembers_collection) {
		this.rolemembers_collection = rolemembers_collection;
	}

	@DrumbeatFormField
	@J2OWLProperty(name = "name", setter = "setName")
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	@DrumbeatFormField
	@J2OWLProperty(name = "in_role", setter = "setEmployees")
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#in_role")
	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public void sendInitialEvents(EventBusCommunication communication)
	{
	   communication.post(this);
	   // TODO  näitä ei vielä ole
	   //for (Project p: employees)
	    //	p.sendInitialEvents(communication);
	}
	
	@Override
	public boolean add(AbstractData data) {
		if(data.getClass().equals(Employee.class))
			return employees.add((Employee) data);
		return true;
		
	}
	
	@Override
	public boolean remove(AbstractData data) {
		if(data.getClass().equals(Employee.class))
			return employees.remove((Employee) data);
		return true;
		
	}
	
	@Override
	public String toString() {
		return "Role: "+name;
	}

}
