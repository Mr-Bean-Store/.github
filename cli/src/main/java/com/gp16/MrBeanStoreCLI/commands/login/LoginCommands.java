package com.gp16.MrBeanStoreCLI.commands.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gp16.MrBeanStoreCLI.MrBeanStoreCliApplication;
import com.gp16.MrBeanStoreCLI.flows.MBS.MBSDriver;
import com.gp16.MrBeanStoreCLI.models.response.MBS.CustomerResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.AccessTokenResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.EmailResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.UserResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.VerificationResponse;
import com.gp16.MrBeanStoreCLI.services.login.GithubService;
import com.gp16.MrBeanStoreCLI.services.login.LoginService;
import org.springframework.shell.command.annotation.Command;

import java.util.Scanner;

@Command(group = "Login Commands")
public class LoginCommands {
    private final String client_id = "f3c5acce35108a97090a";
    private final String grant_type = "urn:ietf:params:oauth:grant-type:device_code";
    private String scope = "user";
    public CustomerResponse customerResponse;
    LoginService loginService;
    GithubService githubService;
    MBSDriver mbsDriver;


    Scanner scanner = new Scanner(System.in);

//    public LoginCommands() {}
    public  LoginCommands(LoginService loginService, GithubService githubService, MBSDriver mbsDriver) {
        this.loginService = loginService;
        this.githubService = githubService;
        this.mbsDriver = mbsDriver;
    }

    @Command(command = "login", description = "A login command for logging the user in")
    public void login() throws JsonProcessingException {
        System.out.println("\t\t\t\t\t\t\tLoading ...");

        VerificationResponse verificationCodes = loginService.requestVerificationCodes(client_id, scope);

        System.out.println("Please visit this link: " + verificationCodes.verification_uri());
        System.out.println("Then enter the following code: " + verificationCodes.user_code());
        System.out.println("\nNote: this code expires after 15 minutes!\n");

        boolean responseAvailable = false;
        while (!responseAvailable) {
            AccessTokenResponse accessTokenResponse = loginService.requestAccessToken(client_id, verificationCodes.device_code(), grant_type);

            if (accessTokenResponse.access_token() != null) {
                responseAvailable = true;
                UserResponse userResponse = githubService.getUser(accessTokenResponse.access_token());
                EmailResponse emailResponse = githubService.getEmail(accessTokenResponse.access_token());

                System.out.println("--------------------------------------------------------------------------------");
                System.out.println("Authenticated successfully, Welcome " + userResponse.login() + ".");
                System.out.println("--------------------------------------------------------------------------------");

                // register the user if they are new (not in the db)
                System.out.println("\n");
                System.out.println("--------------------------------------------------------------------------------");
                System.out.println("Please enter the following details");
                System.out.println("--------------------------------------------------------------------------------");
                System.out.print("firstname: ");

                String firstname = scanner.next();

                System.out.print("lastname: ");
                String lastname = scanner.next();

                customerResponse = mbsDriver.registerCustomer(firstname, lastname, emailResponse.email());
                System.out.println(customerResponse);

                System.out.println("Registered successfully.");

            } else if (accessTokenResponse.error().equals("authorization_pending")) {
                // when the authorization is still pending sleep for 5 sec and try again
                try {
                    Thread.sleep(verificationCodes.interval() * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (accessTokenResponse.error().equals("slow_down")) {
                // in case of making request not respecting the given interval, add more 5 seconds
                try {
                    Thread.sleep(verificationCodes.interval() * 1000 + 5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (accessTokenResponse.error().equals("expired_token")) {
                // the device code has expired, have to start afresh
                responseAvailable = true;
                System.out.println("The device code has expired. Please run tye `login` command again.");
            } else if (accessTokenResponse.error().equals("access_denied")) {
                // when the user cancelled
                responseAvailable = true;
                System.out.println("Login was cancelled!");
            }
        }
    }
}
