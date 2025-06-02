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

public class PracticeTeaTableController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> practiceIdColumn;
    @FXML
    private TableColumn<Map,String> nameColumn;
    @FXML
    private TableColumn<Map,String> typeColumn;
    @FXML
    private TableColumn<Map,String> placeColumn;
    @FXML
    private TableColumn<Map,String> timeColumn;
    @FXML
    private TableColumn<Map, Button> statusColumn;
    @FXML
    private ComboBox<OptionItem> studentComboBox;

    private ArrayList<Map> practiceList = new ArrayList();
    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    private List<OptionItem> studentList;
    private List<OptionItem> practiceIdList;
    private Stage stage = null;
    private PracticeTeaEditController practiceTeaEditController = null;
    public List<OptionItem> getStudentList() {return studentList;}
    public List<OptionItem> getPracticeIdList() {
        return practiceIdList;
    }

    @FXML
    public void initialize() {
        practiceIdColumn.setCellValueFactory(new MapValueFactory<>("practiceId"));
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
        placeColumn.setCellValueFactory(new MapValueFactory<>("place"));
        timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
        statusColumn.setCellValueFactory(new MapValueFactory<>("status"));
        DataRequest req =new DataRequest();
        studentList = HttpRequestUtil.requestOptionItemList("/api/practice/getStudentItemOptionList",req);
        practiceIdList = HttpRequestUtil.requestOptionItemList("api/practice/getPracticeItemOptionList",req);
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
        }
        dataTableView.setItems(observableList);
    }

    public void editItem(String name){
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = practiceList.get(j);
        initDialog();
        practiceTeaEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    private void initDialog() {
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("practice-tea-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 600, 400);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/teach/javafx/picture/xiaohui.jpg"))));
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("项目审批");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            practiceTeaEditController = (PracticeTeaEditController) fxmlLoader.getController();
            practiceTeaEditController.setPracticeTeaTableController(this);
            practiceTeaEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onAuditButtonClick() {
        initDialog();
        practiceTeaEditController.showDialog(null);
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
        if("ok".equals(cmd)) {
            DataResponse res;
            Integer practiceId = CommonMethod.getInteger(data, "practiceId");
            if (practiceId == null) {
                MessageDialog.showDialog("没有选中项目不能添加保存！");
                return;
            }
            DataRequest req = new DataRequest();
            req.add("practice", CommonMethod.getInteger(data, "practiceId"));
            req.add("status", CommonMethod.getInteger(data, "status"));
            res = HttpRequestUtil.request("/api/practice/practiceAuth", req);
            if (res != null && res.getCode() == 0) {
                onQueryButtonClick();
            }
        }else if("reject".equals(cmd)) {
            DataRequest req = new DataRequest();
            DataResponse res;
            req.add("practice", CommonMethod.getInteger(data, "practiceId"));
            req.add("status", CommonMethod.getInteger(data, "status"));
            res = HttpRequestUtil.request("/api/practice/practiceAuth", req);
            if (res != null && res.getCode() == 0) {
                onQueryButtonClick();
            }
        }
    }
}
