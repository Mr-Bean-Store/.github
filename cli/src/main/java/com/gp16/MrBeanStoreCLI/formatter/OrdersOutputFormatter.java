package com.gp16.MrBeanStoreCLI.formatter;

import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class OrdersOutputFormatter {

    public String formatToTable(Stream<HashMap<String, Object>> orders) {
        List<Object> data = orders
                .map(OrdersOutputFormatter::toRow)
                .collect(Collectors.toList());
        data.addFirst(addRow(
                "order_id\t\t\t\t\t\t\t\t\t\t",
                "model_description\t\t\t\t\t\t\t\t\t\t",
                "price\t\t\t\t\t\t\t\t\t\t",
                "address\t\t\t\t\t",
                "orderDate\t\t\t\t\t",
                "expectedArrivalDate\t\t\t\t\t"
        ));

        ArrayTableModel model = new ArrayTableModel(data.toArray(Object[][]::new));
        TableBuilder table = new TableBuilder(model);
        table.addHeaderAndVerticalsBorders(BorderStyle.fancy_light);
        table.addInnerBorder(BorderStyle.fancy_light);
        return table.build().render(100);
    }
    private static String[] toRow(HashMap<String, Object> order) {
        return addRow(
                String.valueOf(order.get("orderId")),
                String.valueOf(order.get("modelDescriptions")),
                String.valueOf(order.get("price")),
                String.valueOf(order.get("address")),
                String.valueOf(order.get("orderDate")),
                String.valueOf(order.get("expectedArrivalDate"))
        );
    }

    private static String[] addRow(String order_id, String model_description, String price, String address, String order_date, String expected_arrival_date) {
        return new String[] {order_id, model_description, price, address, order_date, expected_arrival_date};
    }
}
