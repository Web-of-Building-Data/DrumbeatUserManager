package fi.aalto.drumbeat.DrumbeatUserManager.entities;

import java.util.UUID;

import com.viceversatech.rdfbeans.annotations.RDFSubject;

import fi.aalto.drumbeat.DrumbeatUserManager.events.EventBusCommunication;
import fi.aalto.drumbeat.DrumbeatUserManager.utils.JavaReflectorUtil;


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

public abstract class AbstractData {
	protected  String id = UUID.randomUUID().toString();
    private AbstractData parent;
    private boolean delete_set=false;
    
	public AbstractData(AbstractData parent) {
		super();
		this.parent = parent;
		JavaReflectorUtil.setHostName(this);
	}

    
	@RDFSubject(prefix="http://drumbeat.cs.hut.fi/DrumbeatUserAdmin/resource/")
	public String getId() {
		return id.toString();
	}

	public AbstractData getParent() {
		return parent;
	}

	public void setParent(AbstractData parent) {
		this.parent = parent;
	}

	public boolean isDelete_set() {
		return delete_set;
	}


	public void setDelete_set(boolean delete_set) {
		this.delete_set = delete_set;
	}
	
	public boolean add(AbstractData data){return true;};
	public boolean remove(AbstractData data){return true;};
	
	public void sendInitialEvents(EventBusCommunication communication)
	{
		   communication.post(this);	
	}
}
