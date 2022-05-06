package multiclients;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class FXMLDocumentBase extends BorderPane {

    protected final TextArea comingText;
    protected final FlowPane flowPane;
    protected final TextField outingText;
    protected final Button sendBTN;
    Socket socket ;
    DataInputStream dis;
    PrintStream dos;
    public FXMLDocumentBase() {

        comingText = new TextArea();
        flowPane = new FlowPane();
        outingText = new TextField();
        sendBTN = new Button();

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(400.0);
        setPrefWidth(600.0);

        BorderPane.setAlignment(comingText, javafx.geometry.Pos.CENTER);
        comingText.setEditable(false);
        comingText.setPrefHeight(200.0);
        comingText.setPrefWidth(200.0);
        setCenter(comingText);

        BorderPane.setAlignment(flowPane, javafx.geometry.Pos.CENTER);
        flowPane.setPrefHeight(40.0);
        flowPane.setPrefWidth(600.0);

        outingText.setPrefHeight(25.0);
        outingText.setPrefWidth(470.0);

        sendBTN.setMnemonicParsing(false);
        sendBTN.setPrefHeight(25.0);
        sendBTN.setPrefWidth(80.0);
        sendBTN.setText("Send");
        setBottom(flowPane);

        flowPane.getChildren().add(outingText);
        flowPane.getChildren().add(sendBTN);
        
        try {
            socket= new Socket("127.0.0.1",5005);
            dis= new DataInputStream(socket.getInputStream());
            dos=new PrintStream(socket.getOutputStream());
            new Thread(){
                public void run(){
                while(true)
                {
                    
                    try {
                        String msg = dis.readLine();
                        comingText.appendText(msg+"\n");
                        
                    }
                    catch(SocketException e) {
                        try {
                            dis.close();
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLDocumentBase.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentBase.class.getName()).log(Level.SEVERE, null, ex);
                        
                    }
                   
                }
                }
            
            }.start();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      sendBTN.addEventHandler(ActionEvent.ACTION,new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
             //  System.out.println("Hi");
                if(!outingText.getText().isEmpty()){
                    dos.println(outingText.getText());
                    outingText.clear();
                }
            }
        });  
        
        
        

    }
}
