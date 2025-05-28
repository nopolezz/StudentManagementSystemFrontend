package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.DataRequest;
import com.teach.javafx.request.DataResponse;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import com.teach.javafx.util.CommonMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AbsentStuTableController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> absentIDColumn;
    @FXML
    private TableColumn<Map,String> reasonColumn;
    @FXML
    private TableColumn<Map,String> placeColumn;
    @FXML
    private TableColumn<Map,String> startColumn;
    @FXML
    private TableColumn<Map,String> endColumn;
    @FXML
    private TableColumn<Map, Button> statusColumn;

    private ArrayList<Map> absentListWithStatus = new ArrayList();
    private ObservableList<Map> observableList= FXCollections.observableArrayList();

    @FXML
    private ComboBox<OptionItem> studentComboBox;

    private List<OptionItem> studentList;

    private AbsentStuEditController absentStuEditController = null;
    private Stage stage = null;
    public List<OptionItem> getStudentList() {
        return studentList;
    }

    @FXML
    private void onQueryButtonClick(){
        Integer personId = 0;
        Integer auth_status = 0;
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            personId = Integer.parseInt(op.getValue());
        if(op != null)
            auth_status = Integer.parseInt(op.getValue());
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("personId",personId);
        req.add("auth_status",auth_status);
        res = HttpRequestUtil.request("/api/absent/getAbsentListWithStatus/student",req);
        if(res != null && res.getCode() == 0) {
            absentListWithStatus = (ArrayList<Map>)res.getData();
        }
        setTableViewData();
    }

    private void setTableViewData() {
        observableList.clear();
        Map map;
        Button editButton;
        for (int j = 0; j < absentListWithStatus.size(); j++) {
            map = absentListWithStatus.get(j);
            editButton = new Button("编辑");
            editButton.setId("edit"+j);
            editButton.setOnAction(e->{
                editItem(((Button)e.getSource()).getId());
            });
            map.put("edit",editButton);
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);
    }
    public void editItem(String name){
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = absentListWithStatus.get(j);
        initDialog();
        absentStuEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }
    @FXML
    public void initialize() {
        absentIDColumn.setCellValueFactory(new MapValueFactory<>("absentId"));
        reasonColumn.setCellValueFactory(new MapValueFactory<>("reason"));
        placeColumn.setCellValueFactory(new MapValueFactory<>("place"));
        startColumn.setCellValueFactory(new MapValueFactory<>("start"));
        endColumn.setCellValueFactory(new MapValueFactory<>("end"));
        statusColumn.setCellValueFactory(new MapValueFactory<>("auth_status"));

        DataRequest req =new DataRequest();
        studentList = HttpRequestUtil.requestOptionItemList("/api/absent/getStudentItemOptionList",req);
        OptionItem item = new OptionItem(null,"0","请选择");
        studentComboBox.getItems().addAll(item);
        studentComboBox.getItems().addAll(studentList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onQueryButtonClick();
    }

    private void initDialog() {
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("absent-stu-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 600, 400);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/teach/javafx/picture/xiaohui.jpg"))));
            stage.setScene(scene);
            stage.setTitle("请假申请");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            absentStuEditController = (AbsentStuEditController) fxmlLoader.getController();
            absentStuEditController.setAbsentStuTableController(this);
            absentStuEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void doClose(String cmd, Map<String, Object> data) {
        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        DataResponse res;
        Integer personId = CommonMethod.getInteger(data,"personId");
        if(personId == null) {
            MessageDialog.showDialog("没有选中学生不能添加保存！");
            return;
        }

        DataRequest req =new DataRequest();
        req.add("personId",personId);
        req.add("absentId",CommonMethod.getInteger(data,"absentId"));
        req.add("reason",CommonMethod.getString(data,"reason"));
        req.add("place",CommonMethod.getString(data,"place"));
        req.add("start",CommonMethod.getString(data,"start"));
        req.add("end",CommonMethod.getString(data,"end"));
        req.add("auth_status",CommonMethod.getInteger(data,"auth_status"));
        res = HttpRequestUtil.request("/api/absent/absentSave",req);
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
        }
    }
    @FXML
    private void onApplyButtonClick() {
        initDialog();
        absentStuEditController.showDialog(null);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }
    @FXML
    private void onEditButtonClick() {
        Map data = dataTableView.getSelectionModel().getSelectedItem();
        if(data == null) {
            MessageDialog.showDialog("没有选中，不能修改！");
            return;
        }
        initDialog();
        absentStuEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }
    @FXML
    private void onDeleteButtonClick() {
        Map<String,Object> form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if(ret != MessageDialog.CHOICE_YES) {
            return;
        }
        Integer absentId = CommonMethod.getInteger(form,"absentId");
        DataRequest req = new DataRequest();
        req.add("absentId", absentId);
        DataResponse res = HttpRequestUtil.request("/api/absent/absentDelete",req);
        if(res.getCode() == 0) {
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

}