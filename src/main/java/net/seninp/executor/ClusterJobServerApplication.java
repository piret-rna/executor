package net.seninp.executor;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import net.seninp.executor.db.ExecutorDB;

public class ClusterJobServerApplication extends Application {

  @Override
  public Restlet createInboundRoot() {

    // init the DB connection
    ExecutorDB.connect("");
    
    Router router = new Router(getContext());

    router.attach("/json", ClusterJobServerResource.class);

    return router;
  }

}
