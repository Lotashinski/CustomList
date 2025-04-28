package com.github.lotashinski;

import java.util.List;

import com.github.lotashinski.collections.CustomList;

/**
 * Hello world!
 */
public class App {
    
	public static void main(String[] args) {
        List<String> list = new CustomList<>();
        list.add("Hello");
        list.add("!");
        list.add(" ");
        list.add("World");
        list.add("!");
        
        for (String string : list) {
        	System.out.print(string);
        }
        
        CustomList<Person> persons = new CustomList<>();
        persons.sort(null);
    }
    
}
