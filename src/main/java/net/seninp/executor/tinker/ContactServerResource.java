package net.seninp.executor.tinker;

import org.restlet.resource.ServerResource;

/**
 * The server side implementation of the Restlet resource.
 */
public class ContactServerResource extends ServerResource implements ContactResource {

  private static volatile Contact contact = new Contact("Scott", "Tiger",
      new Address("10 bd Google", null, "20010", "Mountain View", "USA"), 40);

  public void remove() {
    contact = null;
  }

  public Contact retrieve() {
    System.out.println(contact);
    return contact;
  }

  public void store(Contact contact) {
    ContactServerResource.contact = contact;
  }
}
