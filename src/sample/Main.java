package sample;

import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    Label dirLabel = new Label("root folder: ");
    TextField dirTextInput = new TextField("/home/bartek/workspace");
    Button submit = new Button("Run");
    int NumOfThreads = 10;
    ArrayList<Label> resultLabels = new ArrayList<>(NumOfThreads);
    String dir = dirTextInput.getText();

    @Override
    public void start(Stage primaryStage) throws Exception{
        setCallbacks();
        primaryStage.setTitle("File counter");
        primaryStage.setScene(getScene());
        primaryStage.show();
    }

    private Scene getScene(){
        BorderPane borderPane = new BorderPane();
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(25,25,25,25));
        pane.add(dirTextInput, 1, 1);
        pane.add(dirLabel, 0, 1);
        pane.add(submit, 0, 2);
        for (int i = 0; i < NumOfThreads; i++){
            Label label = new Label(i + " - Num of files: 0");
            resultLabels.add(label);
            pane.add(label, 1, 2 + i);
        }
        borderPane.setCenter(pane);
        return new Scene(borderPane, 300, 275);
    }

    private void setCallbacks(){
        dirTextInput.textProperty().addListener((observable, oldValue, newValue) -> dir = newValue);

        submit.setOnAction(e -> {
            for(int i = 0; i < NumOfThreads; i++){
                runThread(i);
            }
        });
    }

    private void runThread(final int threadId){
        new Thread(
            () -> {
                int files = getFilesCount(dir);
                updateResultLabel(threadId, files);
            }
        ).start();
    }

    private void updateResultLabel(final int threadId, final int files){
        Platform.runLater(() -> resultLabels.get(threadId).setText(
            threadId + " - Num of files: " + files));
    }

    private int getFilesCount(String dir){
        File directory= new File(dir);
        return Utils.listFilesForFolderWithSemaphore(directory);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
