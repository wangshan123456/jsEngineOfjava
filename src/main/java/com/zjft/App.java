package com.zjft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Hello world!
 *
 */

@EnableEurekaClient
@SpringBootApplication
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
        System.out.println("test");
        System.out.println("\n" +
                "\n" +
                "               o8o  .o88o1.     .   \n" +
                "               `\"'  888 `\"   .o8   \n" +
                "  oooooooo    oooo o888oo  .o888oo \n" +
                " d'\"\"7d8P     `888  888      888   \n" +
                "   .d8P'       888  888      888   \n" +
                " .d8P'  .P     888  888      888 . \n" +
                "d8888888P      888 o888o     \"888\" \n" +
                "               888                 \n" +
                "           .o. 88P                 \n" +
                "           `Y888P                  \n" +
                "\n");
    }

}
