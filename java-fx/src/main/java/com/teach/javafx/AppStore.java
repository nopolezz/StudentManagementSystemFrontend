package com.teach.javafx;


import com.teach.javafx.controller.base.MainFrameController;
import com.teach.javafx.request.JwtResponse;
public class AppStore {
    private static JwtResponse jwt;
    private static MainFrameController mainFrameController;
    private AppStore(){
    }

    public static JwtResponse getJwt() {
        return jwt;
    }

    public static void setJwt(JwtResponse jwt) {
        AppStore.jwt = jwt;
    }

    public static MainFrameController getMainFrameController() {
        return mainFrameController;
    }

    public static void setMainFrameController(MainFrameController mainFrameController) {
        AppStore.mainFrameController = mainFrameController;
    }
}
