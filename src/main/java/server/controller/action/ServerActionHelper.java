package server.controller.action;

import com.sun.net.httpserver.Headers;

import model.Cart;
import model.Product;
import model.loader.InventoryLoader;
import server.controller.IServerExchange;

import static server.controller.action.ServerActionHelper.getQueryParams;
import static server.controller.action.ServerActionHelper.workToken;

import java.io.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.controller.action.ServerActionHelper;

/* ServerActionHelper
 *
 */
class ServerActionHelper {

    static void createPageAndSend(IServerExchange exchange, String resource, Map<String,String> tokenReplaceReady, int status) throws IOException {
        String s = getStringFromInputStream(ServerActionHelper.class.getResourceAsStream(resource));
        
        String response = workToken(tokenReplaceReady, s);
       
        if (null != response) {
            Headers h = (Headers) exchange.getResponseHeaders();
            h.set("Content-Type", "text/html");
            exchange.setStatus(status, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            exchange.close();
        }
    }

    // Helper method to copy static files to the server output stream.
    @SuppressWarnings("restriction")
	static int sendFile(IServerExchange exchange, String resource, int status) throws IOException {
        int count = 0;

        InputStream is = ServerActionHelper.class.getResourceAsStream(resource);
        if (null != is) {
            Headers h = (Headers) exchange.getResponseHeaders();
            h.set("Content-Type", "text/html");
            exchange.setStatus(status, 0);
            count = stream(is, exchange.getResponseBody());
            exchange.close();
        }

        return count;
    }
    
    static String workToken(Map<String,String> data, String s) {
    	String aux;
    	Iterator<Map.Entry<String, String>> iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
    		Map.Entry<String, String> entry = iterator.next();
    	    aux = s.replace(entry.getKey(),entry.getValue());
    	    s = aux;
    	}
        return s;
    }
    

    static boolean isWwwFormUrlencoded(IServerExchange exchange) {
        List<String> contentType = exchange.getRequestHeaders().get("Content-type");

        return null != contentType && contentType.contains("application/x-www-form-urlencoded");

    }

    static Map<String, String> getWwwFormUrlencodedBody(IServerExchange exchange) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        String body = getStringFromInputStream(exchange.getRequestBody());

