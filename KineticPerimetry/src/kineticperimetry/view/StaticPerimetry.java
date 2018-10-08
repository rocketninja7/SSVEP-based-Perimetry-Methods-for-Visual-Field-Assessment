package kineticperimetry.view;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import kineticperimetry.model.DegPixConverter;
import kineticperimetry.model.StimuliVector;

public class StaticPerimetry extends Stage {
	
	final static double limit=30;
	final static double separationAngle=5;
	final static double distToScreen=23.4;
	final static double tempFreq=0.01;
	final static int promptDuration=1000;
	
	int[] brightnessVector;
	
	Pane displayPane;
	
	Timeline timelineBoot;
	Timeline timelineStimulus;
	
	Random randomGenerator;
	
	ArrayList<StimuliVector> stimuliVectors;
	ArrayList<Integer> arrayListActiveStimuliIndices;
	
	StimuliVector currentlyDisplayedStimulus;
	
	boolean youCanRespondToStimulus;
	
	boolean procedureIsFinished;
	
	double fixX;
	double fixY;
	
	Animation currAnimation;
	
	int currDisplayedIndex;
	
	double response;
	
	int step;
	
	int step1Size;
	int step2Size;
	
	public StaticPerimetry() {
		youCanRespondToStimulus=false;
		randomGenerator=new Random();
		stimuliVectors=new ArrayList<>();
		arrayListActiveStimuliIndices=new ArrayList<>();
		
		displayPane=new Pane();
		displayPane.setMinWidth(Screen.getPrimary().getBounds().getWidth());
		displayPane.setMinHeight(Screen.getPrimary().getBounds().getHeight());
		displayPane.setStyle("-fx-background-color: hsb(" + 0 + ", " + 0 + "%, " + 0 + "%);");
		fixX=Screen.getPrimary().getBounds().getWidth()/2;
		fixY=Screen.getPrimary().getBounds().getHeight()/2;
		Ellipse fixationPoint=new Ellipse(fixX, fixY, 10, 10);
		fixationPoint.setFill(Color.WHITE);
		displayPane.getChildren().add(fixationPoint);
		Scene scene=new Scene(displayPane);
		setScene(scene);
		scene.addEventHandler(KeyEvent.KEY_PRESSED, ke -> {
			
			KeyCode option = ke.getCode();
			
			try {
				PrintWriter pw=new PrintWriter(new FileOutputStream("Events.txt", true));
				pw.println(System.currentTimeMillis()+", "+option);
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			if(option.equals(KeyCode.SPACE)) {
				if(youCanRespondToStimulus) {
					for (int i = 0; i < currentlyDisplayedStimulus.getAnswers().length; i++) {
                        if (currentlyDisplayedStimulus.getAnswers()[i] == 0) {
                            currentlyDisplayedStimulus.getAnswers()[i] = response;
                            break;
                        }
					}

                    youCanRespondToStimulus = false;
				}
			}
		});
		
		for(double i = -limit; i <= limit; i += separationAngle) {
			for(double j = -limit; j <= limit; j += separationAngle) {
				stimuliVectors.add(new StimuliVector(i, j, 1, 1, fixX, fixY, 3000, 100, distToScreen));
				arrayListActiveStimuliIndices.add(stimuliVectors.size() - 1);
			}
		}
		step1Size = stimuliVectors.size();
		step = 1;
		initAndRunBootTimeline();
	}
	
	private void initAndRunBootTimeline() {

        /* Init boot timeline */
        timelineBoot = new Timeline();

        /*
         * Create initial key frame (start) which is responsible for starting boot timeline
         * with 1000 ms delay after starting the procedure.
         */
        KeyFrame start = new KeyFrame(Duration.millis(1000), event -> {
            initAndRunStimulusTimeline();
            timelineBoot.stop();
        });
        timelineBoot.getKeyFrames().add(start);
        timelineBoot.play();
        
        try {
        	PrintWriter pw=new PrintWriter(new FileOutputStream("Events.txt", true));
			pw.println(System.currentTimeMillis()+", Procedure Started");
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }

    private void initAndRunStimulusTimeline() {

    	ParallelTransition pt = new ParallelTransition();//
    	
        /* Init stimulus timeline */
        timelineStimulus = new Timeline();

        /* Get random index of active stimulus */
        int r = randomGenerator.nextInt(arrayListActiveStimuliIndices.size());
        currDisplayedIndex = arrayListActiveStimuliIndices.get(r);
        currentlyDisplayedStimulus = stimuliVectors.get(currDisplayedIndex);

        /* Initialize cumulative time interval */
        int cumulativeTimeInterval = 0;
        
        KeyFrame keyFramePromptStimulus = new KeyFrame(Duration.millis(cumulativeTimeInterval), event -> {
        	Ellipse prompt = new Ellipse(currentlyDisplayedStimulus.getStartXPix(), currentlyDisplayedStimulus.getStartYPix(), currentlyDisplayedStimulus.getRadiusX(), currentlyDisplayedStimulus.getRadiusY());
        	int brightness = currentlyDisplayedStimulus.getBrightness();

            /* Create and set color for stimulus */
            Color color = Color.hsb(
                    0,
                    0,
                    (double) brightness / 100
            );
            
            prompt.setFill(color);
            prompt.setStroke(color);
            
            displayPane.getChildren().add(prompt);
            
            try {
            	PrintWriter pw=new PrintWriter(new FileOutputStream("Events.txt", true));
            	double xPix=DegPixConverter.convertDegToPixX(currentlyDisplayedStimulus.getStartXDeg()+response*(currentlyDisplayedStimulus.getEndXDeg()-currentlyDisplayedStimulus.getStartXDeg()), fixX, distToScreen);
            	double yPix=DegPixConverter.convertDegToPixY(currentlyDisplayedStimulus.getStartYDeg()+response*(currentlyDisplayedStimulus.getEndYDeg()-currentlyDisplayedStimulus.getStartYDeg()), fixY, distToScreen);
				pw.println(System.currentTimeMillis()+", Prompt: ("+xPix+", "+yPix+"), ("+currentlyDisplayedStimulus.getStartXDeg()+", "+currentlyDisplayedStimulus.getStartYDeg()+"), "+color);
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        });
        
        timelineStimulus.getKeyFrames().add(keyFramePromptStimulus);
        
        cumulativeTimeInterval += promptDuration;
        
        KeyFrame keyFrameRemovePrompt = new KeyFrame(Duration.millis(cumulativeTimeInterval), event -> {
        	if (displayPane.getChildren().size() > 1) {
            	try {
            		PrintWriter pw=new PrintWriter(new FileOutputStream("Events.txt", true));
            		pw.println(System.currentTimeMillis()+", Remove Prompt");
    				pw.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
            	
                displayPane.getChildren().remove(1);
            }
        });
        
        timelineStimulus.getKeyFrames().add(keyFrameRemovePrompt);
        
        cumulativeTimeInterval += 1000;

        /* Create KeyFrame for displaying stimulus */
        KeyFrame keyFrameDisplayStimulus = new KeyFrame(Duration.millis(cumulativeTimeInterval), event -> {

            /* Allow responding to stimulus */
            youCanRespondToStimulus = true;

            int brightness = currentlyDisplayedStimulus.getBrightness();

            /* Create and set color for stimulus */
            Color color = Color.hsb(
                    0,
                    0,
                    (double) brightness / 100
            );

            currentlyDisplayedStimulus.getShape().setFill(color);
            currentlyDisplayedStimulus.getShape().setStroke(color);

            /* Add stimulus to display pane */
            displayPane.getChildren().add(currentlyDisplayedStimulus.getShape());
            
            try {
            	PrintWriter pw=new PrintWriter(new FileOutputStream("Events.txt", true));
            	double xPix=DegPixConverter.convertDegToPixX(currentlyDisplayedStimulus.getStartXDeg()+response*(currentlyDisplayedStimulus.getEndXDeg()-currentlyDisplayedStimulus.getStartXDeg()), fixX, distToScreen);
            	double yPix=DegPixConverter.convertDegToPixY(currentlyDisplayedStimulus.getStartYDeg()+response*(currentlyDisplayedStimulus.getEndYDeg()-currentlyDisplayedStimulus.getStartYDeg()), fixY, distToScreen);
				pw.println(System.currentTimeMillis()+", Add: ("+xPix+", "+yPix+"), ("+currentlyDisplayedStimulus.getStartXDeg()+", "+currentlyDisplayedStimulus.getStartYDeg()+"), "+color);
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
            
            currAnimation = new Transition() {
            	{
            		setCycleDuration(Duration.millis(currentlyDisplayedStimulus.getDisplayTime()));
            		setInterpolator(Interpolator.LINEAR);
            	}
            	@Override
				protected void interpolate(double arg0) {
					currentlyDisplayedStimulus.getShape().setTranslateX(DegPixConverter.convertDegToPixX(currentlyDisplayedStimulus.getStartXDeg()+arg0*(currentlyDisplayedStimulus.getEndXDeg()-currentlyDisplayedStimulus.getStartXDeg()), fixX, distToScreen)-currentlyDisplayedStimulus.getStartXPix());
					currentlyDisplayedStimulus.getShape().setTranslateY(DegPixConverter.convertDegToPixY(currentlyDisplayedStimulus.getStartYDeg()+arg0*(currentlyDisplayedStimulus.getEndYDeg()-currentlyDisplayedStimulus.getStartYDeg()), fixY, distToScreen)-currentlyDisplayedStimulus.getStartYPix());
            		response = arg0;
            	}
            };
            currAnimation.play();
        });

        /* Add key frame to timeline */
        timelineStimulus.getKeyFrames().add(keyFrameDisplayStimulus);
        
        /* Update cumulative time interval */
        cumulativeTimeInterval += currentlyDisplayedStimulus.getDisplayTime();

        /* Create KeyFrame for removing stimulus from display pane */
        KeyFrame keyFrameRemoveStimulus = new KeyFrame(Duration.millis(cumulativeTimeInterval), event -> {
            if (displayPane.getChildren().size() > 1) {
            	try {
            		PrintWriter pw=new PrintWriter(new FileOutputStream("Events.txt", true));
            		pw.println(System.currentTimeMillis()+", Remove Stimulus");
    				pw.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
            	
                displayPane.getChildren().remove(1);
            }
        });

        /* Add key frame to timeline */
        timelineStimulus.getKeyFrames().add(keyFrameRemoveStimulus);

        /* Specify time interval between next stimulus display */
        int interval = 1000;

        /* Update cumulative time interval */
        cumulativeTimeInterval += interval;

        /* Create KeyFrame for time interval between stimuli */
        KeyFrame keyFrameIntervalBetweenStimuli = new KeyFrame(Duration.millis(cumulativeTimeInterval), event -> {
        	
        	pt.stop();

            /* Write negative answer to stimulus if there was no answer to it */
            if (youCanRespondToStimulus) {
            	for (int i = 0; i < currentlyDisplayedStimulus.getAnswers().length; i++) {
                    if (currentlyDisplayedStimulus.getAnswers()[i] == 0) {
                        currentlyDisplayedStimulus.getAnswers()[i] = 1;
                        break;
                    }
				}
                youCanRespondToStimulus = false;
            }
            
            boolean deactivateThisStimulus = true;
            for (int i = 0; i < currentlyDisplayedStimulus.getAnswers().length; i++) {
                if (currentlyDisplayedStimulus.getAnswers()[i] == 0) {
                    deactivateThisStimulus = false;
                    break;
                }
			}
            
            if(deactivateThisStimulus) {
            	arrayListActiveStimuliIndices.remove(new Integer(currDisplayedIndex));
            }
            
            if(arrayListActiveStimuliIndices.size()==0) {
            	/*if(step==1) {
            		Random random=new Random(System.currentTimeMillis());
            		while(arrayListActiveStimuliIndices.size() < 50) {
            			double tempX=random.nextDouble()*limit*2-limit, tempY=random.nextDouble()*limit*2-limit;
            			double sum = 0;
            			for(int i = 0; i < step1Size - 1; i++) {
            				double tempX1 = stimuliVectors.get(i).getStartXDeg()+stimuliVectors.get(i).getAnswer()*(stimuliVectors.get(i).getEndXDeg()-stimuliVectors.get(i).getStartXDeg());
            				double tempY1 = stimuliVectors.get(i).getStartYDeg()+stimuliVectors.get(i).getAnswer()*(stimuliVectors.get(i).getEndYDeg()-stimuliVectors.get(i).getStartYDeg());
            				double tempX2 = stimuliVectors.get(i+1).getStartXDeg()+stimuliVectors.get(i+1).getAnswer()*(stimuliVectors.get(i+1).getEndXDeg()-stimuliVectors.get(i+1).getStartXDeg());
            				double tempY2 = stimuliVectors.get(i+1).getStartYDeg()+stimuliVectors.get(i+1).getAnswer()*(stimuliVectors.get(i+1).getEndYDeg()-stimuliVectors.get(i+1).getStartYDeg());
            				if(ccw(tempX1, tempY1, tempX, tempY, tempX2, tempY2)) {
            					sum += angle(tempX1, tempY1, tempX, tempY, tempX2, tempY2);
            				}
            				else {
            					sum -= angle(tempX1, tempY1, tempX, tempY, tempX2, tempY2);
            				}
            			}
            			double tempX1 = stimuliVectors.get(step1Size - 1).getStartXDeg()+stimuliVectors.get(step1Size - 1).getAnswer()*(stimuliVectors.get(step1Size - 1).getEndXDeg()-stimuliVectors.get(step1Size - 1).getStartXDeg());
        				double tempY1 = stimuliVectors.get(step1Size - 1).getStartYDeg()+stimuliVectors.get(step1Size - 1).getAnswer()*(stimuliVectors.get(step1Size - 1).getEndYDeg()-stimuliVectors.get(step1Size - 1).getStartYDeg());
        				double tempX2 = stimuliVectors.get(0).getStartXDeg()+stimuliVectors.get(0).getAnswer()*(stimuliVectors.get(0).getEndXDeg()-stimuliVectors.get(0).getStartXDeg());
        				double tempY2 = stimuliVectors.get(0).getStartYDeg()+stimuliVectors.get(0).getAnswer()*(stimuliVectors.get(0).getEndYDeg()-stimuliVectors.get(0).getStartYDeg());
        				if(ccw(tempX1, tempY1, tempX, tempY, tempX2, tempY2)) {
        					sum += angle(tempX1, tempY1, tempX, tempY, tempX2, tempY2);
        				}
        				else {
        					sum -= angle(tempX1, tempY1, tempX, tempY, tempX2, tempY2);
        				}
            			if(sum < 0) {
            				sum = -sum;
            			}
            			if(sum - 2 * Math.PI < eps && 2 * Math.PI - sum < eps) {
            				stimuliVectors.add(new StimuliVector(tempX, tempY, fixX, fixY, 1000, 100, distToScreen));
            				arrayListActiveStimuliIndices.add(stimuliVectors.size()-1);
            			}
            		}
            		step2Size = stimuliVectors.size();
            	}
            	else if(step == 2) {
            		for(int i = step1Size; i < step2Size; i ++) {
            			if(stimuliVectors.get(i).getAnswer() == 1) {
            				for(double j=0;j<360;j+=120) {
            					double tempX = stimuliVectors.get(i).getStartXDeg()+stimuliVectors.get(i).getAnswer()*(stimuliVectors.get(i).getEndXDeg()-stimuliVectors.get(i).getStartXDeg());
            					double tempY = stimuliVectors.get(i).getStartYDeg()+stimuliVectors.get(i).getAnswer()*(stimuliVectors.get(i).getEndYDeg()-stimuliVectors.get(i).getStartYDeg());
            					stimuliVectors.add(new StimuliVector(tempX, tempY, tempX+limit*Math.cos(j/180*Math.PI), tempY+limit*Math.sin(j/180*Math.PI), fixX, fixY, 3, 100, distToScreen));
            					arrayListActiveStimuliIndices.add(stimuliVectors.size()-1);
            				}
            			}
            		}
            	}
            	else {*/
            		procedureIsFinished=true;
            		try {
                    	PrintWriter pw=new PrintWriter(new FileOutputStream("Events.txt", true));
        				pw.println(System.currentTimeMillis()+", Procedure Complete");
        				pw.close();
        			} catch (FileNotFoundException e) {
        				e.printStackTrace();
        			}
            		StaticResults staticResults = new StaticResults(limit, separationAngle, fixX, fixY, distToScreen, stimuliVectors);
            		staticResults.show();
            	/*}
            	step++;*/
            }
            /* Run next stimulus display */
            if (!procedureIsFinished) {
                initAndRunStimulusTimeline();
            }
        });

        /* Add key frame to timeline */
        timelineStimulus.getKeyFrames().add(keyFrameIntervalBetweenStimuli);
        
		pt.getChildren().add(timelineStimulus);//

        /* Play timeline */
        //timelineStimulus.play();
		
        /*EFFECT 1 (FADING IN AND OUT)*/
        /*
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.01), currentlyDisplayedStimulus.getShape());
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        //fadeTransition.play();
        */
        
        /*EFFECT 1.5 (BLINKING USING FADING)*/
		/*
        double SMALL_DURATION = 0.01;
        double SECONDS_PER_ROUND = 1; //edit this
        
        FadeTransition ft1 = new FadeTransition(Duration.seconds(SMALL_DURATION), currentlyDisplayedStimulus.getShape());
        ft1.setFromValue(1.0);
        ft1.setToValue(0.0);
        
        FadeTransition ft2 = new FadeTransition(Duration.seconds(SECONDS_PER_ROUND-SMALL_DURATION), currentlyDisplayedStimulus.getShape());
        ft2.setFromValue(1.0);
        ft2.setToValue(1.0);
        
        SequentialTransition st = new SequentialTransition();
        
        st.getChildren().add(ft1);
		st.getChildren().add(ft2);
		
        st.setCycleCount(Animation.INDEFINITE);
        
        st.play();
        */
		
        /*EFFECT 2 (BLINKING)*/
        
        Timeline timelineBlink = new Timeline(new KeyFrame(Duration.millis(1/tempFreq), evt -> {
        	currentlyDisplayedStimulus.getShape().setVisible(false);
        	try {
            	PrintWriter pw=new PrintWriter(new FileOutputStream("Events.txt", true));
				pw.println(System.currentTimeMillis()+", Stimulus Disappear");
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        }),
        new KeyFrame(Duration.millis(2/tempFreq), evt -> {
            currentlyDisplayedStimulus.getShape().setVisible(true);
            try {
            	PrintWriter pw=new PrintWriter(new FileOutputStream("Events.txt", true));
				pw.println(System.currentTimeMillis()+", Stimulus Appear");
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        }));
        timelineBlink.setCycleCount(Animation.INDEFINITE);
		
		pt.getChildren().add(timelineBlink);
		pt.play();
    }
}
