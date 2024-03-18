package com.gp16.MrBeanStoreCLI.commands.MBS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gp16.MrBeanStoreCLI.flows.login.Login;
import com.gp16.MrBeanStoreCLI.formatter.OrdersOutputFormatter;
import com.gp16.MrBeanStoreCLI.formatter.OutputFormatter;
import com.gp16.MrBeanStoreCLI.models.posts.MBS.OrderObject;
import com.gp16.MrBeanStoreCLI.models.response.MBS.*;
import com.gp16.MrBeanStoreCLI.services.MBS.AddressService;
import com.gp16.MrBeanStoreCLI.services.MBS.MBSService;
import com.gp16.MrBeanStoreCLI.services.login.GithubService;
import com.gp16.MrBeanStoreCLI.services.login.LoginService;
import com.gp16.MrBeanStoreCLI.validations.Validations;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@ShellComponent
public class MBSCommands {
    private final String client_id = "f3c5acce35108a97090a";
    private final String grant_type = "urn:ietf:params:oauth:grant-type:device_code";
    private final String scope = "read:user,user:email";
    LoginService loginService;
    GithubService githubService;
    MBSService mbsService;
    AddressService addressService;

    CustomerResponse customer;

    Login login = new Login();
    OutputFormatter outputFormatter = new OutputFormatter();
    Validations validations = new Validations();
    OrdersOutputFormatter ordersOutputFormatter = new OrdersOutputFormatter();
    Scanner scanner = new Scanner(System.in);

    public MBSCommands(LoginService loginService, GithubService githubService, MBSService mbsService, AddressService addressService) {
        this.loginService = loginService;
        this.githubService = githubService;
        this.mbsService = mbsService;
        this.addressService = addressService;
    }

    @ShellMethod(key = "login", value = "It logs the user in using github oauth.")
    public void login() throws JsonProcessingException {
        System.out.println("Loading...");

        customer = login.initialize(loginService, githubService, mbsService, client_id, scope, grant_type);
    }

    @ShellMethod(key = "products", value = "It displays the products in our catalog")
    public String products() {
        List<ProductItem> models = mbsService.getProducts();

        System.out.println("================================================================================");
        System.out.println("\t\t\tProduct Catalog");
        System.out.println("================================================================================");
        System.out.println("Use the `select` command to select the products of your choice and add them to your order");

        return outputFormatter.formatToTable(models);
    }

    @ShellMethod(key = "select", value = "select products from catalog to add them to order")
    public String select() throws JsonProcessingException {
        System.out.println("Enter the id of the product you want to select from the catalog and click enter.");
        System.out.println("Enter -1 to complete your selection\n");

        List<Integer> modelIds = mbsService.getProducts().stream().map(productItem -> productItem.model().model_id()).toList();
        List<Integer> selected = new ArrayList<>();

        while (scanner.hasNext()) {
            int id = scanner.nextInt();

            if (id == -1) break;

            if (validations.isValidModelId(id, modelIds)) {
                selected.add(id);
            } else {
                System.out.println("Please enter a valid model id. run the `products` command to see the available models");
            }


        }

        if (!selected.isEmpty()) {

            System.out.println("You added the following to your order:");
            System.out.println(selected.toString());

            System.out.println("Please enter your delivery address below");
            // I don't know why its jumping suburb, nextLine below fixes this problem
            scanner.nextLine();

            System.out.print("Suburb: ");
            String suburb = scanner.nextLine();
            System.out.print("City: ");
            String city = scanner.nextLine();
            System.out.print("Country: ");
            String country = scanner.nextLine();

            String address = suburb + "%20" + city + "%20" + country;

            // convert address to lat & long
            MappedAddressResponse mappedAddress = addressService.mapAddress(address);

            MBSAddressResponse addedAddress = mbsService.addAddress(mappedAddress);

            // add the order
            OrderObject orderObject = new OrderObject(customer.customer_id(), addedAddress.addressId(), selected);

            AddedOrderResponse addedOrderResponse = mbsService.addToOrder(orderObject);
        }

        return selected.isEmpty() ? "Cancelled" : "Successfully added to order.";
    }

    @ShellMethod(key = "view orders", value = "It displays all orders of the customer")
    public String customerOrders() throws JsonProcessingException {
        List<HashMap<String, List<ProductItem>>> response = mbsService.customerOrders(customer.customer_id());

        Stream<String> orderIds = response.stream().flatMap(order -> order.keySet().stream());

        Stream<HashMap<String, Object>> orders = orderIds.map(orderId -> {
            List<String> modelDescriptions = response.stream()
                    .filter(order -> order.keySet().stream().findFirst().get().equals(orderId))
                    .findFirst()
                    .get()
                    .get(orderId)
                    .stream().map(productItem -> productItem.model().description())
                    .toList();

            AddedOrderResponse addedOrderResponse = mbsService.getOrder(Long.parseLong(orderId));
            String address = addressService.reverseMapAddress(addedOrderResponse.delivery().longitude(), addedOrderResponse.delivery().latitude());
            PriceResponse priceResponse = mbsService.orderPrice(Long.parseLong(orderId));

            HashMap<String, Object> order = new HashMap<>();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            order.put("orderId", orderId);
            order.put("modelDescriptions", modelDescriptions);
            order.put("address", address);
            order.put("price", "R" + priceResponse.price());
            order.put("orderDate", dateFormat.format(new Date(addedOrderResponse.orderDate().getTime())));
            order.put("expectedArrivalDate", dateFormat.format(new Date(addedOrderResponse.arrivalDate().getTime())));

            return order;
        });

        return ordersOutputFormatter.formatToTable(orders);
    }

    @ShellMethodAvailability({"products", "select", "view orders"})
    public Availability availabilityCheck() {
        return customer != null
                ? Availability.available()
                : Availability.unavailable("You have login by running the `login` command to access the commands");
    }
}