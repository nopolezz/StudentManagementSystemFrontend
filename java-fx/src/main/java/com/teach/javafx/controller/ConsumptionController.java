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

public class ConsumptionController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map, String> consumptionIDColumn;
    @FXML
    private TableColumn<Map, String> consumptionTimeColumn;
    @FXML
    private TableColumn<Map, String> amountColumn;
    @FXML
    private TableColumn<Map, String> locationColumn;
    @FXML
    private TableColumn<Map, String> descriptionColumn;
    @FXML
    private ComboBox<OptionItem> studentComboBox;

    private ArrayList<Map> consumptionList = new ArrayList();
    private ObservableList<Map> observableList = FXCollections.observableArrayList();
    private List<OptionItem> studentList;

    private ConsumptionEditController consumptionEditController = null;
    private Stage stage = null;

    public List<OptionItem> getStudentList() {
        return studentList;
    }

    @FXML
    private void onQueryButtonClick() {
        Integer personId = 0;
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        if (op != null) personId = Integer.parseInt(op.getValue());
        DataResponse res;
        DataRequest req = new DataRequest();
        req.add("personId", personId);
        res = HttpRequestUtil.request("/api/consumption/getConsumptionList", req);
        if (res != null && res.getCode() == 0) {
            consumptionList = (ArrayList<Map>) res.getData();
        }
        setTableViewData();
    }

    private void setTableViewData() {
        observableList.clear();
        Map map;
        Button editButton;
        for (int j = 0; j < consumptionList.size(); j++) {
            map = consumptionList.get(j);
            editButton = new Button("编辑");
            editButton.setId("edit" + j);
            editButton.setOnAction(e -> {
                editItem(((Button) e.getSource()).getId());
            });
            map.put("edit", editButton);
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);

    }

    public void editItem(String name) {
        if (name == null) {
            return;
        }
        int j = Integer.parseInt(name.substring(4, name.length()));
        Map data = consumptionList.get(j);
        initDialog();
        consumptionEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    @FXML
    public void initialize() {
        consumptionIDColumn.setCellValueFactory(new MapValueFactory<>("consumptionId"));
        consumptionTimeColumn.setCellValueFactory(new MapValueFactory<>("consumptionTime"));
        amountColumn.setCellValueFactory(new MapValueFactory<>("amount"));
        locationColumn.setCellValueFactory(new MapValueFactory<>("location"));
        descriptionColumn.setCellValueFactory(new MapValueFactory<>("description"));

        DataRequest req = new DataRequest();
        studentList = HttpRequestUtil.requestOptionItemList("/api/consumption/getStudentItemOptionList", req);
        OptionItem item = new OptionItem(null, "0", "请选择");
        studentComboBox.getItems().addAll(item);
        studentComboBox.getItems().addAll(studentList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onQueryButtonClick();
    }

    private void initDialog() {
        if (stage != null)
            return;
        FXMLLoader fxmlLoader;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("consumption-edit-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 400, 200);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/teach/javafx/picture/xiaohui.jpg"))));
            stage.setScene(scene);
            stage.setTitle("消费录入");
            stage.setOnCloseRequest(event -> {
                MainApplication.setCanClose(true);
            });
            consumptionEditController = (ConsumptionEditController) fxmlLoader.getController();
            consumptionEditController.setConsumptionController(this);
            consumptionEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void doClose(String cmd, Map<String, Object> data) {
        MainApplication.setCanClose(true);
        stage.close();
        if (!"ok".equals(cmd))
            return;
        DataResponse res;
        Integer personId = CommonMethod.getInteger(data, "personId");
        if (personId == null) {
            MessageDialog.showDialog("没有选中学生不能添加保存！");
            return;
        }
        DataRequest req = new DataRequest();
        req.add("personId", personId);
        req.add("consumptionId", CommonMethod.getInteger(data, "consumptionId"));
        req.add("consumptionTime", CommonMethod.getString(data, "consumptionTime"));
        req.add("amount", CommonMethod.getString(data, "amount"));
        req.add("location", CommonMethod.getString(data, "location"));
        req.add("description", CommonMethod.getString(data, "description"));
        res = HttpRequestUtil.request("/api/consumption/addRecord", req);
        if (res != null && res.getCode() == 0) {
            onQueryButtonClick();
        }
    }

    @FXML
    private void onAddButtonClick() {
        initDialog();
        consumptionEditController.showDialog(null);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    @FXML
    private void onEditButtonClick() {
        Map data = dataTableView.getSelectionModel().getSelectedItem();
        if (data == null) {
            MessageDialog.showDialog("没有选中，不能修改！");
            return;
        }
        initDialog();
        consumptionEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    @FXML
    private void onDeleteButtonClick() {
        Map<String, Object> form = dataTableView.getSelectionModel().getSelectedItem();
        if (form == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if (ret != MessageDialog.CHOICE_YES) {
            return;
        }
        Integer honorId = CommonMethod.getInteger(form, "consumptionId");
        DataRequest req = new DataRequest();
        req.add("consumptionId", honorId);
        DataResponse res = HttpRequestUtil.request("/api/consumption/deleteRecord", req);
        if (res.getCode() == 0) {
            onQueryButtonClick();
        } else {
            MessageDialog.showDialog(res.getMsg());
        }

    }
}