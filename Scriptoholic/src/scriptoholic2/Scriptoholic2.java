/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptoholic2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author bryant
 */
public class Scriptoholic2 extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        TextArea txtConsole = new TextArea();
        TextArea txtScript = new TextArea();

        btn.setText("Execute");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String script = txtScript.getText();
                System.out.println("Executing " + script);
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(txtConsole);
        root.getChildren().add(txtScript);
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
