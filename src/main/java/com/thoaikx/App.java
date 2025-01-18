package com.thoaikx;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;

public class App {

  public static void main(String[] args) {
    DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://127.0.0.1:2375").build();
    System.out.println(dockerClient.versionCmd().exec());

  }

}
