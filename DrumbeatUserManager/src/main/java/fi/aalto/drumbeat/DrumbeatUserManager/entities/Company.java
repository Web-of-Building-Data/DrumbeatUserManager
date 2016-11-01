package fi.aalto.drumbeat.DrumbeatUserManager.entities;

import java.util.HashSet;
import java.util.Set;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;

import Java2OWL.J2OWLClass;
import Java2OWL.J2OWLProperty;
import fi.aalto.drumbeat.DrumbeatUserManager.events.EventBusCommunication;
import fi.aalto.drumbeat.DrumbeatUserManager.utils.tags.DrumbeatFormField;
import fi.aalto.drumbeat.DrumbeatUserManager.vo.EmployeeCollection;
import fi.aalto.drumbeat.DrumbeatUserManager.vo.ProjectCollection;

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


@J2OWLClass(name = "Company", OWLSuperClasses = "Thing")
@RDFBean("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#Company")
public class Company extends AbstractData{
	 String name="";

    // This is to enable add buttons....sets could be inside 
	 // Voidaanko yleistää??
    EmployeeCollection personnel_collection = new EmployeeCollection(this);
	ProjectCollection  projects_collection = new ProjectCollection(this);
    Set<Employee>  personnel=new HashSet<Employee>();
	Set<Project>   projects=new HashSet<Project>();
	 
	public Company() {
			super(null);  // Company does not have a parent
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
	
	@J2OWLProperty ( name = "personnel" , setter="setPersonnel" )	
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#personnel") 	
	public Set<Employee> getPersonnel() {
		return personnel;
	}

	public void setPersonnel(Set<Employee> personnel) {
		this.personnel = personnel;
	}

	@J2OWLProperty ( name = "projects" , setter="setProjects" )	
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#projects") 	
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	
	public void setId(String id) {
		this.id = id;
	}

	
	

	public EmployeeCollection getPersonnel_collection() {
		return personnel_collection;
	}

	public void setPersonnel_collection(EmployeeCollection personnel_collection) {
		this.personnel_collection = personnel_collection;
	}

	public ProjectCollection getProjects_collection() {
		return projects_collection;
	}

	public void setProjects_collection(ProjectCollection projects_collection) {
		this.projects_collection = projects_collection;
	}

	@Override
	public boolean add(AbstractData data) {
		if(data.getClass().equals(Project.class))
			return projects.add((Project) data);
		if(data.getClass().equals(Employee.class))
			return personnel.add((Employee) data);
		return true;
	}

	@Override
	public boolean remove(AbstractData data) {
		if(data.getClass().equals(Project.class))
			return projects.remove((Project) data);
		if(data.getClass().equals(Employee.class))
		{
			System.out.println("person remove!!");
			return personnel.remove((Employee) data);
		}
		return true;
	}

	@Override
	public void sendInitialEvents(EventBusCommunication communication)
	{
	   communication.post(this);
	   for (Employee e: personnel)
		   e.sendInitialEvents(communication);
	   for (Project p: projects)
		   p.sendInitialEvents(communication);
	}
	
	@Override
	public String toString() {
		return name;
	}



	
	}
