package com.katatds.springbtdemo.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
import org.springframework.stereotype.Component;

import com.katatds.springbtdemo.model.InputData;

@Component
public class MowItemProcessor implements ItemProcessor<InputData, InputData> {

	public static final Logger logger = LoggerFactory.getLogger(MowItemProcessor.class);

	private int mowX = -1;
	private int mowY = -1;
	private String orientation = null;
	
    private ExecutionContext executionContext;
	
	@BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        executionContext = stepExecution.getExecutionContext();
    }
	
	@AfterStep
    public void afterStep(StepExecution stepExecution) {
		executionContext.put("x", null);
		executionContext.put("y", null);
    }
	
	@Override
	public InputData process(InputData inputData) throws Exception {
		String[] splittedLine = inputData.getLine().split(" ");
		if(executionContext.get("x") == null && executionContext.get("y") == null) {
			if(splittedLine.length == 2) {  
				executionContext.putInt("x",  Integer.parseInt(splittedLine[0]));
				executionContext.putInt("y",  Integer.parseInt(splittedLine[1]));
			}
			if(executionContext.getInt("x") < 0 || executionContext.getInt("y") < 0) {
				logger.error(inputData.toString());
				throw new FlatFileFormatException("Incorrect format","bloking");
			}
			throw new FlatFileFormatException("Skip first line of file");
		}
		if(orientation == null) {
			if(splittedLine.length == 3) {
				mowX = Integer.parseInt(splittedLine[0]);
				mowY = Integer.parseInt(splittedLine[1]);
				orientation = splittedLine[2];
			}
			if(mowX <0 || mowX > executionContext.getInt("x") || mowY < 0 || mowY > executionContext.getInt("y") || !validOrientation(orientation)){
				logger.error(inputData.toString());
				throw new FlatFileFormatException("Incorrect format","bloking");
			}
			throw new FlatFileFormatException("Skip first line of mow");
		}
		if(splittedLine.length == 1 && !validControl(inputData.getLine())) {
			throw new FlatFileFormatException("Incorrect format","bloking");
		} 
		inputData.setOrientation(orientation);
		inputData.setCoordX(mowX);
		inputData.setCoordY(mowY);
		inputData.setControl(inputData.getLine());
		for (int i = 0; i < inputData.getControl().length(); i++) {
			
			char c = inputData.getControl().charAt(i);
			if (c == 'D') {
				switch (inputData.getOrientation()) {
					case "N" -> inputData.setOrientation("E");
					case "E" -> inputData.setOrientation("S");
					case "S" -> inputData.setOrientation("W");
					case "W" ->inputData.setOrientation("N");
				}
			}
			if (c == 'G') {
				switch (inputData.getOrientation()) {
					case "N" ->	inputData.setOrientation("W");
					case "W" ->	inputData.setOrientation("S");
					case "S" -> inputData.setOrientation("E");
					case "E" -> inputData.setOrientation("N");
				}
			}
			if (c == 'A') {
				switch(inputData.getOrientation()){
					case("N") -> {if(inputData.getCoordY() < executionContext.getInt("y")) inputData.setCoordY(inputData.getCoordY() + 1);}
					case("S") -> {if( inputData.getCoordY() > 0) inputData.setCoordY(inputData.getCoordY() - 1);}
					case("E") -> {if(inputData.getCoordX() < executionContext.getInt("x")) inputData.setCoordX(inputData.getCoordX() + 1);}
					case("W") -> {if(inputData.getCoordX() > 0) inputData.setCoordX(inputData.getCoordX() - 1);}
				}
			}
		}
		orientation = null; 
		return inputData;
	}
	
	private boolean validControl(String control) {
		if(control == null) {
			return false;
		}
		// Accept only N,E,W,S,D,G,A letters
        String motif = "^[DGA]+$";
        // create Pattern
        Pattern pattern = Pattern.compile(motif);
        // create Matcher
        Matcher matcher = pattern.matcher(control);
        // Check if string matches pattern
        return matcher.matches();
    }
	
	private boolean validOrientation(String orientation) {
		if(orientation == null) {
			return false;
		}
		// Accept only N,E,W,S,D,G,A letters
        String motif = "^[NEWS]+$";
        // create Pattern
        Pattern pattern = Pattern.compile(motif);
        // create Matcher
        Matcher matcher = pattern.matcher(orientation);
        // Check if string matches pattern
        return matcher.matches();
    }
}