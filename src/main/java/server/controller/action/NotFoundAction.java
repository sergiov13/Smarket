package server.controller.action;

import server.controller.IServerExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static server.controller.action.ServerActionHelper.createPageAndSend;

/* NotFoundAction is a IServerAction implementation for URI requests not mapped
 * in the router configuration.
 */
public class NotFoundAction implements IServerAction {

    @Override
    public void execute(IServerExchange exchange) throws IOException {
    	Map<String,String> error = new HashMap<>();
    	error.put("%status%","");
        createPageAndSend(exchange, "/error.html", error, 404);
    }

}
