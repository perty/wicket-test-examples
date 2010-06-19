package se.crisp.wicket.examples;

import org.apache.wicket.protocol.http.WebApplication;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see se.crisp.wicket.examples.TestExampleStarter#main(String[])
 */
public class WicketTestExamplesApplication extends WebApplication
{    
    /**
     * Constructor
     */
	public WicketTestExamplesApplication()
	{
	}
	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	public Class<HomePage> getHomePage()
	{
		return HomePage.class;
	}

}
