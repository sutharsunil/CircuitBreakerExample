package com.consumer.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.consumer.beans.Product;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class ConsumerForProduct
{
	@Value("server.port")
	String serverPort;
	@Value("spring.application.name")
	String applicationName;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
//	private DiscoveryClient discoveryClient;
	
	@RequestMapping(value = "/",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public String helloWorld()
	{
		return "Hello Form " + applicationName;
	}
	
	@RequestMapping(value = "/consumer/products/",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	@HystrixCommand(fallbackMethod = "getConsumerProductsFallback",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds" ,value = "3000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold" ,value = "6"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage" ,value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds" ,value = "5000")
			}
			)
	public List<Product> getConsumerProducts()
	{
	  	return restTemplate.getForObject("http://product-service-provider/products/", List.class);
	}
	
	
	public List<Product> getConsumerProductsFallback()
	{
	  	return Arrays.asList(
	  			new Product(-1, "Default", "Default descriptions", new Date())	  			
	  			);
	}
	
	
}
