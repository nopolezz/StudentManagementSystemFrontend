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

public class PracticeStuTableController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> nameColumn;
    @FXML
    private TableColumn<Map,String> timeColumn;
    @FXML
    private TableColumn<Map,String> typeColumn;
    @FXML
    private TableColumn<Map,String> placeColumn;
    @FXML
    private TableColumn<Map, Button> statusColumn;
    @FXML
    private ComboBox<OptionItem> studentComboBox;
    private List<OptionItem> studentList;
    private List<OptionItem> typeList;
    private ArrayList<Map> practiceList = new ArrayList();
    private Stage stage = null;
    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    private PracticeStuEditController practiceStuEditController = null;
    public List<OptionItem> getStudentList() {return studentList;}
    public List<OptionItem> getTypeList() {
        return typeList;
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
        typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
        placeColumn.setCellValueFactory(new MapValueFactory<>("place"));
        statusColumn.setCellValueFactory(new MapValueFactory<>("status"));

        DataRequest req =new DataRequest();
        studentList = HttpRequestUtil.requestOptionItemList("/api/practice/getStudentItemOptionList",req);
        typeList = HttpRequestUtil.requestOptionItemList("/api/practice/getTypeItemOptionList",req);
        OptionItem item = new OptionItem(null,"0","请选择");
        studentComboBox.getItems().addAll(item);
        studentComboBox.getItems().addAll(studentList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onQueryButtonClick();
    }

    @FXML
    private void onQueryButtonClick(){
        Integer personId = null;
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            personId = Integer.parseInt(op.getValue());
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("personId",personId);
        res = HttpRequestUtil.request("/api/practice/getPracticeList/student",req);
        if(res != null && res.getCode() == 0) {
            practiceList = (ArrayList<Map>)res.getData();
        }
        setTableViewData();
    }

    private void setTableViewData() {
        observableList.clear();
        Map map;
        Button editButton;
        for (int j = 0; j < practiceList.size(); j++) {
            map = practiceList.get(j);
            editButton = new Button("编辑");
            editButton.setId("edit"+j);
            editButton.setOnAction(e->{
                editItem(((Button)e.getSource()).getId());
            });
            map.put("edit",editButton);
            observableList.addAll(FXCollections.observableArrayList(map));
        }//说实话82-92没什么用，但懒得改了
        dataTableView.setItems(observableList);
    }

    public void editItem(String name){
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));//同上
        Map data = practiceList.get(j);
        initDialog();
        practiceStuEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    private void initDialog() {
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("practice-stu-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 600, 400);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/teach/javafx/picture/xiaohui.jpg"))));
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("创新实践活动申请");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            practiceStuEditController = (PracticeStuEditController) fxmlLoader.getController();
            practiceStuEditController.setPracticeStuTableController(this);
            practiceStuEditController.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onApplyButtonClick() {
        initDialog();
        practiceStuEditController.showDialog(null);
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
        practiceStuEditController.showDialog(data);
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
        Integer practiceId = CommonMethod.getInteger(form,"practiceId");
        DataRequest req = new DataRequest();
        req.add("practiceId", practiceId);
        DataResponse res = HttpRequestUtil.request("/api/practice/practiceDelete",req);
        if(res.getCode() == 0) {
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
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
        req.add("practiceId",CommonMethod.getInteger(data,"absentId"));
        req.add("name",CommonMethod.getString(data,"name"));
        req.add("location",CommonMethod.getString(data,"location"));
        req.add("time",CommonMethod.getString(data,"time"));
        res = HttpRequestUtil.request("/api/practice/practiceSave",req);
        DataRequest req1 =new DataRequest();
        req1.add("practice",CommonMethod.getInteger(data,"absentId"));
        req1.add("status",null);
        if(res != null && res.getCode() == 0) {
            onQueryButtonClick();
        }
    }
}
