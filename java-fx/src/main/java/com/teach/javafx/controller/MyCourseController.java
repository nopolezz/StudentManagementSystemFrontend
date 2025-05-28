package com.teach.javafx.controller;

import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.DataRequest;
import com.teach.javafx.request.DataResponse;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.AppStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class MyCourseController {
    @FXML private TableView<Map> dataTableView;
    @FXML private TableColumn<Map, String> numColumn;
    @FXML private TableColumn<Map, String> nameColumn;
    @FXML private TableColumn<Map, String> creditColumn;
    @FXML private TableColumn<Map, Void> operateColumn;
    private ArrayList<Map> courseList = new ArrayList<>();
    private ObservableList<Map> observableList = FXCollections.observableArrayList();

    private void setTableViewData() {
        observableList.clear();
        observableList.addAll(courseList);
        dataTableView.setItems(observableList);
    }

    public void initialize() {
        numColumn.setCellValueFactory(new MapValueFactory<>("num"));
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));

        operateColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deselectButton = new Button("退课");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Map course = getTableView().getItems().get(getIndex());
                    deselectButton.setOnAction(event -> deselectCourse((String) course.get("courseId")));
                    setGraphic(deselectButton);
                }
            }
        });

        loadSelectedCourses();
    }

    private void loadSelectedCourses() {
        DataRequest req = new DataRequest();
        req.add("studentId", AppStore.getJwt().getId()); // user.person_id
        DataResponse res = HttpRequestUtil.request("/api/course/getSelectedCourses", req);
        if (res != null && res.getCode() == 0) {
            courseList = (ArrayList<Map>) res.getData();
            setTableViewData();
        } else {
            MessageDialog.showDialog("无法加载已选课程");
        }
    }

    private void deselectCourse(String courseId) {
        DataRequest req = new DataRequest();
        System.out.println(courseId);
        req.add("studentId", AppStore.getJwt().getId()); // user.person_id
        req.add("courseId", Integer.parseInt(courseId));
        DataResponse res = HttpRequestUtil.request("/api/course/deselectCourse", req);
        if (res != null && res.getCode() == 0) {
            MessageDialog.showDialog("选课成功");
            loadSelectedCourses(); // 刷新列表
        } else {
            MessageDialog.showDialog("错误，选课失败");
        }
    }
    @FXML
    private void refreshClick(){
        loadSelectedCourses();
    }
}