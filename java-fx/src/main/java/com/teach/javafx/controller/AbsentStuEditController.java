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

public class AbsentStuEditController {
    @FXML
    private ComboBox<OptionItem> studentComboBox;
    private List<OptionItem> studentList;
    @FXML
    private TextField reasonField;
    @FXML
    private DatePicker startPicker;
    @FXML
    private DatePicker endPicker;
    @FXML
    private TextField placeField;
    private AbsentStuTableController absentStuTableController= null;
    private Integer studentId= null;
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
        data.put("reason",reasonField.getText());
        data.put("start",startPicker.getValue());
        data.put("end",endPicker.getValue());
        data.put("place",placeField.getText());
        data.put("auth_status",0);
        absentStuTableController.doClose("ok",data);
    }
    @FXML
    public void cancelButtonClick(){
        absentStuTableController.doClose("cancel",null);
    }

    public void setAbsentStuTableController(AbsentStuTableController absentStuTableController) {
        this.absentStuTableController = absentStuTableController;
    }
    public void init(){
        studentList =absentStuTableController.getStudentList();
        studentComboBox.getItems().addAll(studentList );
    }
    public void showDialog(Map data){
        if(data == null) {
            studentId = null;
            studentComboBox.getSelectionModel().select(-1);
            studentComboBox.setDisable(false);
            reasonField.setText("");
            startPicker.setDisable(false);
            endPicker.setDisable(false);
            placeField.setText("");
        }else {
            studentId = CommonMethod.getInteger(data,"studentId");
            studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getString(data, "personId")));
            studentComboBox.setDisable(false);
            reasonField.setText(CommonMethod.getString(data, "reason"));
            startPicker.setDisable(false);
            endPicker.setDisable(false);
            placeField.setText(CommonMethod.getString(data, "place"));
        }
    }
}
