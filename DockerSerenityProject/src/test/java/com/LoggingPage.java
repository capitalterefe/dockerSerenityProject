package com;

import utilities.Base;
import static org.junit.Assert.assertTrue;

public class LoggingPage extends Base{
	

	public void loggingInto(){
		System.out.println("Logging into App");
		driver.get("http://www.google.com");
	}

	public void failMeNow() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue("failing now",true);
	}
}
