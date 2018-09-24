package server.controller.action;

import server.controller.IServerExchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Cart;
import model.Product;
import model.loader.InventoryLoader;

import static server.controller.action.ServerActionHelper.*;

/* CartListAction is the controller containing the logic for adding and removing from the cart 
 * as well as filtering the products that have been added to the cart.
 * 
 */
public class CartListAction implements IServerAction {

    @Override
    public void execute(IServerExchange exchange) throws IOException {

    	Map<String, String> params = getWwwFormUrlencodedBody(exchange);
       
		switch (getAction(params)) {
            case "cartList":
                cartList(exchange, params);
                break;
            case "addProduct":
                addProduct(exchange, params);
                break;
            case "removeProduct":
            	removeProduct(exchange, params);
            	break;
            default:
                exchange.setStatus(405,-1);
                exchange.close();
        }
    }



	private void cartList(IServerExchange exchange, Map <String,String> params) throws IOException {
		
		InventoryLoader inventory = new InventoryLoader();
		//Cart Load
		ArrayList<String> cartRaw =  readCart();
	    Cart cart = new Cart();
	    cartRaw.forEach(prod -> {
	    	cart.addProduct(inventory.getInventory().get(prod));
	    });
	    //Validation
    	Map<String,String> token = createTokenTable(createCartListInventory(inventory.getInventory(), cart), cart, "%CartList%");
    	
    	createPageAndSend(exchange,"/cartList.html",token, 200);

    	exchange.close();
    
	}



	private void removeProduct(IServerExchange exchange, Map<String,String> params) throws IOException {
		params.forEach((k,v) -> {
			try {
				removeFromCart(v);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		cartList(exchange, params);
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

	private void addProduct(IServerExchange exchange, Map<String, String> params) throws IOException {
        params.forEach((k,v) -> {
        	try {
				add2Cart(v);
			} catch (IOException e) {}
        });
    	cartList(exchange, params);
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
}
    


