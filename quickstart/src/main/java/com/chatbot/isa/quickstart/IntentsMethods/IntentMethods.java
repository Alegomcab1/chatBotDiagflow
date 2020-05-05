package com.chatbot.isa.quickstart.IntentsMethods;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.Lists;
import com.google.cloud.dialogflow.v2.Context;
import com.google.cloud.dialogflow.v2.EntityType;
import com.google.cloud.dialogflow.v2.EntityType.Kind;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.Intent.Message;
import com.google.cloud.dialogflow.v2.Intent.Message.Text;
import com.google.cloud.dialogflow.v2.Intent.TrainingPhrase;
import com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part;
import com.google.cloud.dialogflow.v2.IntentName;
import com.google.cloud.dialogflow.v2.IntentsClient;
import com.google.cloud.dialogflow.v2.ProjectAgentName;

public class IntentMethods {

	
	public static Intent createIntent(String displayName, String projectId, List<String> trainingPhrasesParts, List<String> messageTexts) throws Exception {
		
		  // Instantiates a client
		  try (IntentsClient intentsClient = IntentsClient.create()) {
			  
		    // Set the project agent name using the projectID (my-project-id)
		    ProjectAgentName parent = ProjectAgentName.of(projectId);
		    
		    
		    // Define the parts of each phrase
		    
		    /*Part part1 = Part.newBuilder().setText("I want to cancel a card ended in  ").build();
	        Part part2 = Part.newBuilder().setText("5123").setEntityType("@cardNumber").setAlias("cardNumber")
	                .setUserDefined(true).build();
	        List<Part> parts = new ArrayList<Part>();
	        parts.add(part1);
	        parts.add(part2);*/
		    
		    // Build the trainingPhrases from the trainingPhrasesParts
		    List<TrainingPhrase> trainingPhrases = new ArrayList<>();
		    for (String trainingPhrase : trainingPhrasesParts) {
		      trainingPhrases.add(
		          TrainingPhrase.newBuilder().addParts(
		              Part.newBuilder().setEntityType("@sys.geo-city").setText(trainingPhrase).build())
		              .build());
		    }

		    // Build the message texts for the agent's response
		    Message message = Message.newBuilder()
		        .setText(
		            Text.newBuilder()
		                .addAllText(messageTexts).build()
		        ).build();

		    // Build the intent
		    Intent intent = Intent.newBuilder()
		        .setDisplayName(displayName)
		        .addMessages(message)
		        .addAllTrainingPhrases(trainingPhrases)
		        .build();

		    // Performs the create intent request
		    Intent response = intentsClient.createIntent(parent, intent);
		    System.out.format("Intent created: %s\n", response);

		    return response;
		  }
	 }
	
	
	
	public static List<Intent> listIntents(String projectId) throws Exception {
		  List<Intent> intents = Lists.newArrayList();
		  // Instantiates a client
		  try (IntentsClient intentsClient = IntentsClient.create()) {
		    // Set the project agent name using the projectID (my-project-id)
		    ProjectAgentName parent = ProjectAgentName.of(projectId);

		    // Performs the list intents request
		    for (Intent intent : intentsClient.listIntents(parent).iterateAll()) {
		      System.out.println("====================");
		      System.out.format("Intent name: '%s'\n", intent.getName());
		      System.out.format("Intent display name: '%s'\n", intent.getDisplayName());
		      System.out.format("Action: '%s'\n", intent.getAction());
		      System.out.format("Root followup intent: '%s'\n", intent.getRootFollowupIntentName());
		      System.out.format("Parent followup intent: '%s'\n", intent.getParentFollowupIntentName());

		      System.out.format("Input contexts:\n");
		      for (String inputContextName : intent.getInputContextNamesList()) {
		        System.out.format("\tName: %s\n", inputContextName);
		      }
		      System.out.format("Output contexts:\n");
		      for (Context outputContext : intent.getOutputContextsList()) {
		        System.out.format("\tName: %s\n", outputContext.getName());
		      }

		      intents.add(intent);
		    }
		  }
		  return intents;
	}
	
	
	
	public static void deleteIntent(String intentId, String projectId) throws Exception {
		  // Instantiates a client
		  try (IntentsClient intentsClient = IntentsClient.create()) {
		    IntentName name = IntentName.of(projectId, intentId);
		    // Performs the delete intent request
		    intentsClient.deleteIntent(name);
		  }
	}
	
	
	public static List<String> getIntentIds(String displayName, String projectId) throws Exception {
	    List<String> intentIds = new ArrayList<>();

	    // Instantiates a client
	    try (IntentsClient intentsClient = IntentsClient.create()) {
	      ProjectAgentName parent = ProjectAgentName.of(projectId);
	      for (Intent intent : intentsClient.listIntents(parent).iterateAll()) {
	        if (intent.getDisplayName().equals(displayName)) {
	          String[] splitName = intent.getName().split("/");
	          intentIds.add(splitName[splitName.length - 1]);
	        }
	      }
	    }

	    return intentIds;
	}
	
}
