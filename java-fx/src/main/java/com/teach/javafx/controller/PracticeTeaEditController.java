package com.teach.javafx.controller;

import com.teach.javafx.request.OptionItem;
import com.teach.javafx.util.CommonMethod;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PracticeTeaEditController {
    @FXML
    private ComboBox<OptionItem> practiceIdComboBox;
    private List<OptionItem> practiceIdList;
    private PracticeTeaTableController practiceTeaTableController= null;
    private Integer status= null;

    public void setPracticeTeaTableController(PracticeTeaTableController practiceTeaTableController) {
        this.practiceTeaTableController = practiceTeaTableController;
    }

    public void init(){
        practiceIdList = practiceTeaTableController.getPracticeIdList();
        practiceIdComboBox.getItems().addAll(practiceIdList);
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void okButtonClick(){
        Map<String,Object> data = new HashMap<>();
        OptionItem op;
        op = practiceIdComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("practiceId", Integer.parseInt(op.getValue()));
            status = 1;
            data.put("status", status);
        }
        practiceTeaTableController.doClose("ok",data);
    }

    @FXML
    public void rejectButtonClick(){
        Map<String,Object> data = new HashMap<>();
        OptionItem op;
        op = practiceIdComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("practiceId", Integer.parseInt(op.getValue()));
            status = 2;
            data.put("status", status);
        }
        practiceTeaTableController.doClose("reject",data);
    }

    @FXML
    public void cancelButtonClick(){
        Map<String,Object> data = new HashMap<>();
        status = 0;
        data.put("status", status);
        practiceTeaTableController.doClose("cancel",data);
    }

    public void showDialog(Map data){
        if(data == null) {
            status= null;
            practiceIdComboBox.getSelectionModel().select(-1);
            practiceIdComboBox.setDisable(false);
        }else {
            status = CommonMethod.getInteger(data,"status");
            practiceIdComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(practiceIdList, CommonMethod.getString(data, "practiceId")));
            practiceIdComboBox.setDisable(true);
        }
    }
}
