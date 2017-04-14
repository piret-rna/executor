package net.seninp.executor.util;

import java.io.Serializable;

/**
 * A simple class to use when conveying error messages back to the client...
 * 
 * @author psenin
 *
 */
public class ErrorMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  private String code;
  private String message;
  private String description;

  public ErrorMessage() {
    super();
    assert true;
  }

  public ErrorMessage(String code, String message, String description) {
    super();
    this.code = code;
    this.message = message;
    this.description = description;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
