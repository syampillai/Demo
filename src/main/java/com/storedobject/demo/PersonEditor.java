package com.storedobject.demo;

import com.storedobject.vaadin.DataEditor;
import com.storedobject.vaadin.View;
import com.vaadin.flow.shared.Registration;

public class PersonEditor extends DataEditor<Person> implements Person.ChangeListener {

    private Registration registration;

    public PersonEditor() {
        super(Person.class, "Person", Person::save);
        addField("Age");
    }

    @Override
    protected int getFieldOrder(String fieldName) {
        return switch (fieldName) {
            case "FirstName" -> 100;
            case "LastName" -> 200;
            case "DateOfBirth" -> 300;
            default -> super.getFieldOrder(fieldName);
        };
    }

    @Override
    protected void validateData() throws Exception {
        Person p = getObject();
        if(isEmpty(p.getFirstName()) && isEmpty(p.getLastName())) {
            warning("Both 'First Name' and 'Last Name' can not be empty!");
        }
        super.validateData();
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    public void refreshPersons() {
    }

    @Override
    public void refreshPerson(Person person) {
        if(person.equals(getObject())) {
            setObject(person);
        }
    }

    @Override
    public void clean() {
        super.clean();
        if(registration != null) {
            registration.remove();
            registration = null;
        }
    }

    @Override
    protected void execute(View parent, boolean doNotLock) {
        if(registration == null) {
            registration = Person.register(this);
        }
        super.execute(parent, doNotLock);
    }
}