package com.gp16.MrBeanStoreCLI;

import com.gp16.MrBeanStoreCLI.commands.MBS.MBSCommands;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan
public class MrBeanStoreCliApplication {

	public static void main(String[] args) {
		System.out.println("================================================================================");
		System.out.println("\t\t\tWelcome to Mr Bean Store");
		System.out.println("================================================================================");

		System.out.println("You can use the `help` command to see all the command you can use.");
		System.out.println("Please use the `login` command to login with github and have access to our store.");

		SpringApplication.run(MrBeanStoreCliApplication.class, args);
	}
}
