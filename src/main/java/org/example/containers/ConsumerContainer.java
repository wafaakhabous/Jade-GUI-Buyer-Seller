package org.example.containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.example.agents.ConsumerAgent;

public class ConsumerContainer extends Application{
    //chaque fois qu'un event se produit de l'interface graphique on a besoin de contacter l'agent
    protected ConsumerAgent consumeragent;
    public static void main(String[] args) {
        launch(args);
    }

    public void startContainer()
    {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl(false);
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);
        try {
            AgentController agentController = agentContainer
                    .createNewAgent("consumer","org.example.agents.ConsumerAgent",new Object[]{this});// au moment du deploiement on transmet a l'agent une reference vers l'interface
            agentController.start();
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startContainer();
        primaryStage.setTitle("Buy a Product");
        BorderPane borderpane=new BorderPane();
        Label label = new Label("Product name :");
        TextField textField = new TextField();
        Button button = new Button("Search :");
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(label,textField,button);

        ObservableList<String>  observableList= FXCollections.observableArrayList();
        ListView<String> listMsg=new ListView<>(observableList);
        vBox.getChildren().add(listMsg);

        borderpane.setTop(hBox);
        borderpane.setCenter(vBox);
        Scene scene=new Scene(borderpane,600,400);
        primaryStage.setScene(scene);
        primaryStage.show();

        button.setOnAction(evt->{
            String livre=textField.getText();
            //observableList.add(livre);
            //on veut passer le livre comme message ms seul l'agent peut envoyer un message
            //=> on le transmet à l'agent ConsumerAgent
            GuiEvent event= new GuiEvent(this,1);
            event.addParameter(livre);
            consumeragent.onGuiEvent(event);

        });
    }

    public ConsumerAgent getConsumeragent() {
        return consumeragent;
    }

    public void setConsumeragent(ConsumerAgent consumeragent) {
        this.consumeragent = consumeragent;
    }
}
