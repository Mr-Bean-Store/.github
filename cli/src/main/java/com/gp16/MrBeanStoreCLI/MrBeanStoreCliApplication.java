package com.gp16.MrBeanStoreCLI;

import com.gp16.MrBeanStoreCLI.commands.MBS.MBSCommands;
import com.gp16.MrBeanStoreCLI.commands.login.LoginCommands;
import com.gp16.MrBeanStoreCLI.models.response.MBS.CustomerResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.EnableCommand;

@SpringBootApplication
@EnableCommand({LoginCommands.class, MBSCommands.class})
public class MrBeanStoreCliApplication {
	public CustomerResponse customerResponse;

	public static void main(String[] args) {
		System.out.println("================================================================================");
		System.out.println("\t\t\t\t\t\t\tWelcome to Mr Bean Store");
		System.out.println("================================================================================");

		System.out.println("You can use the `help` command to see all the command you can use.");
		System.out.println("Please use the `login` command to login with github and have access to our store.");

		SpringApplication.run(MrBeanStoreCliApplication.class, args);
	}
}
