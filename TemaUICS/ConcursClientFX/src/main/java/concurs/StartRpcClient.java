package concurs;

import concurs.gui.HomePageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import services.IServices;
import networking.rpcprotocol.ChatServicesRpcProxy;
import concurs.gui.LoginPageController;

import java.io.IOException;
import java.util.Properties;


public class StartRpcClient extends Application {
    private static int defaultConcursPort=55555;
    private static String defaultServer="localhost";
    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProps=new Properties();
        try {
            clientProps.load(StartRpcClient.class.getResourceAsStream("/concursclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find bd.config.properties "+e);
            return;
        }
        String serverIP=clientProps.getProperty("concurs.server.host",defaultServer);
        int serverPort=defaultConcursPort;
        try{
            serverPort=Integer.parseInt(clientProps.getProperty("concurs.server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number "+ex.getMessage());
            System.out.println("Using default port: "+defaultConcursPort);
        }
        System.out.println("Using server IP "+serverIP);
        System.out.println("Using server port "+serverPort);
        IServices server= (IServices)  new ChatServicesRpcProxy(serverIP, serverPort);


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("loginPage.fxml"));
        Parent root=loader.load();

        LoginPageController loginCtrl = loader.<LoginPageController>getController();
        loginCtrl.setService(server);

        FXMLLoader mainLoader = new FXMLLoader(getClass().getClassLoader().getResource("homePage.fxml"));
        Parent croot= mainLoader.load();

        HomePageController mainCtrl = mainLoader.<HomePageController>getController();

        loginCtrl.setHomePageController(mainCtrl);
        loginCtrl.setMainParent(croot);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
