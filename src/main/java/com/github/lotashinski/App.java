package com.github.lotashinski;

import com.github.lotashinski.collections.CustomList;

public class App {
    
	public static void main(String[] args) {
		CustomList<String> list = new CustomList<>();
        list.add("Hello");
        list.add("!");
        list.add(" ");
        list.add("World");
        list.add("!");
        
        for (String string : list) {
        	System.out.print(string);
        }
        System.out.println();
        
        list.sort();
        
        for (String string : list) {
        	System.out.print(string);
        }
    }
    
}
