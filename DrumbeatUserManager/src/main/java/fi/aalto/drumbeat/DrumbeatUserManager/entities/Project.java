package fi.aalto.drumbeat.DrumbeatUserManager.entities;

import java.util.HashSet;
import java.util.Set;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;

import Java2OWL.J2OWLClass;
import Java2OWL.J2OWLProperty;
import fi.aalto.drumbeat.DrumbeatUserManager.events.EventBusCommunication;
import fi.aalto.drumbeat.DrumbeatUserManager.utils.tags.DrumbeatFormField;
import fi.aalto.drumbeat.DrumbeatUserManager.vo.ProjectRoleCollection;
import fi.aalto.drumbeat.DrumbeatUserManager.vo.SubContractorCollection;
import fi.aalto.drumbeat.DrumbeatUserManager.vo.SubProjectCollection;

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


@J2OWLClass(name = "Project", OWLSuperClasses = "Thing")
@RDFBean("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#Project")
public class Project extends AbstractData {
	String name = "";
	
	SubContractorCollection subcontractors_collection = new SubContractorCollection(this);
	ProjectRoleCollection  projectroles_collection = new ProjectRoleCollection(this);
	SubProjectCollection  subprojects_collection = new SubProjectCollection(this);
	
	
	Set<SubContractor> subcontractors = new HashSet<SubContractor>();
	Set<ProjectRole> projectroles = new HashSet<ProjectRole>();
	Set<Project> sub_projects = new HashSet<Project>();
	
	public Project() {
		super(null);
	}
	public Project(AbstractData parent) {
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


	

	@Override
	public boolean add(AbstractData data) {
		if(data.getClass().equals(SubContractor.class))
			return subcontractors.add((SubContractor) data);
		if(data.getClass().equals(Project.class))
			return sub_projects.add((Project) data);
		
		if(data.getClass().equals(ProjectRole.class))
			return projectroles.add((ProjectRole) data);

		return true;
		
	}
	
	@Override
	public boolean remove(AbstractData data) {
		if(data.getClass().equals(SubContractor.class))
			return subcontractors.remove((SubContractor) data);
		if(data.getClass().equals(Project.class))
			return sub_projects.remove((Project) data);		
		if(data.getClass().equals(ProjectRole.class))
			return projectroles.remove((ProjectRole) data);
		return true;
	}
	
	public SubContractorCollection getSubcontractors_collection() {
		return subcontractors_collection;
	}
	public void setSubcontractors_collection(SubContractorCollection subcontractors_collection) {
		this.subcontractors_collection = subcontractors_collection;
	}
	
	public ProjectRoleCollection getProjectroles_collection() {
		return projectroles_collection;
	}
	public void setProjectroles_collection(ProjectRoleCollection projectroles_collection) {
		this.projectroles_collection = projectroles_collection;
	}
	
	public SubProjectCollection getSubprojects_collection() {
		return subprojects_collection;
	}
	public void setSubprojects_collection(SubProjectCollection subprojects_collection) {
		this.subprojects_collection = subprojects_collection;
	}
	@J2OWLProperty ( name = "subcontractor" , setter="setSubcontractors" )
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#subcontractor") 	
	public Set<SubContractor> getSubcontractors() {
		return subcontractors;
	}

	public void setSubcontractors(Set<SubContractor> subcontractors) {
		this.subcontractors = subcontractors;
	}	

	@J2OWLProperty ( name = "projectroles" , setter="setProjectroles" )
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#projectroles") 	

	public Set<ProjectRole> getProjectroles() {
		return projectroles;
	}
	public void setProjectroles(Set<ProjectRole> projectroles) {
		this.projectroles = projectroles;
	}
	
	@J2OWLProperty ( name = "subproject" , setter="setSub_projects" )
	@RDF("http://drumbeat.cs.hut.fi/owl/DrumbeatUserAdmin.ttl#subproject") 	
	public Set<Project> getSub_projects() {
		return sub_projects;
	}

 	public void setSub_projects(Set<Project> sub_projects) {
		this.sub_projects = sub_projects;
	}


	@Override
	public void sendInitialEvents(EventBusCommunication communication)
	{
	   communication.post(this);
	   for (Project p: sub_projects)
		   p.sendInitialEvents(communication);
	   // Nämä toimivat jo
	   for (SubContractor e: subcontractors)
		   e.sendInitialEvents(communication);
	   for (ProjectRole p: projectroles)
		   p.sendInitialEvents(communication);
	   
	}
	

	
	@Override
	public String toString() {
		return "Project: "+name;
	}
}
