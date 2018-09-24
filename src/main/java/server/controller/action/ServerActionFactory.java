package server.controller.action;

import server.controller.action.FilterAction;
import server.controller.action.IServerAction;
import server.controller.action.HomeAction;
import server.controller.action.NotFoundAction;
import server.application.IServerRouter;
import server.controller.IServerExchange;


/* ServerActionFactory implements Factory Method pattern.
 * It provides a creational method which returns an specific action implementation
 * for the needed job, depending on our router configuration.
 */
public class ServerActionFactory {

    // Router configuration
    private IServerRouter router;
    
    // Actions pool
    private final IServerAction homeAction;
    private final IServerAction filterAction;
    private final IServerAction cartList;
    private final IServerAction notFoundAction;

    // Router configuration is injected as a dependency
    public ServerActionFactory(IServerRouter router) {
        this.router = router;
        
         //Create actions
        this.homeAction = new HomeAction();
        this.filterAction = new FilterAction();
        this.cartList = new CartListAction();
        this.notFoundAction = new NotFoundAction();
    }

    
    public IServerAction getAction(IServerExchange exchange) {
    	String action = exchange.getRequestURI().getPath();
    	IServerAction act;
    	switch (action) {
        case "/home" :
            act = new HomeAction();
            break;
        case "/list":
            act = new FilterAction();
            break;
        case "/cartList":
        	act = new CartListAction();
        	break;
        case "/":
        	act = new HomeAction();
        	break;
        case "/manageCart":
        	act = new CartListAction();
        	break;
        case "/completeList":
        	act = new FilterAction();
        	break;
        default:
            act = new NotFoundAction();
            break;
    }   
    	return act;
  }             
}
