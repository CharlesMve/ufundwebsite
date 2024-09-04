package com.ufund.api.ufundapi.TenPercentEnhanment;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.ufund.api.ufundapi.model.Need;

public class CodeThatShareButtonShouldRun {

    private static String body;
	public static TweetingClass tweetingClass;
    private Need need;

    public CodeThatShareButtonShouldRun(Need need){
        this.need= need;
        CodeThatShareButtonShouldRun.tweetingClass = new TweetingClass();
        this.body = "";
    }


    public void run() {
			try {
				HttpRequest nullCheck = tweetingClass.makeRequest(this.need);
				HttpResponse<String> response = tweetingClass.getClient().send(nullCheck,HttpResponse.BodyHandlers.ofString());
				body = response.body();
				if (response.statusCode() == 201){
					System.out.println("Yay");
				} else if (response.statusCode() == 401) {
					System.out.println("Nay");
				} else {
					System.out.println("IDK");
				}
				System.out.println(response.body());
			} catch (Exception e) {
				System.err.println(e);
			}
		}
}
