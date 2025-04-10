package com.storedobject.demo;

import com.storedobject.vaadin.*;
import com.vaadin.flow.router.Route;

public class Demo extends Application {

    private PersonEditor personEditor;
    private PersonGrid personGrid;

    public Demo() {
    }

    PersonEditor getPersonEditor(Person person) {
        if(personEditor == null) {
            personEditor = new PersonEditor();
        }
        personEditor.setObject(person == null ? new Person() : person);
        return personEditor;
    }

    PersonGrid getPersonGrid() {
        if(personGrid == null) {
            personGrid = new PersonGrid();
        }
        return personGrid;
    }

    @Override
    public ApplicationLayout createLayout() {
        return new SimpleLayout();
    }

    private static class SimpleLayout extends ApplicationFrame {

        public SimpleLayout() {
            setCaption("SO Demo - Ver 2.6");
        }

        @Override
        public void drawMenu(Application a) {
            ApplicationMenu m = getMenu();
            m.add(a.createMenuItem("Add New Person", () -> ((Demo)a).getPersonEditor(null).execute()));
            m.add(a.createMenuItem("List of Persons", () -> ((Demo)a).getPersonGrid().execute()));
            String[] files = new String[] { "Demo", "Person", "PersonEditor", "PersonGrid", "ViewSource" };
            ApplicationMenuItemGroup mg = a.createMenuItemGroup("View Source Code");
            m.add(mg);
            for (String file : files) {
                ApplicationMenuItem mi;
                mg.add(mi = a.createMenuItem(file + ".java", new ViewSource(file)));
                m.add(mi);
            }
        }
    }

    @SuppressWarnings("unused")
    @Route("")
    public static class DemoView extends ApplicationView {

        @Override
        protected Application createApplication() {
            return new Demo();
        }
    }
}
