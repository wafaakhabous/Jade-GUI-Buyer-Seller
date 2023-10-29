package org.example.agents.buyer;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//container de cet agent
public class BookBuyerGui extends Application {
    protected BookBuyerAgent bookBuyerAgent;
    protected ListView<String> listMsg;
    protected  ObservableList<String> observableList;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        startContainer();
        primaryStage.setTitle("Buy a Product");
        BorderPane borderpane=new BorderPane();
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));

        observableList= FXCollections.observableArrayList();
        listMsg=new ListView<String>(observableList);

        vBox.getChildren().add(listMsg);
        borderpane.setCenter(vBox);
        Scene scene=new Scene(borderpane,600,400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void startContainer() {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl(false);
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);
        try {
            AgentController agentController = agentContainer
                    .createNewAgent("BookBuyerAgent","org.example.agents.buyer.BookBuyerAgent",new Object[]{this});// au moment du deploiement on transmet a l'agent une reference vers l'interface
            agentController.start();
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        }
    }

    public void logMessage(ACLMessage aclmessage)//si buyer agent veut log'er qlq chose a l'interface il ne fait que appeler cette methode
    {
        Platform.runLater(()->
        {
            observableList.add(aclmessage.getSender().getName()+" => "+aclmessage.getContent());
        });
        //afficher un message dans la liste view

    }
}
