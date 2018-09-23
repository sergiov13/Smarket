package server.controller.action;

import server.controller.IServerExchange;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.Cart;
import model.Product;
import model.loader.InventoryLoader;
import static server.controller.action.ServerActionHelper.*;


/* AuthorizeAction is the controller for user authorization.
 * It is triggered from the login.html form.
 */
public class FilterAction implements IServerAction {

	private static final String catParam = "category";
	private static final String depParam = "department";
	private static final String nameParam = "product_name";
	

    @Override
    public void execute(IServerExchange exchange) throws IOException {
			Map<String, String> params = getWwwFormUrlencodedBody(exchange);
			InventoryLoader inventory = new InventoryLoader();
			String query = exchange.getRequestURI().getPath();
			
			//Cart Load
			ArrayList<String> cartRaw =  readCart();
		    
			Cart cart = new Cart();
		    cartRaw.forEach(prod -> {
		    	cart.addProduct(inventory.getInventory().get(prod));
		    });
	        
		    if (exchange.getRequestURI().getPath().contains("completeList")) {
	        	Map<String,String> token = createTokenTable(
	        			inventory.getInventory(), cart, "%list%");
	        	
	        	createPageAndSend(exchange,"/list.html",token, 200);
	        } else if (null != params) {  	
	        	Map<String,String> token = createTokenTable(
	        			filter(inventory.getInventory(),
	        					checkParam(inventory.getCategories().get(params.get(catParam))),
	        					checkParam(inventory.getDepartments().get(params.get(depParam))),
	        					checkParam(params.get(nameParam))), cart, "%list%");
	        	
	        	createPageAndSend(exchange,"/list.html",token, 200);

	        	exchange.close();
	        }

	 	}
    

    	

    private void doRedirect(IServerExchange exchange) throws IOException {
    	
    }
    
    
    /*
     * Filters from inventory using 1 or 2 or 3 parameters, if none is given will return empty Map
     * 
     */
    private Map<String,Product> filter(Map<String,Product> inventory,String cat, String dep, String name) { 
	    	Map<String,Product> aux = new HashMap<>();
    		ArrayList<Product> products = new ArrayList<>(inventory.values());
    		setHelper(new Product(name, dep, cat));
    		List<Product> filtered =  products.stream()
    	    .filter(p -> 
    	    	
    	    		    (
    	    		        	(	// IF Name is equals AND Category is empty or equals AND Department is empty or equals 
    	    		        		p.getName().equals(getProdFilter().getName()) 
    	    		       			&& 
    	    		       			( getProdFilter().getName().equals("") || p.getCategories().contains(getProdFilter().getCategories().get(0)) ) 
    	    		       			&& 
    	    		       			( getProdFilter().getName().equals("") || p.getDepartments().contains(getProdFilter().getDepartments().get(0)) )
    	    		       		) 
    	    		       		||	
    	    		       		(	//IF Name is empty AND either Category equals and department is empty OR Department equals and category is empty
    	    		       			getProdFilter().getName().equals("empty") 
    	    		       			&& 
    	    		       			(
	    	    		       			(p.getCategories().contains(getProdFilter().getCategories().get(0)) && getProdFilter().getDepartments().get(0).equals("") )
	    	    		       			|| 
	    	    		       			(p.getCategories().contains(getProdFilter().getDepartments().get(0)) && getProdFilter().getCategories().get(0).equals("") )
	    	    		       		)
    	    		       			
    	    		       		)
    	    		       		||
    	    		       		( 	//IF name is empty AND Categoy equals and department equals
	    		       				getProdFilter().getName().equals("empty")
	    		       				&& 
	    		       				(
		    		       				p.getCategories().contains(getProdFilter().getCategories().get(0)) 
		    		       				&& 
		    		       				p.getDepartments().contains(getProdFilter().getDepartments().get(0))  	 
	    		       				)
    	    		       		)
    	    		    )
    	    			
    	    ).collect(Collectors.toList());
    		
	    	Iterator itr = filtered.iterator();
    		while(itr.hasNext()) {
    				Product prod = (Product) itr.next();
    				aux.put(prod.getName(), prod);
    				
    		} 	    	
	    	return aux;
    }
    
    private void filterCart(Map<String,Product> inventory, ArrayList<Product> cart,String dep, String cat, String name) {
    	cart.forEach( cartProd->{
	    	inventory.forEach((k, product) ->{
	    		
	    		
	    	});
    	});
    	
    }
    private String checkParam(String param) {
    	if (param == null)
    		param = "";
    	return param;
    }
    
    private Product helperProd;    
    private void setHelper(Product p) {
    	this.helperProd = p;
    }
    
    private Product getProdFilter() {
    	return this.helperProd;
    }
}
