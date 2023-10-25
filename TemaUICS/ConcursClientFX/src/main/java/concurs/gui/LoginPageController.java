package concurs.gui;
import javafx.scene.Parent;
import model.OficiuPersoana;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.IServices;

import java.io.IOException;

public class LoginPageController {

    private IServices service;
    @FXML
    TextField userField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button loginButton;

    Parent mainParent;

    private HomePageController homePageController;


    public void setService(IServices service) {
        this.service = service;
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }
    public void setMainParent(Parent mainParent) {
        this.mainParent = mainParent;
    }

    @FXML
    void LoginButtonClicked() throws Exception {
        System.out.println("Clicked!!!");
        System.out.println(userField);
        String user = userField.getText();
        String password = passwordField.getText();
        if(user.isEmpty() || password.isEmpty()){
            System.out.println("Introduceti datele corespunzatoare");
            return;
        }
        try{
            service.login(user,password,homePageController);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        try{
            OficiuPersoana op = service.login(user,password,homePageController);
            System.out.println(op);
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("/homePage.fxml"));
//            AnchorPane root = loader.load();
//            HomePageController homePageController = loader.getController();
            homePageController.setService(service);
            homePageController.setOficiuPersoana(op);

            Scene scene =new Scene(mainParent, 730, 505);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Hi,"+op.getUser());
            stage.show();

            Stage thisStage = (Stage) loginButton.getScene().getWindow();
            thisStage.close();
        }catch (Exception e){
            System.out.println(e.getMessage());


        }
    }

}
