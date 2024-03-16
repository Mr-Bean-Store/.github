package com.gp16.MrBeanStoreCLI.commands.MBS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gp16.MrBeanStoreCLI.api.LoginHttpAPIHandler;
import com.gp16.MrBeanStoreCLI.commands.login.LoginCommands;
import com.gp16.MrBeanStoreCLI.formatter.OutputFormatter;
import com.gp16.MrBeanStoreCLI.models.posts.MBS.OrderObject;
import com.gp16.MrBeanStoreCLI.models.posts.MBS.OrderPost;
import com.gp16.MrBeanStoreCLI.models.response.MBS.MBSAddressResponse;
import com.gp16.MrBeanStoreCLI.models.response.MBS.MappedAddressResponse;
import com.gp16.MrBeanStoreCLI.models.response.MBS.ProductItem;
import com.gp16.MrBeanStoreCLI.services.MBS.AddressService;
import com.gp16.MrBeanStoreCLI.services.MBS.MBSService;
import com.gp16.MrBeanStoreCLI.services.login.LoginService;
import org.springframework.shell.command.annotation.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Command(group = "MR BEAN STORE MAIN COMMANDS")
public class MBSCommands {
    MBSService mbsService;
    AddressService addressService;
    OutputFormatter outputFormatter;
    Scanner scanner = new Scanner(System.in);

    public MBSCommands(MBSService mbsService, AddressService addressService, OutputFormatter outputFormatter) {
        this.mbsService = mbsService;
        this.addressService = addressService;
        this.outputFormatter = outputFormatter;
    }

    @Command(command = "products", description = "It displays the products in our catalog")
    public String products() {
        List<ProductItem> models = mbsService.getProducts();
//        models.stream().map(model -> model.model().model_id()).forEach(System.out::println);
        System.out.println("================================================================================");
        System.out.println("\t\t\t\t\t\t\tProduct Catalog");
        System.out.println("================================================================================");
        System.out.println("Use the `select` command to select the products of your choice and add them to your order");
        return outputFormatter.formatToTable(models);
    }

    @Command(command = "select", description = "select products from catalog to add them to order")
    public String select() throws JsonProcessingException {
        System.out.println("Enter the id of the product you want to select from the catalog and click enter.");
        System.out.println("Enter -1 to complete your selection");
        System.out.println();
        List<Integer> selected = new ArrayList<>();

        while (scanner.hasNext()) {
            int id = scanner.nextInt();

            if (id == -1) break;

            selected.add(id);
        }

        if (!selected.isEmpty()) {
            OrderPost orderPost = new OrderPost(selected);
//        mbsService.addToOrder(orderPost);

            System.out.println("You added the following to your order:");
            System.out.println(selected.toString());

            System.out.println("Please enter your delivery address below");
            System.out.print("Street: ");
            scanner.nextLine();
            String street = scanner.nextLine();
            // I don't know why its jumping suburb, but the next line fixes this problem

            System.out.print("Suburb: ");
            String suburb = scanner.nextLine();
            System.out.print("City: ");
            String city = scanner.nextLine();
            System.out.print("Country: ");
            String country = scanner.nextLine();

            String address = street + "%20" + suburb + "%20" + city + "%20" + country;
            System.out.println(address);

            //convert address
            MappedAddressResponse mappedAddress = addressService.mapAddress(address);
//        System.out.println(mappedAddress);
            MBSAddressResponse addedAddress = mbsService.addAddress(mappedAddress);
            // add the order
//            Long customer_id = new LoginCommands().customerResponse.customer_id();
//            OrderObject orderObject = new OrderObject(customer_id, addedAddress.addressId(), selected);

//            mbsService.addToOrder(orderObject);

        }

        return selected.isEmpty() ? "Cancelled" : "Successfully added to order.";
    }
}
