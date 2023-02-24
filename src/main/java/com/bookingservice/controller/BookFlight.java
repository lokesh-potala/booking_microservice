package com.bookingservice.controller;


//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bookingservice.dto.BookMainReq;
import com.bookingservice.dto.BookingResponse;
import com.bookingservice.exception.FareNotPresent;
import com.bookingservice.service.BookingService;
import com.bookingservice.service.Impl.FareValidateService;

@RestController
@RequestMapping("/bookingservice")
public class BookFlight{

	@Autowired(required = true)
	BookingService bookingService;
	
	@Autowired(required = true)
	FareValidateService fareChecking;
	
//	@Autowired
//    private JmsTemplate jmsTemplate;
	
	BookingResponse bookResponse;

	Logger logger = LoggerFactory.getLogger(BookFlight.class);
	
	//Creating new Booking to the flight
	@PostMapping
	@RequestMapping(value = "/newbooking",method = RequestMethod.POST)
	public ResponseEntity<BookingResponse> newBooking(@RequestBody BookMainReq BookingData) {
		logger.info("calling controller and new booking is start: ");
		
		bookResponse = bookingService.newBooking(BookingData);
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//		try {
//			byte[] json = objectMapper.writeValueAsBytes(bookResponse);
//			jmsTemplate.convertAndSend("booking", json);
//			URL url = new URL("https://checkinmicroservice.azurewebsites.net/Api/Queue/Booking"); 
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
//			connection.setRequestMethod("POST"); 
//			int responseCode = connection.getResponseCode();
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		int fareResult = fareChecking.validatingFare(BookingData);
		if(fareResult == 1) {
			logger.info("new booking is successfull: ");
		}
		else {
			logger.error("new booking is on hold because of fare issues: ");
			throw new FareNotPresent("The given data did not have fare amount or it is 0.0, Details are saved in Records as 'Status : Pending with Fare amount'");
		}
		
		logger.info("In controller new booking is end: ");
		return new ResponseEntity<BookingResponse>(bookResponse,HttpStatus.OK);
	}
	
}