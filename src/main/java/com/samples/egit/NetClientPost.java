package com.samples.egit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Create a simple Java client to send “GET” request
 * 
 * Link:
 * https://www.mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class NetClientPost {
	
	public static void main(String[] args) {
		try {
			URL url = new URL(
					"https://api.github.com/repos/bitcoin/bitcoin/commits/cccc362d62b0ba9475b1ac47d5908f77f7eb5d21");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}
}
