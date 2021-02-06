package com.helius.dao;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MyDtoNullKeySerializer extends JsonSerializer<Object> {

	@Override
	  public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused) 
	      throws IOException, JsonProcessingException
	  {
	    jsonGenerator.writeFieldName("-");
	  }

}