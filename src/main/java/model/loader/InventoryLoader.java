package model.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import model.Product;

/*
 * Loader for the inventory, getter and setters for each aspect can be found here
 * 
 */
public class InventoryLoader {
	
	Map<String, Product> inventory = new HashMap<>();
	
	Map<String,String> products = new HashMap<>();
	Map<String,String> categories = new HashMap<>();
	Map<String,String> departments = new HashMap<>();
	
	public Map<String, Product> getInventory(){
		return inventory;
	}

	public Map<String, String> getDepartments(){
		return departments;
	}
	
	public Map<String, String> getCategories(){
		return categories;
	}
	
	public Map<String, String> getProducts(){
		return products;
	}
	
	public InventoryLoader() {
		EnumSet.allOf(Inventory.class).forEach( fileName -> {
			
			try {
				String file = fileName.getValue();
				InputStream path = InventoryLoader.class.getResourceAsStream("/Inventory"+file);
				BufferedReader in = new BufferedReader(new InputStreamReader(path));
			    String str = in.readLine();
			    while ((str = in.readLine()) != null) {
			        System.out.println(str);
			        String[] ar=str.split(",");
			        switch (fileName) {
			        case CATEGORY:
			        	categories.put(ar[0], ar[1]);
			        	break;
			        case DEPARTMENT:
			        	departments.put(ar[0], ar[1]);
			        	break;
			        case PRODUCT:
			        	products.put(ar[0], ar[1]);
			        	break;
			        case INVENTORY:
			        	inven(ar, inventory);
			        	break;
			        }
			    }
			    	in.close();
			} catch (IOException e) {
			    System.out.println("File Read Error");
			}
		});	
	}
	
	private void inven(String[] inv, Map<String, Product> inventory ) {
		if (inventory.containsKey(products.get(inv[0]))) {
			if (!inventory.get(products.get(inv[0])).getDepartments().contains(departments.get(inv[1])))
				inventory.get(products.get(inv[0])).setDepartaments(departments.get(inv[1]));
        	if (!inventory.get(products.get(inv[0])).getCategories().contains(categories.get(inv[2])))
				inventory.get(products.get(inv[0])).setCategories(categories.get(inv[2]));
        } else {
        	Product prod =  new Product(products.get(inv[0]),departments.get(inv[1]),categories.get(inv[2]));
        	inventory.put(products.get(inv[0]),prod);
        }	
	}
}

