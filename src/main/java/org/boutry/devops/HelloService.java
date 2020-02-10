package org.boutry.devops;


import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloService {
    public String greeting(String name) {
        return "hello " + name;
    }
}
