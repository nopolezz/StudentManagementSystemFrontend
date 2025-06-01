package com.teach.javafx.controller;

import com.teach.javafx.request.OptionItem;
import com.teach.javafx.util.CommonMethod;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumptionEditController {
    @FXML
    private ComboBox<OptionItem> studentComboBox;
    private List<OptionItem> studentList;
    @FXML
    private TextField amountField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField descriptionField;

    @FXML
    private DatePicker datePicker;
    private ConsumptionController consumptionController= null;
    private Integer consumptionId= null;

    @FXML
    public void initialize() {
    }

    @FXML
    public void okButtonClick(){
        Map<String,Object> data = new HashMap<>();
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("personId",Integer.parseInt(op.getValue()));
        }
        data.put("consumptionId",consumptionId);
        data.put("amount",amountField.getText());
        data.put("consumptionTime",datePicker.getValue());
        data.put("location", locationField.getText());
        data.put("description", descriptionField.getText());

        consumptionController.doClose("ok",data);
    }

    @FXML
    public void cancelButtonClick(){
        consumptionController.doClose("cancel",null);
    }

    public void setConsumptionController(ConsumptionController consumptionController) {
        this.consumptionController = consumptionController;
    }

    public void init(){
        studentList =consumptionController.getStudentList();
        studentComboBox.getItems().addAll(studentList);
    }

    public void showDialog(Map data){
        if(data == null) {
            consumptionId = null;
            studentComboBox.getSelectionModel().select(-1);
            studentComboBox.setDisable(false);
            datePicker.setDisable(false);
            amountField.setText("");
            locationField.setText("");
            descriptionField.setText("");
        }else {
            consumptionId = CommonMethod.getInteger(data,"consumptionId");
            studentComboBox.setDisable(false);
            datePicker.setDisable(false);
            amountField.setText(CommonMethod.getString(data, "amount"));
            locationField.setText(CommonMethod.getString(data, "location"));
            descriptionField.setText(CommonMethod.getString(data, "description"));
        }
    }
}
