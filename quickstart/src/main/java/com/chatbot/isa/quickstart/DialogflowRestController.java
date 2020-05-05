package com.chatbot.isa.quickstart;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatbot.isa.quickstart.IntentsMethods.IntentMethods;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.TextInput.Builder;

@RestController
public class DialogflowRestController {

    private String userText = "hola";
    private final String LANG_CODE = "es";
    private final String PROJECT_ID = "newagent-2-mlrjmr";
    private String sessionId = UUID.randomUUID().toString();
    //private final String credential = "/credentials/newagent-2-mlrjmr-a6567bf12560.json";
    private final String URL = "https://dialogflow.googleapis.com/v2/{session=projects/MY_PROJECT_ID/agent/sessions/" +
            sessionId + "}:detectIntent";

    @RequestMapping("/intent")
    public String doThings() throws Exception {
    	   	
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            SessionName session = SessionName.of(PROJECT_ID, sessionId);
            
            List<String> trainingPhrasesParts = new ArrayList<String>();
            List<String> messageTexts = new ArrayList<String>();
            
            trainingPhrasesParts.add("Hola Madrid");
            trainingPhrasesParts.add("adios Barcelona");
            trainingPhrasesParts.add("fafa Cordoba");
            trainingPhrasesParts.add("wed Sevilla macarrones");
            trainingPhrasesParts.add("equisqcu mamba en Almeria");
            
            messageTexts.add("hola que tal");
            
            Intent ejemplo = IntentMethods.createIntent("3xaMpl32", PROJECT_ID, trainingPhrasesParts, messageTexts);
            
            Builder textInput = TextInput.newBuilder().setText(userText).setLanguageCode(LANG_CODE);
            
            
            
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
            
            
            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            
           
            
            return response.toString();
        }
    }
}