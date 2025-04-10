package com.storedobject.demo;

import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.shared.Registration;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

// Person class and its in-memory database
@SuppressWarnings("unused")
public class Person {

    // Simple in memory database of persons
    private static final List<Person> database = new ArrayList<>();
    // Change listeners
    private static final List<WeakReference<ChangeListener>> changeListeners = new ArrayList<>();
    // Just a unique ID for the person
    private final UUID id;
    // First name, Last name
    private String firstName, lastName;
    // Date of birth
    private LocalDate dateOfBirth = LocalDate.of(1998, 1, 23);

    public Person() {
        this("", "");
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        id = UUID.randomUUID();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Person && ((Person)obj).id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    // Create a "data provider" from the database
    public static ListDataProvider<Person> getData() {
        return new ListDataProvider<>(database);
    }

    // Save a person to the database
    public static void save(Person person) {
        changeListeners.removeIf(wr -> wr.get() == null);
        if(!database.contains(person)) {
            if(database.size() >= 500) {
                database.removeFirst(); // We don't want someone to grab a lot of memory on the demo server
            }
            database.add(person);
            changeListeners.stream().map(Reference::get).filter(Objects::nonNull).forEach(ChangeListener::refreshPersons);
        } else {
            changeListeners.stream().map(Reference::get).filter(Objects::nonNull).forEach(cl -> cl.refreshPerson(person));
        }
    }

    // Person change listener
    public interface ChangeListener {
        void refreshPersons();
        void refreshPerson(Person person);
    }

    // Register me for tracking changes in the database
    public static Registration register(ChangeListener changeListener) {
        if(valid(changeListener)) {
            final WeakReference<ChangeListener> changeListenerWeakReference = new WeakReference<>(changeListener);
            changeListeners.add(changeListenerWeakReference);
            return () -> changeListeners.remove(changeListenerWeakReference);
        }
        return null;
    }

    private static boolean valid(ChangeListener changeListener) {
        if(changeListener == null) {
            return false;
        }
        return changeListeners.stream().noneMatch(wr -> wr.get() == changeListener);
    }
}
