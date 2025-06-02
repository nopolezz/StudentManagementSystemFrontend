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

public class PracticeStuEditController {
    @FXML
    private ComboBox<OptionItem> studentComboBox;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<OptionItem> typeComboBox;
    @FXML
    private DatePicker timePicker;
    @FXML
    private TextField placeField;

    private List<OptionItem> studentList;
    private List<OptionItem> typeList;
    private PracticeStuTableController practiceStuTableController= null;
    private Integer practiceId = null;
    private Integer personId = null;
    private Integer status = null;

    public void setPracticeStuTableController(PracticeStuTableController practiceStuTableController) {
        this.practiceStuTableController = practiceStuTableController;
    }

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
        data.put("absentId",practiceId);
        data.put("name",nameField.getText());
        data.put("time",timePicker.getValue());
        data.put("location",placeField.getText());
        practiceStuTableController.doClose("ok",data);
    }

    @FXML
    public void cancelButtonClick(){
        practiceStuTableController.doClose("cancel",null);
    }

    public void init(){
        studentList = practiceStuTableController.getStudentList();
        studentComboBox.getItems().addAll(studentList);
        typeList = practiceStuTableController.getTypeList();
        typeComboBox.getItems().addAll(typeList);
    }

    public void showDialog(Map data){
        if(data == null) {
            personId = null;
            practiceId = null;
            status = null;
            studentComboBox.getSelectionModel().select(-1);
            studentComboBox.setDisable(false);
            typeComboBox.getSelectionModel().select(-1);
            typeComboBox.setDisable(false);
            nameField.setText("");
            timePicker.setDisable(false);
            placeField.setText("");
        }else {
            personId = CommonMethod.getInteger(data,"personId");
            practiceId = CommonMethod.getInteger(data,"practiceId");
            status = CommonMethod.getInteger(data,"status");
            studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getString(data, "personId")));
            studentComboBox.setDisable(true);
            typeComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(typeList, CommonMethod.getString(data, "type")));
            typeComboBox.setDisable(false);
            nameField.setText(CommonMethod.getString(data, "reason"));
            timePicker.setDisable(false);
            placeField.setText(CommonMethod.getString(data, "location"));
        }
    }
}
