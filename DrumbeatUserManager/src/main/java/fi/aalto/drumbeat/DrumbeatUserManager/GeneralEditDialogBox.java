package fi.aalto.drumbeat.DrumbeatUserManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import fi.aalto.drumbeat.DrumbeatUserManager.entities.AbstractData;
import fi.aalto.drumbeat.DrumbeatUserManager.events.EventBusCommunication;
import fi.aalto.drumbeat.DrumbeatUserManager.utils.JavaReflectorUtil;
import fi.aalto.drumbeat.DrumbeatUserManager.utils.ValuePair;

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


class GeneralEditDialogBox extends Window {
	private EventBusCommunication communication = EventBusCommunication.getInstance();
	private static final long serialVersionUID = 1L;
    private final Map<TextField,ValuePair> field_map=new HashMap<TextField,ValuePair>();
	
	// isEdit: true=edit old value, false=add
	public GeneralEditDialogBox(AbstractData data,boolean isEdit,boolean hasDelete) {
        super(""); // Set window caption
        center();
       

        // Some basic content for the window
        VerticalLayout content = new VerticalLayout();
        System.out.println("Data class: "+data.getClass().getSimpleName());
        List<ValuePair> fields=JavaReflectorUtil.annotated_fields(data);
		for(ValuePair vp:fields)
		{
	        HorizontalLayout line = new HorizontalLayout();
	        Label l=new Label(vp.getName()+": ");
	        l.setWidth("250px");
	        TextField tf=new TextField ();
	        tf.setWidth("400px");
	        tf.setValue(vp.getValue());
	        field_map.put(tf, vp);
			line.addComponents(l,tf);
			content.addComponent(line);
		}

        
        content.setMargin(true);
        setContent(content);
        // Disable the close button
        setClosable(false);

        
        HorizontalLayout buttons_line = new HorizontalLayout();
        
        
        Button cancel = new Button("Cancel");
        cancel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
                close(); // Close the sub-window
            }
        });

        String button_txt=null;
        if(isEdit)
        	button_txt="Save";
        else
        	button_txt="Add";
        Button save = new Button(button_txt);
        if(isEdit)
        {
        	save.setIcon(new ThemeResource("images/edit2.png"));
        }
        else
        {
        	save.setIcon(new ThemeResource("images/1475717585_document-new.png"));
        }
        save.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
            public void buttonClick(ClickEvent event) {
            	for (Map.Entry<TextField , ValuePair> entry : field_map.entrySet())
            	{
            		TextField tf=entry.getKey();            	  
            	    ValuePair vp=entry.getValue();
            	    vp.setValue(tf.getValue());
            	    
            	    communication.post(data);
            	}
                close(); // Close the sub-window
            }
        });
        buttons_line.addComponents(cancel,save);
        
        if(hasDelete)
        {
            Button delete = new Button("Delete");
            //delete.setIcon(new ThemeResource("images/edit2.png"));
            delete.addClickListener(new ClickListener() {
    			private static final long serialVersionUID = 1L;
                public void buttonClick(ClickEvent event) {
                	for (@SuppressWarnings("unused") Map.Entry<TextField , ValuePair> entry : field_map.entrySet())
                	{
                		data.setDelete_set(true);
                	    communication.post(data);
                	}
                    close(); // Close the sub-window
                }
            });
            buttons_line.addComponents(delete);
        	
        }
        content.addComponent(buttons_line);
    }
}