package com.teach.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class SystemSummaryController {
    @FXML
    private void onPlay(){
        try {
            Image image = new Image(getClass().getResourceAsStream("/BackGroundPicture.jpg"));
            System.out.println("图片加载成功，宽度: " + image.getWidth());
        } catch (Exception e) {
            System.out.println("图片加载失败: " + e.getMessage());
        }
    }

}
