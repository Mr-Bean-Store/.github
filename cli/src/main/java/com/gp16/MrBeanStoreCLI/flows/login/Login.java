package com.gp16.MrBeanStoreCLI.flows.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gp16.MrBeanStoreCLI.models.response.MBS.CustomerResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.AccessTokenResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.EmailResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.UserResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.VerificationResponse;
import com.gp16.MrBeanStoreCLI.services.MBS.MBSService;
import com.gp16.MrBeanStoreCLI.services.login.GithubService;
import com.gp16.MrBeanStoreCLI.services.login.LoginService;

import java.util.Scanner;

public class Login {
    public CustomerResponse initialize(LoginService loginService, GithubService githubService, MBSService mbsService, String client_id, String scope, String grant_type) throws JsonProcessingException {
        CustomerResponse customer = null;
        Scanner scanner = new Scanner(System.in);

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

                System.out.println("\n");

                customer = mbsService.isCustomerRegistered(emailResponse.email());

                // Register user if not in db
                if (customer.customer_id() == 0 && customer.firstName().isEmpty() && customer.lastName().isEmpty() && customer.email().isEmpty()) {
                    System.out.println("--------------------------------------------------------------------------------");
                    System.out.println("Please enter the following details");
                    System.out.println("--------------------------------------------------------------------------------");
                    System.out.print("firstname: ");

                    String firstname = scanner.next();

                    System.out.print("lastname: ");
                    String lastname = scanner.next();

                    customer = mbsService.registerCustomer(firstname, lastname, emailResponse.email());
                }


                System.out.println("\nLogin was successful.");

                return customer;

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

        return customer;
    }
}
