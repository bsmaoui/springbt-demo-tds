package com.katatds.springbtdemo.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.katatds.springbtdemo.model.InputData;

@Component
public class MowItemWriter implements ItemWriter<InputData>{

	 public static final Logger logger = LoggerFactory.getLogger(MowItemWriter.class);
	 
	@Override
	public void write(Chunk<? extends InputData> inputData) throws Exception {
		inputData.getItems().stream().filter(i-> i.getOrientation() != null && !i.getOrientation().isEmpty()).forEach( i->{
			StringBuilder sb = new StringBuilder();
			sb.append("X= ").append(i.getCoordX()).append(" , Y = ").append(i.getCoordY()).append(" , Orientation = ").append(i.getOrientation());
			System.out.println(sb.toString());
		});
	}



}
