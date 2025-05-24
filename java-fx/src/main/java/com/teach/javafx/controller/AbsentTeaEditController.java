package com.teach.javafx.controller;

import com.teach.javafx.request.OptionItem;
import com.teach.javafx.util.CommonMethod;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbsentTeaEditController {
    @FXML
    private ComboBox<OptionItem> absentIdComboBox;
    private List<OptionItem> absentIdList;
    private AbsentTeaTableController absentTeaTableController= null;
    private Integer auth_status= null;
    @FXML
    public void initialize() {
    }

    @FXML
    public void okButtonClick(){
        Map<String,Object> data = new HashMap<>();
        OptionItem op;
        op = absentIdComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("absentId", Integer.parseInt(op.getValue()));
            auth_status= 1;
            data.put("auth_status", auth_status);
        }
        absentTeaTableController.doClose("ok",data);
    }
    @FXML
    public void rejectButtonClick(){
        Map<String,Object> data = new HashMap<>();
        OptionItem op;
        op = absentIdComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("absentId", Integer.parseInt(op.getValue()));
        }
        auth_status= 2;
        data.put("auth_status", auth_status);
        absentTeaTableController.doClose("reject",data);
    }

    @FXML
    public void cancelButtonClick(){
        Map<String,Object> data = new HashMap<>();
        auth_status= 0;
        data.put("auth_status", auth_status);
        absentTeaTableController.doClose("cancel",null);
    }

    public void setAbsentTeaTableController(AbsentTeaTableController absentTeaTableController) {
        this.absentTeaTableController = absentTeaTableController;
    }

    public void init(){
        absentIdList = absentTeaTableController.getAbsentIdList();
        absentIdComboBox.getItems().addAll(absentIdList);
    }

    public void showDialog(Map data){
        if(data == null) {
            auth_status= null;
            absentIdComboBox.getSelectionModel().select(-1);
            absentIdComboBox.setDisable(false);
        }else {
            auth_status = CommonMethod.getInteger(data,"auth_status");
            absentIdComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(absentIdList, CommonMethod.getString(data, "absentId")));
            absentIdComboBox.setDisable(true);
        }
    }
}
