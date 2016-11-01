package fi.aalto.drumbeat.DrumbeatUserManager;

import java.util.Collection;
import java.util.HashSet;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import fi.aalto.drumbeat.DrumbeatUserManager.entities.Company;
import fi.aalto.drumbeat.DrumbeatUserManager.entities.Employee;
import fi.aalto.drumbeat.DrumbeatUserManager.entities.ProjectRole;
import fi.aalto.drumbeat.DrumbeatUserManager.events.EventBusCommunication;

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


class PersonsSelectDialogBox extends Window {
	private EventBusCommunication communication = EventBusCommunication.getInstance();
	private static final long serialVersionUID = 1L;

	public PersonsSelectDialogBox(ProjectRole data, Company company) {
		super(""); // Set window caption
		center();
		setHeight("330px");
		setWidth("400px");

		// Some basic content for the window
		VerticalLayout content = new VerticalLayout();
		TwinColSelect select = new TwinColSelect("");
		// select.setHeight("200px");
		// Put some items in the select
		for (Employee e : company.getPersonnel())
			select.addItem(e);

		// Few items, so we can set rows to match item count
		select.setRows(select.size());

		// Preselect a few items by creating a set
		HashSet<Employee> value = new HashSet<Employee>(data.getEmployees());
		select.setValue(value);

		content.addComponent(select);

		HorizontalLayout buttons_line = new HorizontalLayout();
		Button cancel = new Button("Cancel");
		cancel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				close(); // Close the sub-window
			}
		});

		String button_txt = null;
		button_txt = "Save";

		final HashSet selection = new HashSet(); // selected content
		Button save = new Button(button_txt);
		save.setIcon(new ThemeResource("images/edit2.png"));
		save.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {

				data.getEmployees().clear();
				for (Object person : selection) {
					data.getEmployees().add((Employee) person);
				}
				communication.post(data);
				close(); // Close the sub-window
			}
		});
		select.setLeftColumnCaption("Selectable users");
		select.setRightColumnCaption("Users in the role");
		select.setImmediate(true);
		buttons_line.addComponents(cancel, save);
		content.addComponent(buttons_line);
		content.setMargin(true);
		content.setSpacing(true);

		setContent(content);
		content.setSizeFull();
		// Disable the close button
		setClosable(false);

		// Handle value changes
		select.addValueChangeListener(event -> // Java 8
		{
			Collection s = (Collection) event.getProperty().getValue();
			selection.clear();
			selection.addAll(s);
			// cla

		});

	}
}