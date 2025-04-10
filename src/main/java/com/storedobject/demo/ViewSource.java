package com.storedobject.demo;

import com.storedobject.vaadin.ApplicationFrame;
import com.storedobject.vaadin.CloseableView;
import com.storedobject.vaadin.TextArea;
import com.storedobject.vaadin.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ViewSource extends View implements CloseableView {

    public ViewSource(String source) {
        super(source + ".java");
        TextArea textArea = new TextArea();
        textArea.setValue(loadSource(source).toString());
        textArea.setReadOnly(true);
        setComponent(textArea);
    }

    private StringBuilder loadSource(String file) {
        BufferedReader r;
        r = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ApplicationFrame.class.getClassLoader().getResourceAsStream("demo/" + file + ".java")),
                StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = r.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception ignored) {
        } finally {
            try {
                r.close();
            } catch (IOException ignored) {
            }
        }
        return sb;
    }
}
