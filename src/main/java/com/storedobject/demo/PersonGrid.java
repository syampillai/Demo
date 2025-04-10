package com.storedobject.demo;

import com.storedobject.vaadin.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.shared.Registration;

import java.util.Arrays;

public class PersonGrid extends DataGrid<Person> implements Person.ChangeListener {

    private Button add, edit;
    private Registration registration;

    public PersonGrid() {
        super(Person.class, Arrays.asList("FirstName", "LastName", "DateOfBirth", "Age"));
        setSizeFull();
        setCaption("Persons");
        setDataProvider(Person.getData());
        getApplication().startPolling(this);
    }

    @Override
    public Component createHeader() {
        add = new Button("Add", "plus",this);
        edit = new Button("Edit", this);
        return new ButtonLayout(add, edit, new Button("Quit", "exit", e -> close()));
    }

    @Override
    public void clicked(Component c) {
        if(c == add) {
            ((Demo)getApplication()).getPersonEditor(null).execute();
            return;
        }
        if(c == edit) {
            Person selected = getSelected();
            if(selected == null) {
                warning("Select a person to edit");
            } else {
                ((Demo)getApplication()).getPersonEditor(selected).execute();
            }
            return;
        }
        super.clicked(c);
    }

    @Override
    public void refreshPersons() {
        refresh();
    }

    @Override
    public void refreshPerson(Person person) {
        refresh(person);
    }

    @Override
    public View getView(boolean create) {
        if(registration == null) {
            registration = Person.register(this);
        }
        return super.getView(create);
    }

    @Override
    public void clean() {
        super.clean();
        if(registration != null) {
            registration.remove();
            registration = null;
            getApplication().stopPolling(this);
        }
    }
}