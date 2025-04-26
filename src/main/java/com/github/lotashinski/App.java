package com.github.lotashinski;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        
        Iterator<String> itr = list.iterator();
        System.out.println(itr.next());
        
        list.set(2, "5");
        
        System.out.println(itr.next());
    }
}
