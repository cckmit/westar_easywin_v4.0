package com.westar.base.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {
  
  private String userName;
  private String password;


  public void setAcount(String username, String password) {
    this.userName = username;
    this.password = password;
  }
  
  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(userName, password);
  }
}