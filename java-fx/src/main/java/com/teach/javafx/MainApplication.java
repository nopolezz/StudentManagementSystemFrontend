package com.teach.javafx;

import com.teach.javafx.request.HttpRequestUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * 应用的主程序 MainApplication 按照编程规范，需继承Application 重写start 方法 主方法调用Application的launch() 启动程序
 */
public class MainApplication extends Application {
    /**
     * 加载登录对话框，设置登录Scene到Stage,显示该场景
     */
    private static Stage mainStage;
    private static double stageWidth = -1;
    private static double stageHeight = -1;

    private static boolean canClose=true;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("教学管理系统");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/teach/javafx/picture/xiaohui.jpg"))));
        stage.show();
        stage.setOnCloseRequest(event -> {
            if(canClose) {
               HttpRequestUtil.close();
           }else {
               event.consume();
           }
        });
        mainStage = stage;
    }

    /**
     * 给舞台设置新的Scene
     * @param name 标题
     * @param scene 新的场景对象
     */
    public static void resetStage(String name, Scene scene) {
        if(stageWidth > 0) {
            mainStage.setWidth(stageWidth);
            mainStage.setHeight(stageHeight);
            mainStage.setX(0);
            mainStage.setY(0);

        }
        mainStage.setTitle(name);
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.show();
    }

    public static void loginStage(String name, Scene scene) {
        stageWidth = mainStage.getWidth();
        stageHeight = mainStage.getHeight();
        mainStage.setTitle(name);
        mainStage.setScene(scene);
        double x = (stageWidth-600)/2;
        double y = (stageHeight-400)/2;
        mainStage.setX(x);
        mainStage.setY(y);
        mainStage.setWidth(600);
        mainStage.setHeight(400);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setCanClose(boolean canClose) {
        MainApplication.canClose = canClose;
    }
}