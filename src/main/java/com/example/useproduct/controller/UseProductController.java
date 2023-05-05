package com.example.useproduct.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="/useproduc")
public class UseProductController {

	@Autowired
	RestTemplate rest;

	@GetMapping(value = "/getproductwithtax")
	public List<Product> getProductWithTax() {

		String url1 = "http://localhost:8080/pro/getall";
		String url2 = "http://localhost:8081/gst/getpercentagebyhsn/";

		ResponseEntity<List<Product>> response1 = rest.exchange(url1, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Product>>() {
				});

		List<Product> pros = response1.getBody();
		for (Product p : pros) {
			int temp = p.getHsn();
			ResponseEntity<Integer> response2 = rest.exchange(url2 + temp, HttpMethod.GET, null, Integer.class);
			int percentage = response2.getBody();
			p.setPrice(p.getPrice() + (p.getPrice() * percentage / 100));
		}

		return pros;
	}

}
