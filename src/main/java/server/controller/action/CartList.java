package server.controller.action;

import server.controller.IServerExchange;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.controller.action.*;
import server.controller.ServerExchange;

import model.Cart;
import model.Product;
import model.loader.InventoryLoader;

import static server.controller.action.ServerActionHelper.*;

/* UserApiAction is the REST API controller. An authenticated admin user can create,
 * modify anddelete users from the user store.
 */
public class CartList implements IServerAction {


    @Override
    public void execute(IServerExchange exchange) throws IOException {

    	Map<String, String> params;
  
    	if (exchange.getRequestMethod().equals("GET")) {
    	   	params = getQueryParams(exchange);
        } else {
       		params = getQueryParams(exchange);
        }
       
		switch (getAction(params)) {
            case "cartList":
                cartList(exchange);
                break;
            case "addProduct":
                addProduct(exchange);
                break;
            case "removeProduct":
            	removeProduct(exchange);
            	break;
            default:
                exchange.setStatus(405,-1);
                exchange.close();
        }
    }



	private void cartList(IServerExchange exchange) throws IOException {
		Map<String, String> params = getWwwFormUrlencodedBody(exchange);
		InventoryLoader inventory = new InventoryLoader();
		//Cart Load
		ArrayList<String> cartRaw =  readCart();
	    Cart cart = new Cart();
	    cartRaw.forEach(prod -> {
	    	cart.addProduct(inventory.getInventory().get(prod));
	    });
	    
        if (null != params) { //Validation  	
        	Map<String,String> token = createTokenTable(createCartListInventory(inventory.getInventory(), cart), cart, "%list%");
        	
        	createPageAndSend(exchange,"/list.html",token, 200);

        	exchange.close();
        }
	}



	private void removeProduct(IServerExchange exchange) throws IOException {
		Map<String, String> params = getQueryParams(exchange);
		params.forEach((k,v) -> {
			try {
				removeFromCart(v);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		cartList(exchange);
	}

	private String getAction(Map<String,String> params) {
		String act = "";
		if (params == null)
			act = "cartList";
		else
			for(String key : params.keySet()) {
				act = key.replaceAll("\\d","");
				break;
			}
		return act;
	}

	private void addProduct(IServerExchange exchange) throws IOException {
        Map<String, String> params = getQueryParams(exchange);
        params.forEach((k,v) -> {
        	try {
				add2Cart(v);
			} catch (IOException e) {}
        });
    	cartList(exchange);
    }
    
	private Map<String,Product> createCartListInventory(Map<String, Product> inventory, Cart cart) {
		Map<String, Product> cartListInven = new HashMap<>();
		inventory.forEach((k,v) ->{
			cart.getCart().forEach( name -> {
				if (k.equals(name.getName()))
					cartListInven.put(k, v);
			});
		});
		return cartListInven;
	} 
	
	private String getDataFromRequest(IServerExchange exchange) {
		Pattern pattern = Pattern.compile("/product");
        Matcher matcher = pattern.matcher(exchange.getRequestURI().getPath());
        if (matcher.find())
        	return matcher.group(1);
        
        return "";
	}
	
	public Map<String,String> clean(Map<String,String> params){
		List val = new ArrayList<String>(params.values());
		params.forEach((k,v) -> {
			
		});
		return params;
	}
}
    


