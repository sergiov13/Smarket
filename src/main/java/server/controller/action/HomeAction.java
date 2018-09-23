package server.controller.action;

import server.controller.IServerExchange;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import model.loader.InventoryLoader;

import static server.controller.action.ServerActionHelper.*;

/*
 *
 */
public class HomeAction implements IServerAction {

    @Override
    public void execute(IServerExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        
        String query = exchange.getRequestURI().getQuery();
       
        Map<String,Map<String,String>> token = new HashMap<>();
        InventoryLoader inventory = new InventoryLoader();
        token.put("%category%",inventory.getCategories());
        token.put("%department%",inventory.getDepartments());
        
        Map<String,String> tokenReplaceReady = createTokenDropdown(token);
       
        createPageAndSend(exchange, "/home.html", tokenReplaceReady,200);

        //            if (null == query || query.isEmpty())
//        else
//            replaceTokenAndSend(exchange, uri.getPath(), "%referer%", "?"+query,200);
    }

}
