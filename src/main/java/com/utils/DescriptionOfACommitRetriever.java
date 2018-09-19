package com.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import com.bitcoin.object.IConfiguration;
import com.bitcoin.object.IPersonalInformation;

/**
 * Create a simple Java client to send “GET” request
 * 
 * Link:
 * https://www.mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class DescriptionOfACommitRetriever {
	// Full description of a commit
	private String response = "";

	private String sha = "";

	public static void main(String[] args) {
		DescriptionOfACommitRetriever retriever = new DescriptionOfACommitRetriever();
		retriever.setSha("14b29a77acb2ea7ebc5c9d08ace1ac5e08013384");
		retriever.retrieveDescription();
		String response = retriever.getResponse();
		System.out.println(response);
	}

	public DescriptionOfACommitRetriever() {
	}

	public String retrieveDescription() {
		if (sha != null && sha.length() > 0)
			try {
				String usernameColonPassword = IPersonalInformation.USERNAME + ":" + IPersonalInformation.PASSWORD;
				String basicAuthPayload = "Basic "
						+ Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());

				URL url = new URL(IConfiguration.BASE_COMMIT_URL + sha);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				// Include the HTTP Basic Authentication payload
				conn.addRequestProperty("Authorization", basicAuthPayload);

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				String output;
				while ((output = br.readLine()) != null) {
					response += output + "\n";
				}

				conn.disconnect();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return response;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public String getSha() {
		return sha;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
