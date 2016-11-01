package fi.aalto.drumbeat.DrumbeatUserManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.annotation.WebServlet;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Item;
import com.vaadin.event.UIEvents;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import fi.aalto.drumbeat.DrumbeatUserManager.entities.AbstractData;
import fi.aalto.drumbeat.DrumbeatUserManager.entities.Company;
import fi.aalto.drumbeat.DrumbeatUserManager.entities.Employee;
import fi.aalto.drumbeat.DrumbeatUserManager.entities.Project;
import fi.aalto.drumbeat.DrumbeatUserManager.entities.ProjectRole;
import fi.aalto.drumbeat.DrumbeatUserManager.entities.SubContractor;
import fi.aalto.drumbeat.DrumbeatUserManager.events.CompanyDataChange_Event;
import fi.aalto.drumbeat.DrumbeatUserManager.events.CompanyRead_Event;
import fi.aalto.drumbeat.DrumbeatUserManager.events.EventBusCommunication;
import fi.aalto.drumbeat.DrumbeatUserManager.events.ModelTurtles_Event;
import fi.aalto.drumbeat.DrumbeatUserManager.events.REST_Event;
import fi.aalto.drumbeat.DrumbeatUserManager.vo.AbstractCollection;

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


// Push(PushMode.AUTOMATIC)
@Theme("drumbeat")
public class UserManagerUI extends UI {
	private EventBusCommunication communication = EventBusCommunication.getInstance();
	private static final long serialVersionUID = 1L;

	// Here the only one state of the data
	//static Company company = null;
	static Optional<Company> company = Optional.empty();
	// Just create
	@SuppressWarnings("unused")
	private DrumbeatGraphDatabase db;
	// ID->DAta
	// Only one view
    final static public Map<String,AbstractData> id_map=new HashMap<String,AbstractData>();
    static public String base_dataURI="";
    
	// UI table...
	// --------------------------
	TreeTable ttable = null;

	private final Label company_label = new Label("");

	@SuppressWarnings("serial")
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		String basePath = Page.getCurrent().getLocation().getScheme() + ":" +
                Page.getCurrent().getLocation().getSchemeSpecificPart();
		base_dataURI=basePath.replace("/ui", "/resource/");
		System.out.println("VAAADIN BASE: "+base_dataURI);                
                
		communication.register(this);
		setPollInterval(2000);
		addPollListener(new UIEvents.PollListener() {
			@Override
			public void poll(UIEvents.PollEvent event) {
				if (triple_data.isPresent()) {
					tf.setValue(triple_data.get());
					triple_data = Optional.empty();
				}
			}
		});
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

		// Image as a file resource
		FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/drumbeat_banner_small2.jpg"));

		// Show the image in the application
		Image drumbeat_logo = new Image("Aalto University", resource);

		TabSheet tabsheet = new TabSheet();
		tabsheet.setSizeFull();
		final VerticalLayout main_layout = new VerticalLayout();
		main_layout.setStyleName("drumbeat");

		main_layout.addComponent(drumbeat_logo);
		main_layout.addComponent(tabsheet);

		createOrganizationManagerTab(tabsheet);
		createTestLoginTab(tabsheet);
		//createAuthenticationRulesManagerTab(tabsheet);
		
		//createGraphTab(tabsheet);
		createUnderTheHoodTab(tabsheet);

		main_layout.setMargin(true);
		main_layout.setSpacing(true);

