package com.teach.javafx.controller;

import com.teach.javafx.request.DataRequest;
import com.teach.javafx.request.DataResponse;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.AppStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import java.util.ArrayList;
import java.util.Map;

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
            showAlert("错误", "无法加载已选课程");
        }
    }

    private void deselectCourse(String courseId) {
        DataRequest req = new DataRequest();
        System.out.println(courseId);
        req.add("studentId", AppStore.getJwt().getId()); // user.person_id
        req.add("courseId", Integer.parseInt(courseId));
        DataResponse res = HttpRequestUtil.request("/api/course/deselectCourse", req);
        if (res != null && res.getCode() == 0) {
            showAlert("成功", "退课成功");
            loadSelectedCourses(); // 刷新列表
        } else {
            showAlert("错误", res != null ? res.getMsg() : "退课失败");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void refreshClick(){
        loadSelectedCourses();
    }
}