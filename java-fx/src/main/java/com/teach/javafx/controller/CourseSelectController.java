package com.teach.javafx.controller;

import com.teach.javafx.AppStore;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.models.Student;
import com.teach.javafx.request.DataRequest;
import com.teach.javafx.request.DataResponse;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.JwtResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class CourseSelectController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> numColumn;
    @FXML
    private TableColumn<Map,String> nameColumn;
    @FXML
    private TableColumn<Map,String> creditColumn;
    @FXML
    private TableColumn<Map, String> operateColumn;

    private ArrayList<Map> courseList = new ArrayList<>();
    private ObservableList<Map> observableList = FXCollections.observableArrayList();

    private void setTableViewData() {
        observableList.clear();
        observableList.addAll(FXCollections.observableArrayList(courseList));
        dataTableView.setItems(observableList);
    }

    @FXML
    public void initialize() {

        numColumn.setCellValueFactory(new MapValueFactory<>("num"));
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));

        // 为操作列设置单元格工厂
        operateColumn.setCellFactory(col -> new TableCell<>() {
            private final Button selectButton = new Button("选择");
            private final HBox buttonBox = new HBox( selectButton);
            {
                // 设置按钮样式
                selectButton.setStyle("-fx-background-color: #90EE90; -fx-text-fill: white;");
                selectButton.setAlignment(Pos.CENTER);

            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Map course =getTableView().getItems().get(getIndex());
                    selectButton.setOnAction((event -> selectCourse((String)course.get("courseId"))));
                    setGraphic(buttonBox);
                }
            }
        });
        loadCourses();

    }
    private void selectCourse(String courseId){
        JwtResponse jwt = AppStore.getJwt();
        if (jwt ==null||jwt.getId() == null){
           MessageDialog.showDialog("错误，请先登录");
            return;
        }
        DataRequest req =new DataRequest();
        req.add("studentId",jwt.getId());
        req.add("courseId",Integer.parseInt(courseId));
        DataResponse res =HttpRequestUtil.request("/api/course/selectCourse",req);
        if (res !=null&& res.getCode() == 0){
            MessageDialog.showDialog("选课成功");
        }
        else
            MessageDialog.showDialog("选课失败");
    }

    private void loadCourses() {
        DataRequest req = new DataRequest();
        DataResponse res = HttpRequestUtil.request("/api/course/getCourseList", req);
        if (res != null && res.getCode() == 0) {
            courseList = (ArrayList<Map>) res.getData();
            setTableViewData();
        }
        else {
            MessageDialog.showDialog("无法加载课表");
        }
    }

}
