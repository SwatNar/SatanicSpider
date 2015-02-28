/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Scriptoholic;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 *
 * @author bryant
 */
public class Scriptoholic2 extends Application {

    @Override
    public void start(Stage primaryStage) {

        Button btn = new Button();
        TextArea txtConsole = new TextArea();
        TextArea txtScript = new TextArea();

        btn.setText("Execute");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //String script = txtScript.getText();
                //System.out.println("Executing " + script);
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(txtConsole);
        root.getChildren().add(txtScript);
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
//         PackageRipper rip = new PackageRipper();
//        rip.rip("/tmp/joal.jar","");
        URL url = new URL("file://" + "/tmp/test.jar");
        URL[] urls = new URL[]{url};
//
        ClassLoader urlcl = new URLClassLoader(urls);
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().setUrls(
                        ClasspathHelper.forClassLoader(urlcl)
                ).addClassLoader(urlcl)
                .setScanners(
                        new SubTypesScanner(false /* don't exclude Object.class */),
                        new ResourcesScanner(),
                        new TypeElementsScanner()
                )
        );
        Set<Class<? extends Object>> objs = reflections.getSubTypesOf(Object.class);
        System.out.println(objs.size());
        for (Class c : objs) {
            System.out.println(c.getName());
        }
        //launch(args);
    }

    public static void parse(String filter,GUIConsole con) {
        try {
        // URL url = new URL("file://" + "/tmp/test.jar");
            //URL[] urls = new URL[]{url};
//
            //ClassLoader urlcl = new URLClassLoader(urls);
            Reflections reflections = new Reflections(
                    new ConfigurationBuilder().setUrls(
                            ClasspathHelper.forClassLoader(ClasspathHelper.contextClassLoader())//urlcl)
                    ).addClassLoader(ClasspathHelper.contextClassLoader())//urlcl)
                    .setScanners(
                            new SubTypesScanner(false /* don't exclude Object.class */),
                            new ResourcesScanner(),
                            new TypeElementsScanner()
                    )
                    .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(filter)))
            );
            Set<Class<? extends Object>> objs = reflections.getSubTypesOf(Object.class);
            HashMap<Class, ArrayList<Method>> methods = new HashMap<>();
            HashMap<String, Integer> nameCount = new HashMap<>();
            HashMap<String, Class> baseMap = new HashMap<>();
            System.out.println(objs.size());
            for (Class c : objs) {
                String basename = c.getSimpleName();

                System.out.println(c.getName() + " => " + basename);

                if (!nameCount.containsKey(basename)) {
                    nameCount.put(basename, 0);
                }
                nameCount.put(basename, nameCount.get(basename) + 1);
                baseMap.put(basename, c);
                Method[] m = c.getMethods();
                ArrayList<Method> meth = new ArrayList<>();
                for (Method mm : m) {
                    System.out.println("\t" + mm.toString());
                    meth.add(mm);
                }
                methods.put(c, meth);
            }
            
            for(String key : nameCount.keySet())
            {
                if(nameCount.get(key) == 1)
                {
                    //We can use the common name to link to the engine
                    System.out.println("Mapping " + key + " to the engine as is");
                    Class c = baseMap.get(key);
                    con.bind_static(key, c.getName());
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