		setContent(main_layout);
		db=DrumbeatGraphDatabase.getInstance();
		
	}

	private void createOrganizationManagerTab(TabSheet tabsheet) {
		VerticalLayout tab_site = new VerticalLayout();
		tab_site.setCaption("Company organization data");
		tab_site.setSizeFull();

		tabsheet.addTab(tab_site);

		VerticalLayout ttable_panel = new VerticalLayout();
		ttable_panel.setHeight(70, Unit.PERCENTAGE);
		ttable = new TreeTable("Company organization structure");
		ttable.setSizeFull();
		ttable.addContainerProperty("name", Label.class, null);
		ttable.addContainerProperty("value", Panel.class, null);
		ttable.setColumnWidth("value", 255);
		ttable.setColumnHeader("name", "");
		ttable.setColumnHeader("value", "");
		ttable.setCellStyleGenerator(new Table.CellStyleGenerator() {
		     
			private static final long serialVersionUID = 1L;

			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				if (propertyId == null) {
			          // Styling for row
			          Item item = ttable.getItem(itemId);
			          Label fieldName = (Label) item.getItemProperty("name").getValue();
			          if (fieldName.getValue().toLowerCase().endsWith("s")) {
			            return "highlight-white";
			          } 			         
			          else {
			        	  return "drumbeat";
			          }
			        } else {
			          // styling for column propertyId
			          return null;
			        }
			}
		    });
		addTableCompanyLevel();

		ttable_panel.addComponents(ttable);
		tab_site.addComponents(ttable_panel);
	}

	// Company level.....
	// ===================================
	private void addTableCompanyLevel() {
		if(!company.isPresent())
			return;
		final Panel p_company = new Panel();
		final Button b_company_edit = new Button("Edit");
		b_company_edit.setIcon(new ThemeResource("images/edit2.png"));
		b_company_edit.setWidth("250px");
		company_label.setValue("Company: " + company.get().getName());
		ttable.addItem(new Object[] { company_label, p_company }, company.get().getId());
		p_company.setContent(b_company_edit);
		b_company_edit.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				GeneralEditDialogBox sub = new GeneralEditDialogBox(company.get(), true, false);

				// Add it to the root component
				UI.getCurrent().addWindow(sub);
			}
		});

		addTableFirstLevelSubSection(company.get().getId(),company.get().getPersonnel_collection());
		addTableFirstLevelSubSection(company.get().getId(),company.get().getProjects_collection());

		if(company.isPresent())
		{
		 company.get().sendInitialEvents(communication);
		}

		for (Object itemId : ttable.getContainerDataSource().getItemIds()) {
			ttable.setCollapsed(itemId, false);
		}

	}

	// Staff, Projects...
	// Belongs to the basement
	private void addTableFirstLevelSubSection(String upperLevelId,AbstractCollection collection) {
		final Panel p_data = new Panel();
		final Button b_data_add = new Button("Add new " + collection.getType().toLowerCase());
		b_data_add.setIcon(new ThemeResource("images/1475717585_document-new.png"));

		b_data_add.setWidth("250px");

		ttable.addItem(new Object[] { new Label(collection.getType() + "s"), p_data }, collection.getId());
		p_data.setContent(b_data_add);
		ttable.setParent(collection.getId(), upperLevelId);
		b_data_add.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				AbstractData data = collection.getNew();
				GeneralEditDialogBox sub = new GeneralEditDialogBox(data, false, false);

				// Add it to the root component
				UI.getCurrent().addWindow(sub);
			}
		});
	}

	private void addTableFirstLevelinRoleSubSection(String upperLevelId,AbstractCollection collection,ProjectRole data) {
		final Panel p_data = new Panel();
		final Button b_data_add = new Button("Select employees");
		b_data_add.setIcon(new ThemeResource("images/1475717585_document-new.png"));

		b_data_add.setWidth("250px");

		ttable.addItem(new Object[] { new Label("Members"), p_data }, collection.getId());
		p_data.setContent(b_data_add);
		ttable.setParent(collection.getId(), upperLevelId);
		b_data_add.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if(company.isPresent())
				{
				PersonsSelectDialogBox sub = new PersonsSelectDialogBox(data,company.get());

				// Add it to the root component
				UI.getCurrent().addWindow(sub);
				}
			}
		});
	}

	
	////////////////////////////////////////////////////
	// Dynamic content...
	// Edit level sections.... persons, projects.. subcontractors

	private void setTableDataEditSection(String upperLevelId, AbstractData data) {
		Panel panel = new Panel();
		HorizontalLayout button_line = new HorizontalLayout();
		final Button b_edit = new Button("Edit");
		b_edit.setIcon(new ThemeResource("images/edit2.png"));
		b_edit.setWidth("250px");

		if (ttable.getItem(data.getId()) != null)
			ttable.removeItem(data.getId());
		ttable.addItem(new Object[] { new Label(data.toString()), panel }, data.getId());
		ttable.setParent(data.getId(), upperLevelId);
		button_line.addComponent(b_edit);
		panel.setContent(button_line);

		b_edit.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				GeneralEditDialogBox sub = new GeneralEditDialogBox(data, true, true);

				// Add it to the root component
				UI.getCurrent().addWindow(sub);
			}
		});

		if(data instanceof Project)
		{
		  System.out.println("is Project");
		  Project p=(Project)data;	
		  addTableFirstLevelSubSection(p.getId(),p.getSubcontractors_collection());
		  addTableFirstLevelSubSection(p.getId(),p.getProjectroles_collection());
		  addTableFirstLevelSubSection(p.getId(),p.getSubprojects_collection());
		}
		else
			System.out.println("class was: "+data.getClass().getName());
			
		if(data instanceof ProjectRole)
		{
		  System.out.println("is ProjectRole");
		  ProjectRole p=(ProjectRole)data;	
		  addTableFirstLevelinRoleSubSection(p.getId(),p.getRolemembers_collection(),p);
		 
		}
		else
			System.out.println("class was: "+data.getClass().getName());

	}


	
	private void createTestLoginTab(TabSheet tabsheet) {
		VerticalLayout tab_site = new VerticalLayout();
		tab_site.setCaption("Test login");
		tab_site.setSizeFull();
		tabsheet.addTab(tab_site);
        Label exp=new Label("Under construction.... ");
        tab_site.addComponent(exp);
		
		HorizontalLayout line = new HorizontalLayout();
        Label l=new Label("WebID URL: ");
        l.setWidth("250px");
        TextField tf=new TextField ();
        tf.setWidth("400px");
        tf.setValue("");
		line.addComponents(l,tf);
		
        Button login = new Button("Login (test)");
        login.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
            public void buttonClick(ClickEvent event) {
            	exp.setCaption("Login test... OK: (still under construction)");
            }
        });
        line.addComponents(login);
        tab_site.addComponent(line);
	}

	private void createAuthenticationRulesManagerTab(TabSheet tabsheet) {
		VerticalLayout tab_site = new VerticalLayout();
		tab_site.setCaption("Authentication Rules Manager");
		tab_site.setSizeFull();
		tabsheet.addTab(tab_site);
	}


	private void createGraphTab(TabSheet tabsheet) {
		VerticalLayout tab_site = new VerticalLayout();
		tab_site.setCaption("Graph of the data");
		tab_site.setSizeFull();
		tabsheet.addTab(tab_site);
	}

	
	TextArea tf = new TextArea("Triples");

	private void createUnderTheHoodTab(TabSheet tabsheet) {
		VerticalLayout tab_site = new VerticalLayout();
		tab_site.setCaption("Under the hood");
		tab_site.setSizeFull();
		tabsheet.addTab(tab_site);
		// GraphDatabase g=GraphDatabase.getInstance();
		// tf.setValue(g.data2String());
		tf.setSizeFull();
		tf.setHeight("500px");
		tab_site.addComponents(tf);
	}

	Optional<String> triple_data = Optional.empty();

	@Subscribe
	public void onRESTEvent(REST_Event event) {
		System.out.println("UPDATE!!!");
		// GraphDatabase g=GraphDatabase.getInstance();
		// triple_data=Optional.of(g.data2String());
		// tf.setValue(triple_data.get());
	}

	@Subscribe
	public void onCompanyChangeEvent(Company event) {
		company_label.setValue("Company: " + event.getName());
		communication.post(new CompanyDataChange_Event(0, event));
	}

	@Subscribe
	public void onProjectUpdateEvent(Project event) {
		if (event.isDelete_set()) {
			ttable.removeItem(event.getId());
			event.getParent().remove(event);
		} else {
			event.getParent().add(event);
			setTableDataEditSection(event.getParent().getId(), event);
		}
		// has changed
		if(company.isPresent())
		 communication.post(new CompanyDataChange_Event(0, company.get()));
	}

	@Subscribe
	public void onEmployeeUpdateEvent(Employee event) {
		if (event.isDelete_set()) {
			ttable.removeItem(event.getId());
			event.getParent().remove(event);
		} else {
			event.getParent().add(event); 
			setTableDataEditSection(event.getParent().getId(), event);
		} 
		// has changed
		if(company.isPresent())
		 communication.post(new CompanyDataChange_Event(0, company.get()));
		
	}

	@Subscribe
	public void onSubcontractorsUpdateEvent(SubContractor event) {
		if (event.isDelete_set()) {
			ttable.removeItem(event.getId());
			event.getParent().remove(event);
		} else {
			event.getParent().add(event); 
			setTableDataEditSection(event.getParent().getId(), event);
		} 
		// has changed
		if(company.isPresent())
		  communication.post(new CompanyDataChange_Event(0, company.get()));
	}
	
	
	@Subscribe
	public void onProjectRoleUpdateEvent(ProjectRole event) {
		if (event.isDelete_set()) {
			ttable.removeItem(event.getId());
			event.getParent().remove(event);
		} else {
			event.getParent().add(event); 
			setTableDataEditSection(event.getParent().getId(), event);
		} 
		// has changed
		if(company.isPresent())
		 communication.post(new CompanyDataChange_Event(0, company.get()));
		
	}


	@Subscribe
	public void onModelTurtlesEvent(ModelTurtles_Event event) {
		tf.setValue(event.getContent());
		for (Object itemId : ttable.getContainerDataSource().getItemIds()) {
			ttable.setCollapsed(itemId, false);
		}
	}

	@Subscribe
	public void onCompanyRead_Event(CompanyRead_Event event) {
		Company c=event.getCompany();
		ttable.clear();
		company=Optional.of(c);
		addTableCompanyLevel();
			
	}

    

	@WebServlet(urlPatterns = { "/ui/*", "/VAADIN/*" }, name = "UserManagerUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = UserManagerUI.class, productionMode = false)
	public static class UserManagerUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}

}
