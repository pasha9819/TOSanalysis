package spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tosamara.classifiers.Updater;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Updater.update(false);
        SpringApplication.run(Application.class, args);
    }

}