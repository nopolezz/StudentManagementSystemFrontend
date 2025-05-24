package com.teach.javafx.controller;

import com.teach.javafx.request.DataRequest;
import com.teach.javafx.request.DataResponse;
import com.teach.javafx.request.HttpRequestUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Map;

public class CourseController {
    @FXML private TableView<Map> dataTableView;
    @FXML private TableColumn<Map, String> numColumn;
    @FXML private TableColumn<Map, String> nameColumn;
    @FXML private TableColumn<Map, String> creditColumn;
    @FXML private TableColumn<Map, Void> operateColumn;
    @FXML private TextField numField;
    @FXML private TextField nameField;
    @FXML private TextField creditField;
    @FXML private Button addButton;

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
            private final Button editButton = new Button("编辑");
            private final Button deleteButton = new Button("删除");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Map course = getTableView().getItems().get(getIndex());
                    editButton.setOnAction(event -> editCourse(course));
                    deleteButton.setOnAction(event -> deleteCourse((String) course.get("courseId")));
                    HBox hbox = new HBox(5, editButton, deleteButton);
                    setGraphic(hbox);
                    setText(null);
                }
            }
        });

        loadCourses();
    }

    private void loadCourses() {
        DataRequest req = new DataRequest();
        req.add("numName", "");
        DataResponse res = HttpRequestUtil.request("/api/course/getCourseList", req);
        if (res != null && res.getCode() == 0) {
            courseList = (ArrayList<Map>) res.getData();
            setTableViewData();
        } else {
            showAlert("错误", "无法加载课程列表");
        }
    }

    @FXML
    private void saveCourse() {
        DataRequest req = new DataRequest();
        req.add("num", numField.getText());
        req.add("name", nameField.getText());
        req.add("credit", creditField.getText().isEmpty() ? null : Integer.parseInt(creditField.getText()));
        // 假设前序课程通过其他控件选择，暂设为 null


        DataResponse res = HttpRequestUtil.request("/api/course/courseSave", req);
        if (res != null && res.getCode() == 0) {
            showAlert("成功", "课程保存成功");
            clearFields();
            loadCourses();
        } else {
            showAlert("错误", res != null ? res.getMsg() : "保存失败");
        }
    }

    private void editCourse(Map course) {
        numField.setText((String) course.get("num"));
        nameField.setText((String) course.get("name"));
        creditField.setText((String) course.get("credit"));
        addButton.setText("更新");
        addButton.setOnAction(event -> updateCourse((String) course.get("courseId")));
    }

    private void updateCourse(String courseId) {
        DataRequest req = new DataRequest();
        req.add("courseId", Integer.parseInt(courseId));
        req.add("num", numField.getText());
        req.add("name", nameField.getText());
        req.add("credit", creditField.getText().isEmpty() ? null : Integer.parseInt(creditField.getText()));
        DataResponse res = HttpRequestUtil.request("/api/course/courseSave", req);
        if (res != null && res.getCode() == 0) {
            showAlert("成功", "课程更新成功");
            clearFields();
            addButton.setText("添加");
            addButton.setOnAction(event -> saveCourse());
            loadCourses();
        } else {
            showAlert("错误", res != null ? res.getMsg() : "更新失败");
        }
    }

    private void deleteCourse(String courseId) {
        DataRequest req = new DataRequest();
        req.add("courseId", Integer.parseInt(courseId));
        DataResponse res = HttpRequestUtil.request("/api/course/courseDelete", req);
        if (res != null && res.getCode() == 0) {
            showAlert("成功", "课程删除成功");
            loadCourses();
        } else {
            showAlert("错误", res != null ? res.getMsg() : "删除失败");
        }
    }

    private void clearFields() {
        numField.clear();
        nameField.clear();
        creditField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}