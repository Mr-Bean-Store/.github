package com.gp16.MrBeanStoreCLI.formatter;

import com.gp16.MrBeanStoreCLI.models.response.MBS.ProductItem;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OutputFormatter {

    public String formatToTable(List<ProductItem> productList) {
        List<Object> data = productList
                .stream()
                .map(OutputFormatter::toRow)
                .collect(Collectors.toList());
        data.addFirst(addRow("model_id\t\t\t\t\t\t\t\t\t\t", "description\t\t\t\t\t\t\t\t\t\t"));

        ArrayTableModel model = new ArrayTableModel(data.toArray(Object[][]::new));
        TableBuilder table = new TableBuilder(model);
        table.addHeaderAndVerticalsBorders(BorderStyle.fancy_light);
        return table.build().render(100);
    }
    private static String[] toRow(ProductItem productItem) {
        return addRow(String.valueOf(productItem.model().model_id()), productItem.model().description());
    }
    private static String[] addRow(String model_id, String description) {
        return new String[] {model_id, description};
    }
}