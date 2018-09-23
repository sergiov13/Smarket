package model;

import java.util.ArrayList;

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
