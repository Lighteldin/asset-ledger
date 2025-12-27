package me.jehn;

import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.Collections;
import java.util.List;

import static me.jehn.SQL.*;

public class PrimaryController {
    //START OF DECLARATION//

    @FXML
    private AnchorPane mainPane;
    @FXML
    private GridPane gridPaneAdd;
    @FXML
    private GridPane gridPaneEdit;
    //
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField assetTextField;
    private String nameInput;
    @FXML
    private TextField liabilityTextField;
    @FXML
    private Label errorTextForAdd;
    @FXML
    private Label errorTextForEdit;
    private int assetInput;
    private int liabilityInput;
    //
    @FXML
    private TextField editAssetTextField;
    @FXML
    private TextField editLiabilityTextField;
    @FXML
    private TextField editNameTextField;
    //
    @FXML
    private Button addButtonMenu;
    @FXML
    private Button deleteButtonMenu;
    @FXML
    private Button editButtonMenu;
    //
    int lastMouseX;
    int lastMouseY;
    int selectedRowId;


    /*TABLE DECLARATION*/
    private ObservableList<User> userList;
    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, Integer> assetColumn;
    @FXML
    private TableColumn<User, Integer> liabilityColumn;

    //END OF DECLARATION//
    //START OF INIT//
    @FXML
    public void initialize() {
//        arrayTypeColumn.setCellValueFactory(new PropertyValueFactory<>("arrayType"));
//        input1Column.setCellValueFactory(new PropertyValueFactory<>("input1"));
//        input2Column.setCellValueFactory(new PropertyValueFactory<>("input2"));
//        input3Column.setCellValueFactory(new PropertyValueFactory<>("input3"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assetColumn.setCellValueFactory(new PropertyValueFactory<>("asset"));
        liabilityColumn.setCellValueFactory(new PropertyValueFactory<>("liability"));

        if(getFromDB() != null)
            tableView.setItems(getFromDB()); userList = getFromDB();
    }
    //END OF INIT//
    //START OF EVENT LISTENERS//

    @FXML
    void onAddPress(ActionEvent e) {
        fadeInTransition(500, gridPaneAdd, gridPaneEdit);
        errorTextForAdd.setText("");
    }


    @FXML
    void onEditPress(ActionEvent e) {
        if(tableHasSelection()){
            fadeInTransition(500, gridPaneEdit, gridPaneAdd);
            User selectedUser = tableView.getSelectionModel().getSelectedItem();

            editNameTextField.setText(selectedUser.getName());
            editNameTextField.setPromptText(selectedUser.getName());

            editAssetTextField.setText(String.valueOf(selectedUser.getAsset()));
            editAssetTextField.setPromptText(String.valueOf(selectedUser.getAsset()));

            editLiabilityTextField.setText(String.valueOf(selectedUser.getLiability()));
            editLiabilityTextField.setPromptText(String.valueOf(selectedUser.getLiability()));

        } else {
            showMessageViaToolTip(editButtonMenu, "Select a field to edit!");
        }
    }

    @FXML
    void onDeletePress(ActionEvent e) {
        try {
            if(tableHasSelection()){

                if(showConfirmationViaAlert("Please confirm you want to delete selected field!")) {

                    ObservableList<User> selectedList = tableView.getSelectionModel().getSelectedItems(), itemsList = tableView.getItems();
                    for (User i : selectedList) {
                        itemsList.remove(i);
                        deleteFromDB(i.getName(), i.getAsset(), i.getLiability());
                    }
                    userList = tableView.getItems();

                }

            } else
                showMessageViaToolTip(deleteButtonMenu, "Select a field to delete!");

        } catch (Exception ex) {}
    }

    //
    @FXML
    void onAddToTablePress(ActionEvent e){
        try {
            nameInput = nameTextField.getText();
            assetInput = Integer.parseInt(assetTextField.getText());
            liabilityInput = Integer.parseInt(liabilityTextField.getText());
            addValuesToTable(nameInput, assetInput, liabilityInput);
            nameTextField.clear();
            assetTextField.clear();
            liabilityTextField.clear();
            errorTextForAdd.setText("");
        } catch (Exception exc){
            exc.printStackTrace();
            errorTextForAdd.setText("ERROR: Asset and Liability inputs cannot be empty and cannot include letters.");
        }
    }
    @FXML
    void onDoneEditPress(ActionEvent e){
        try {
            editDB(editNameTextField.getPromptText(), Integer.parseInt(editAssetTextField.getPromptText()), Integer.parseInt(editLiabilityTextField.getPromptText()), editNameTextField.getText(), Integer.parseInt(editAssetTextField.getText()), Integer.parseInt(editLiabilityTextField.getText()));
            selectedRowId = tableView.getSelectionModel().getFocusedIndex();
            userList.set(selectedRowId, new User(editNameTextField.getText(), Integer.parseInt(editAssetTextField.getText()), Integer.parseInt(editLiabilityTextField.getText())));
            tableView.setItems(userList);
            gridPaneEdit.setVisible(false);
            errorTextForEdit.setText("");
        } catch (Exception exce){
            exce.printStackTrace();
            errorTextForEdit.setText("ERROR: Asset and Liability inputs cannot be empty and cannot include letters.");
        }
    }
    @FXML
    void onCancelEditPress(ActionEvent e){
        gridPaneEdit.setVisible(false);
        errorTextForEdit.setText("");
    }

    //END OF EVENT LISTENERS//
    //START OF METHODS//

    /*DIRECT METHODS*/
    public void addValuesToTable(String name, int assetAmount, int liabilityAmount){

        userList = tableView.getItems();
        userList.add(new User(name, assetAmount, liabilityAmount));
        tableView.setItems(userList);
        addToDB(name, assetAmount, liabilityAmount);
    }


    /*REPEATING METHODS*/
    public void fadeInTransition(double milliseconds, Node nodeVisible, Node nodeInvisible){
        FadeTransition ft = new FadeTransition(Duration.millis(milliseconds), nodeVisible);
        ft.setFromValue(0);
        ft.setToValue(1);
        nodeInvisible.setVisible(false);
        nodeVisible.setVisible(true);
        ft.play();
    }

    public boolean tableHasSelection(){
        return tableView.getSelectionModel().isEmpty()? false: true;
    }

    public void showMessageViaToolTip(Button button, String text){
        button.setOnMousePressed(event -> {
            lastMouseX = (int) event.getScreenX();
            lastMouseY = (int) event.getScreenY();
        });
        button.setTooltip(new Tooltip(text));
        Tooltip tooltip = button.getTooltip();
        tooltip.setAutoHide(true);
        tooltip.show(button, lastMouseX, lastMouseY);
        button.setOnMouseMoved(event -> {
            tooltip.hide();
        });
    }

    public boolean showConfirmationViaAlert(String text){
        //NONE, INFORMATION, WARNING, CONFIRMATION, ERROR
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, text);
        alert.setHeaderText(null);
        //CHANGE ICON OF ALERT
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:icon.png"));
        //RETURN RESULT
        return alert.showAndWait().get().getText().equalsIgnoreCase("OK")? true: false;
    }
    //END OF METHODS//
}