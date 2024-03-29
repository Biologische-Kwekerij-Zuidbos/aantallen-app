/* (C)2024 */
package org.example;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.factory.DocumentFactory;
import org.example.state.ISubscriber;
import org.example.state.PrintInfoModel;

public class View extends VBox {

    private final PrintInfoModel printInfoModel = new PrintInfoModel();

    private final DateReader dateReader = new DateReader();

    private GridPane gp = new GridPane();

    private FileChooser fileChooser = createFileChooser();
    private Button fileSelectButton = new Button("Selecteer..");
    private ChoiceBox<LocalDate> datePicker = new ChoiceBox<>();

    private Button printButton = new Button("Print");

    public View(final Stage stage) {
        createView(stage);
        registerEvents();
        setDateFormatting();



    }

    private void setDateFormatting() {
        datePicker.setConverter(
                new StringConverter<LocalDate>() {
                    @Override
                    public String toString(LocalDate localDate) {
                        if (localDate == null) {
                            return "";
                        }

                        return localDate.format(DateFormatting.DUTCH_DATE_TIME_FORMATTER);
                    }

                    @Override
                    public LocalDate fromString(String s) {
                        return (s == null || s.isEmpty()) ? null : LocalDate.parse(s);
                    }
                });
    }

    private void registerEvents() {
        ISubscriber<PrintInfoModel> subscriber =
                new ISubscriber<PrintInfoModel>() {

                    @Override
                    public void update(PrintInfoModel newState) {
                        boolean fileExists = newState.getFile() != null;
                        boolean dateExists = newState.getDate() != null;
                        boolean isPrintButtonEnabled = fileExists && dateExists;
                        datePicker.setDisable(!fileExists);
                        printButton.setDisable(!isPrintButtonEnabled);
                    }
                };

        printInfoModel.getEvents().subscribe(subscriber);
    }

    private void createView(final Stage stage) {
        VBox gpwrap = new VBox();
        gpwrap.setAlignment(Pos.CENTER);

        datePicker.setDisable(true);
        printButton.setDisable(true);

        datePicker.setPrefSize(Double.MAX_VALUE, 1);

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

        printButton.setOnAction(this::openInExcel);
        fileSelectButton.setOnAction(event -> this.chooseFile(event, stage));
        datePicker.setOnAction(this::datePicked);

        printButton.setDefaultButton(true);

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding(new Insets(20.0d));
        ButtonBar.setButtonData(printButton, ButtonBar.ButtonData.OK_DONE);

        buttonBar.getButtons().addAll(printButton);

        this.getChildren().addAll(gpwrap, new Separator(), buttonBar);
    }

    private void openInExcel(ActionEvent evt) {
        try {
            DocumentFactory factory = new DocumentFactory(printInfoModel.getFile());
            File file = factory.createFromDate(printInfoModel.getDate());
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void datePicked(ActionEvent evt) {
        LocalDate value = datePicker.getValue();
        printInfoModel.setDate(value);
    }

    private void chooseFile(ActionEvent event, final Stage stage) {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            printInfoModel.setFile(file);
            updateDateOptions(file);
        }
    }

    private void updateDateOptions(File file) {
        Set<LocalDate> dates = readDates(file);
        datePicker.getItems().setAll(dates);
    }

    private Set<LocalDate> readDates(File file) {
        try {
            return dateReader.readDates(file);
        } catch (IOException e) {
            return Collections.emptySet();
        }
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        ExtensionFilter extensionFilter = new ExtensionFilter("Excel file", "*.xls", "*.xlsx");
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser;
    }
}
