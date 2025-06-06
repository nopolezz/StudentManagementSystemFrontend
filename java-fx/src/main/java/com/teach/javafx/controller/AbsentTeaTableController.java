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
import java.util.*;

public class AbsentTeaTableController {
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
    private List<OptionItem> absentIdList;
    private AbsentTeaEditController absentTeaEditController = null;
    private Stage stage = null;
    public List<OptionItem> getStudentList() {
        return studentList;
    }

    public List<OptionItem> getAbsentIdList() {
        return absentIdList;
    }

    @FXML
    private void onQueryButtonClick(){
        Integer personId = 0;
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            personId = Integer.parseInt(op.getValue());
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("personId",personId);
        res = HttpRequestUtil.request("/api/absent/getAbsentList/student",req);
        if(res != null && res.getCode() == 0) {
            absentListWithStatus = (ArrayList<Map>)res.getData();
        }
        setTableViewData();
    }

    private void setTableViewData() {
        observableList.clear();
        Map map;
        Button editButton;
        // 定义状态映射
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("0.0", "未审批");
        statusMap.put("1.0", "同意");
        statusMap.put("2.0", "不同意");

        for (int j = 0; j < absentListWithStatus.size(); j++) {
            map = new HashMap<>(absentListWithStatus.get(j)); // 复制原始 Map 以避免修改原始数据
            editButton = new Button("编辑");
            editButton.setId("edit" + j);
            editButton.setOnAction(e -> {
                editItem(((Button)e.getSource()).getId());
            });
            map.put("edit", editButton);
            // 转换 auth_status 为中文
            String status = map.get("auth_status") != null ? map.get("auth_status").toString() : "0";
            map.put("auth_status", statusMap.getOrDefault(status, "未知状态"));
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
        absentTeaEditController.showDialog(data);
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
        absentIdList = HttpRequestUtil.requestOptionItemList("/api/absent/getAbsentItemOptionList",req);
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
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("absent-tea-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 600, 400);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/teach/javafx/picture/xiaohui.jpg"))));
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("请假审批");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            absentTeaEditController = (AbsentTeaEditController) fxmlLoader.getController();
            absentTeaEditController.setAbsentTeaTableController(this);
            absentTeaEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void doClose(String cmd, Map<String, Object> data) {
        MainApplication.setCanClose(true);
        stage.close();
        if("ok".equals(cmd)) {
            DataResponse res;
            Integer absentId = CommonMethod.getInteger(data, "absentId");
            if (absentId == null) {
                MessageDialog.showDialog("没有选中假条编号不能添加保存！");
                return;
            }

            DataRequest req = new DataRequest();
            req.add("absentId", CommonMethod.getInteger(data, "absentId"));
            req.add("auth_status", CommonMethod.getInteger(data, "auth_status"));
            res = HttpRequestUtil.request("/api/absent/absentAuth", req);
            if (res != null && res.getCode() == 0) {
                onQueryButtonClick();
            }
        }else if("reject".equals(cmd)) {
            DataRequest req = new DataRequest();
            DataResponse res;
            req.add("absentId", CommonMethod.getInteger(data, "absentId"));
            req.add("auth_status", CommonMethod.getInteger(data, "auth_status"));
            res = HttpRequestUtil.request("/api/absent/absentAuth", req);
            if (res != null && res.getCode() == 0) {
                onQueryButtonClick();
            }
        }
    }
    @FXML
    private void onAuditButtonClick() {
        initDialog();
        absentTeaEditController.showDialog(null);
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