package model;

import java.util.ArrayList;

/*
 * Cart is object model for a Cart which will contain Object Products
 * 
 */
public class Cart {

	private ArrayList<Product> cart;

	public Cart() {
		cart = new ArrayList<>();
	}
	public Cart(ArrayList<Product> currentCart) {
		cart = currentCart;
	}
   
	public ArrayList<Product> getCart() {
		return cart;
	}
	
	public void addProduct(Product prod) {
		cart.add(prod);
	}
}
