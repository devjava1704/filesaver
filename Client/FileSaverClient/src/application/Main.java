package application;

import java.io.File;
import java.io.IOException;

import application.utils.ServerUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application{
	private static Stage primaryStage;
	private BorderPane rootLayout;
	
	
	@Override
	public void start(Stage primaryStage) {
		Main.primaryStage=primaryStage;
		Main.primaryStage.setTitle("test");
		initLayout();
		
		ServerUtils server= new ServerUtils("192.168.178.108", 7002);
		
		
		server.uploadFile(new File("/home/mauro/Scrivania/server.py"));


		
		server.disconnect();


	}
	
	public void initLayout(){
		try{
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/layout.fxml"));
			rootLayout=(BorderPane) loader.load();
			Scene scene=new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	public static Stage getPrimaryStage() {
        return primaryStage;
    }
	public static void main(String[] args){
		launch(args);
	}

}