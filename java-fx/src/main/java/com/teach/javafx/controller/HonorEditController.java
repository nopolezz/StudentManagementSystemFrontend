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

public class HonorEditController {
    @FXML
    private ComboBox<OptionItem> studentComboBox;
    private List<OptionItem> studentList;
    @FXML
    private TextField honorNameField;
    @FXML
    private DatePicker datePicker;
    private HonorTableController honorTableController= null;
    private Integer honorId= null;
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
        data.put("honourId",honorId);
        data.put("name",honorNameField.getText());
        data.put("day",datePicker.getValue());
        honorTableController.doClose("ok",data);
    }
    @FXML
    public void cancelButtonClick(){
        honorTableController.doClose("cancel",null);
    }

    public void setHonorTableController(HonorTableController honorTableController) {
        this.honorTableController = honorTableController;
    }
    public void init(){
        studentList =honorTableController.getStudentList();
        studentComboBox.getItems().addAll(studentList );
    }
    public void showDialog(Map data){
        if(data == null) {
            honorId = null;
            studentComboBox.getSelectionModel().select(-1);
            studentComboBox.setDisable(false);
            datePicker.setDisable(false);
            honorNameField.setText("");
        }else {
            honorId = CommonMethod.getInteger(data,"honourId");
            studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getString(data, "personId")));
            studentComboBox.setDisable(true);
            datePicker.setDisable(false);
            honorNameField.setText(CommonMethod.getString(data, "name"));
        }
    }
}
