package com.consumer.beans;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product
{
	long Id;
	String name;
	String descriptions;
	Date createdOn;
}
