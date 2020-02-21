package com.example.springcucumber.spring_cucumber;

import com.example.springcucumber.spring_cucumber.dao.UserDao;
import com.example.springcucumber.spring_cucumber.model.Role;
import com.example.springcucumber.spring_cucumber.model.User;
import com.example.springcucumber.spring_cucumber.service.UserService;
import cucumber.api.PendingException;
import cucumber.api.java.ru.Допустим;
import cucumber.api.java.ru.Если;
import cucumber.api.java.ru.То;
import cucumber.api.java.ru.Тогда;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = SpringCucumberApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MySteps {
    @Autowired
    private UserService userService;
    private WebDriver driver = RunAddUserTest.driver;

    @Допустим("^мы авторизовались под админом$")
    public void мы_авторизовались_под_админом() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try {
            driver.get("http://localhost:8080/logout");
            driver.get("http://localhost:8080/login");
            System.out.println("Open URL");

            driver.findElement(By.name("username")).sendKeys("admin");
            driver.findElement(By.name("password")).sendKeys("admin");
            driver.findElement(By.className("btn")).click();

            System.out.println("Successful connection with admin role!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PendingException();
        }
    }

    @Допустим("^зашли в меню добавления пользователя$")
    public void зашли_в_меню_добавления_пользователя() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try {
            System.out.println("In menu add user");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PendingException();
        }
    }

    @Если("^на сервере не существует такого же логина$")
    public void на_сервере_не_существует_такого_же_логина() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try{
            User user = userService.getUserByLogin("Огурчик");
            if (user.getLogin().equals("")) {
                System.out.println("Unique login");
            } else {
                userService.deleteUser(user.getId().toString());
            }
        } catch (Exception e) {
            throw new PendingException();
        }
    }

    @Тогда("^пользователь добавляется корретно$")
    public void пользователь_добавляется_корретно() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try {
            driver.findElement(By.id("navAddForm")).click();
            Thread.currentThread().sleep(200);
            driver.findElement(By.id("addFirstName")).sendKeys("Огурчик");
            driver.findElement(By.id("addLastName")).sendKeys("Пестик");
            driver.findElement(By.id("addPhoneNumber")).sendKeys("999");
            driver.findElement(By.id("addLogin")).sendKeys("Огурчик");
            driver.findElement(By.id("addPassword")).sendKeys("Пестик");
            driver.findElement(By.id("butAddUser")).submit();
            Thread.currentThread().sleep(200);
            driver.findElement(By.id("navLinkTable")).click();

            User user = userService.getUserByLogin("Огурчик");

            if (user.getLogin().equals("")) {
                System.out.println("Exception in add user with unique login");
                throw new Exception("Exception in add user with unique login");
            }

            System.out.println("Success added user");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PendingException();
        }
    }

    @Если("^на сервере существует такой же логин$")
    public void на_сервере_существует_такой_же_логин() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try{
            if (userService.getUserByLogin("Огурчик").getLogin().equals("Огурчик")) {
                System.out.println("Double login");
            } else {
                User user = new User(
                        "Огурчик",
                        "Перчик",
                        "Огурчик",
                        "Перчик",
                        999L,
                        new Role("admin")
                        );
                userService.addUser(user);
            }
        } catch (Exception e) {
            throw new PendingException();
        }
    }


    @Тогда("^пользователь не добавляется$")
    public void пользователь_не_добавляется() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try{
            driver.findElement(By.id("navAddForm")).click();
            Thread.currentThread().sleep(200);
            driver.findElement(By.id("addFirstName")).sendKeys("Огурчик");
            driver.findElement(By.id("addLastName")).sendKeys("Пестик");
            driver.findElement(By.id("addPhoneNumber")).sendKeys("999");
            driver.findElement(By.id("addLogin")).sendKeys("Огурчик");
            driver.findElement(By.id("addPassword")).sendKeys("Пестик");
            driver.findElement(By.id("butAddUser")).submit();
            Thread.currentThread().sleep(200);

            System.out.println("User didn't add");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PendingException();
        }
    }
}
