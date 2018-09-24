package model;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Product is the bjejct model for Products
 * Product can have multiple categories and departments
 */
public class Product {
	
	private String name;
	private ArrayList<String> categories;
	private ArrayList<String> departments;
	
	public Product(String nam, String department,String category) {
		name = nam;
		departments =  new ArrayList<>(Arrays.asList(department));
		categories =  new ArrayList<>(Arrays.asList(category));
	}
	
	public String getName() {
		return name;
	}
	public void setName(String nam) {
		name = nam;
	}
	public ArrayList<String> getCategories() {
		return categories;
	}
	public void setCategories(String category) {
		categories.add(category);
	}
	public ArrayList<String> getDepartments() {
		return departments;
	}
	public void setDepartaments(String department) {
		departments.add(department);
	}
	
	
	
}
