package net.seninp.executor.tinker;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ContactServerApplication extends Application {

  @Override
  public Restlet createInboundRoot() {

    Router router = new Router(getContext());

    // router.attachDefault(new Directory(getContext(), "war:///"));
    router.attach("/contacts/123", ContactServerResource.class);

    return router;
  }

}
