package org.example;

import org.example.model.SomeViewModel;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class View extends VBox {
    private GridPane gp = new GridPane();

    private FileChooser fileChooser = createFileChooser();
    private Button fileSelectButton = new Button("Selecteer..");
    private DatePicker datePicker = new DatePicker();

    private Button printButton = new Button("Print");

    public View(final Stage stage) {
        createView(stage);
        bindViewModel();
    }

    private final SomeViewModel viewModel = new SomeViewModel();

    private void bindViewModel() {
        // fileSelectButton.onCLick

        // Bindings.bindBidirectional(
        //         tfAnnualSalary.textProperty(),
        //         viewModel.annualSalaryProperty(),
        //         new NumberStringConverter());
    }

    private void createView(final Stage stage) {
        VBox gpwrap = new VBox();
        gpwrap.setAlignment(Pos.CENTER);

        datePicker.setDisable(true);

        gp.setPadding(new Insets(40));
        gp.setVgap(4);
        gp.add(fileSelectButton, 0, 0);
        gp.add(new Label("Datum:"), 0, 1);
        gp.add(datePicker, 1, 1);

        final ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);

        gp.getColumnConstraints().addAll(col, col);

        gpwrap.getChildren().add(gp);

        VBox.setVgrow(gpwrap, Priority.ALWAYS);

        printButton.setOnAction(this::save);
        fileSelectButton.setOnAction(event -> this.chooseFile(event, stage));

        printButton.setDefaultButton(true);

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding(new Insets(20.0d));
        ButtonBar.setButtonData(printButton, ButtonBar.ButtonData.OK_DONE);

        buttonBar.getButtons().addAll(printButton);

        this.getChildren().addAll(
                gpwrap,
                new Separator(),
                buttonBar);
    }

    private void save(ActionEvent evt) {
        viewModel.save();
    }

    private void chooseFile(ActionEvent event, final Stage stage) {
        fileChooser.showOpenDialog(stage);
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        ExtensionFilter extensionFilter = new ExtensionFilter("Excel file", "*.xls", "*.xlsx");
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser;
    }

}
