package net.seninp.executor.tinker;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface ContactResource {

  @Get("json")
  public Contact retrieve();

  @Put
  public void store(Contact contact);

  @Delete
  public void remove();
}
