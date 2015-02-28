/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Scriptoholic;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 *
 * @author bryant
 */
public class PackageRipper {

    public void rip(String path, String root) {
        ArrayList<String> classes = new ArrayList<>();
        //File file = new File(path);
        URL[] urls = null;
        ClassLoader cl = null;
        try {
//            JarFile jarFile = new JarFile(path);
//            Enumeration e = jarFile.entries();
//
//            URL[] urlz = {new URL("jar:file:" + path + "!/")};
//            URLClassLoader clz = URLClassLoader.newInstance(urlz);
//
//            while (e.hasMoreElements()) {
//                JarEntry je = (JarEntry) e.nextElement();
//                if (je.isDirectory() || !je.getName().endsWith(".class")) {
//                    continue;
//                }
//                // -6 because of .class
//                String className = je.getName().substring(0, je.getName().length() - 6);
//                className = className.replace('/', '.');
//                try {
//                    //Class c = clz.loadClass(className);
//                    classes.add(className);
//                } catch (Exception ex) {
//                    //ex.printStackTrace();
//                }
//
//            }
//
            URL url = new URL("file://" + path);//file.toURL();          // file:/c:/myclasses/
            urls = new URL[]{url};
//
            cl = new URLClassLoader(urls);
//
        } catch (Exception e) {
//            e.printStackTrace();
        }

        //System.err.println(cl.toString());
        System.out.println("Processing " + path);

       List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
       classLoadersList.add(ClasspathHelper.contextClassLoader());
       classLoadersList.add(ClasspathHelper.staticClassLoader());
       classLoadersList.add(cl);
        ConfigurationBuilder cb = new ConfigurationBuilder()
                .setScanners(
                        new SubTypesScanner(false /* don't exclude Object.class */),
                        new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])));
        
            cb.addClassLoaders(classLoadersList);
            cb.addUrls(urls);
            
        Reflections reflections = new Reflections(cb
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(root)))
        );
        Set<Class<? extends Object>> allClasses
                = reflections.getSubTypesOf(Object.class);

        //Set<String> at = r.getAllTypes();
        System.out.println("Jar contained " + allClasses.size() + " classes that extend object");
//        System.out.println("Jar contained " + at.size() + " types");

        if (allClasses.size() > 0) {
            System.out.println("\n\n---------------------------------------\nAll Classes Extending Object\n---------------------------------------");
        }
        for (Class c : allClasses) {
            System.out.println(c.getName());
        }

        if (classes.size() > 0) {
            System.out.println("\n\n---------------------------------------\nAll Classes in path\n---------------------------------------");
        }
        for (String c : classes) {
            System.out.println(c);
        }

//        if(allClasses.size() > 0)
//            System.out.println("\n\n---------------------------------------\nAll Classes Extending Object\n---------------------------------------");
//        for(String c : at)
//        {
//            System.out.println(c);
//        }
    }

}