        for (String parameter : body.split("&")) {
            String[] keyValue = parameter.split("=");
            if (keyValue.length != 1)
            	map.put(keyValue[0], keyValue[1]);
            else if (keyValue[0].isEmpty())
            	map.put(keyValue[0],"empty");
            else
            	map.put(keyValue[0],"empty");
        }
        return map;
    }
    
    static Map<String,String> createTokenDropdown( Map<String,Map<String,String>> tokenReplace){
    	Map<String,String> token = new HashMap<>();
    	
    	tokenReplace.forEach((k,v)->{
    		String aux = k.replaceAll("%", "");
    		token.put(k, createDropdown(v,aux));
    	});
    	
    	return token;
    }
    
    static void sendErrorResponse(int status, String message, IServerExchange exchange) throws IOException {
        exchange.setStatus(status, message.length());
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes("UTF-8"));
    }
    
    static void sendResponse(int status, String message, IServerExchange exchange) throws IOException {
        exchange.setStatus(status, message.length());
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes("UTF-8"));
    }

    static Map<String, String> getQueryParams(IServerExchange exchange) {
        HashMap<String, String> map = new HashMap<>();

        String query = exchange.getRequestURI().getQuery();
        
        if (null == query)
            return null;
        int i= 0;
        for (String parameter : query.split("&")) {
            String[] keyValue = parameter.split("=");

            if (keyValue.length != 2)
                return null;
            if(keyValue[0].contains("-")){
            	String [] aux = keyValue[0].split("-");
            	map.put(aux[0]+i, keyValue[1]);
            } else {
            	map.put(keyValue[0], keyValue[1]);
            }
            i++;
        }

        return map;
    }

    private static int stream(InputStream is, OutputStream os) throws IOException {
        final byte[] buffer = new byte[4096];
        int count;
        int total = 0;
        while ((count = is.read(buffer)) >= 0) {
            os.write(buffer,0,count);
            total += count;
        }
        return total;
    }

    private static String getStringFromInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null ) {
            sb.append(line);
        }
        return sb.toString();
    }
    
    private static String createDropdown(Map<String, String> map, String name) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("<select name=\"" + name + "\">");
    	sb.append("<option value = \"\" selected hidden></option> ");
    	map.forEach((k,v)->{
    		sb.append("<option value=\"" +k+ "\">"+v+ "</option>");	
    	});
    	sb.append("</select>");    	
    	return sb.toString();
    }
    
    static Map<String,String> createTokenTable(Map<String,Product> inventory,Cart cart, String name) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("<form action=\"/manageCart\" method=\"GET\">");
    	sb.append("<table style=\"width:100%\" >"); // add name
    	sb.append("<tr>");
    	sb.append("<th>Product</th>");
    	sb.append("<th>Departments</th>");
    	sb.append("<th>Categories</th>");
    	sb.append("<th>Add to cart</th>");
    	sb.append("<th>Remove from cart</th>");
    	sb.append("</tr>");
    	inventory.forEach((k,v)->{ 
	    	sb.append("<tr>");
	    	sb.append("<td  align=\"center\">" +v.getName() + "</td>");
	    	sb.append("<td  align=\"center\">");
	    	v.getDepartments().forEach(dep->{
	    		sb.append(dep + " - ");
	    	});
	    	sb.append("</td>");	
	    	sb.append("<td align=\"center\">");
	    	v.getCategories().forEach( dep-> {
	    		sb.append(dep + " - ");
	    	});	
	    	sb.append("</td>");
	    	
	    	if (!(cart == null)) {
		    	if(!cart.getCart().contains(v) && !name.equals("%cartList%")) {
		        	sb.append("<td align=\"center\">"+"<input id=\"addProduct\" type=\"checkbox\" name=\"addProduct-"+v.getName()+"\" value= " + v.getName() +" />"+"</td>");
		    	} else	{
		    		sb.append("<td></td>");
		        	sb.append("<td align=\"center\">"+"<input id=\"removeProduct\" type=\"checkbox\" name=\"removeProduct-"+v.getName()+"\" value="+v.getName()+" />"+"</td>");
		    	}
	    		sb.append("</tr>");
	    	} else {
		        	sb.append("<td align=\"center\">"+"<input id=\"addProduct\" type=\"checkbox\" name=\"addProduct-"+v.getName()+"\" value= " + v.getName() +" />"+"</td>");
		    		sb.append("</tr>");
	    	}
    	});
    	sb.append("</table>");
    	
    	sb.append("<div class=\"button\">");
    	sb.append("<button type=\"submit\">Buscar</button>");
    	sb.append("</div>");
    	sb.append("</form>");
    	Map<String,String> tokenReady = new HashMap<>();
    	tokenReady.put(name, sb.toString());
    	
    	return tokenReady;
    }
   
    private static String tDir = System.getProperty("java.io.tmpdir");
    
    private static File file;
    
    private static void setFile(File file2) {
    	file = file2;
    }
    
    static void resetCart() throws IOException {
    	final File folder = new File(tDir+"\\");
    	final File[] files = folder.listFiles( new FilenameFilter() {
			    public boolean accept( final File dir,
			                           final String name ) {
			        return name.matches( "cartPersistence.*\\.tmp" );
			    }
			} );
			for ( final File file : files ) {
			    if ( !file.delete() ) {
			        System.err.println( "Can't remove " + file.getAbsolutePath() );
			    }
			}
    }
    
    static void add2Cart(String prod) throws IOException {
    	if (file == null) {	
    		resetCart();
    		File tmpFile = File.createTempFile("cartPersistence", ".tmp");
    		FileWriter writer = new FileWriter(tmpFile);
    		writer.write(prod+"\n");
    		writer.close();
    		setFile(tmpFile);
    	} else {
    		FileWriter writer = new FileWriter(file, true);
    		writer.append(prod+"\n");
    		writer.close();
    	}
        System.out.println("Temp file : " +tDir); 

    }
    
    public static ArrayList<String> readCart() throws IOException {
    	ArrayList<String> aux = new ArrayList<>();
    	if (!(file == null)) {
	    	BufferedReader reader = new BufferedReader(new FileReader(file));
	   	 	String line;
	   	 	while ((line = reader.readLine()) != null) {
	   	 			aux.add(line);
	   	 	}
	   	 	reader.close();
    	}
    	return aux;
    }
    
    static void removeFromCart(String name) throws IOException {
    	ArrayList<String> aux = readCart();
    	aux.remove(name);
    	resetCart();
    	aux.forEach(k -> {
    		try {
    			
				add2Cart(k);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	});
    }
}
