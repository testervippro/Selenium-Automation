package com.thoaikx;


import com.thoaikx.processsbuilder.ProcessManager;

import java.io.IOException;

public class App {

  public static void main(String[] args) throws IOException, InterruptedException {

    System.out.printf("hello word");


    ProcessManager.executeCommand("./mvnw clean test -Pweb-execution -Dsuite=local-suite -Dtarget=local-suite -Dheadless=true -Dbrowser=chrome");

  }


}
