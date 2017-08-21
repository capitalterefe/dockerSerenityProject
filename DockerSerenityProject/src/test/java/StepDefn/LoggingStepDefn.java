package StepDefn;

import com.LoggingPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import utilities.Base;

public class LoggingStepDefn extends Base{
	LoggingPage loggingPage=new com.LoggingPage();
	@Given("^i am logged into$")
	public void i_am_logged_into()  {
		loggingPage.loggingInto();
		
	}
	@Then("^i fail the test$")
	public void ifailTheTest()  {
		loggingPage.failMeNow();
		
	}
}
