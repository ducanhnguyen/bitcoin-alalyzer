package com.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

	public static void main(String[] args) {
		System.out.println(Utils.removeComments(""));
		System.out.println(Utils.removeComments("//fddfs"));
		System.out.println(Utils.removeComments("/*fddfs*/"));
		System.out.println(Utils.removeComments("int a= 0; //fsdf stwert fsf\nint a=1;"));
		System.out.println(Utils.removeComments("int a= 0; /*asd fsl*/int a=1;"));
		System.out.println(Utils.removeComments("int a= 0; /*asd fs \njksfh sdg l*/int a=1;"));
	}

	public static String removeQuote(String src) {
		String output = "";

		boolean isQuoteType1 = false; // using '
		boolean isQuoteType2 = false; // using "

		src = " " + src;
		char[] srcs = src.toCharArray();

		for (int i = 1; i <= srcs.length - 1; i++) {
			char previous = srcs[i - 1];
			char current = srcs[i];

			// type 1
			if (!isQuoteType2 && current == '\"' && previous != '\\') {
				isQuoteType2 = true;
				continue;
			}

			if (isQuoteType2 && current == '\"' && previous == '\\')
				continue;

			if (isQuoteType2 && current == '\"' && previous != '\\') {
				isQuoteType2 = false;
				continue;
			}

			// type 2

			if (isQuoteType1 && current == '\'') {
				isQuoteType1 = false;
				continue;
			}
			if (!isQuoteType1 && current == '\'') {
				isQuoteType1 = true;
				continue;
			}

			if (!isQuoteType1 && !isQuoteType2)
				output += current;
		}

		return output;
	}

	public static String removeComments(String src) {
		String output = "";

		boolean isInsideCommentType1 = false; // using //
		boolean isInsideCommentType2 = false; // using /*... */

		src = " " + src + " ";
		char[] srcs = src.toCharArray();

		for (int i = 1; i < srcs.length - 1; i++) {
			char previous = srcs[i - 1];
			char current = srcs[i];
			char next = srcs[i + 1];
			if (current == '/' && next == '/') {
				isInsideCommentType1 = true;
				continue;
			}

			if (current == '/' && next == '*') {
				isInsideCommentType2 = true;
				continue;
			}

			if (isInsideCommentType1 && current == '\n') {
				isInsideCommentType1 = false;
				continue;
			}

			if (isInsideCommentType2 && previous == '*' && current == '/') {
				isInsideCommentType2 = false;
				continue;
			}

			if (!isInsideCommentType1 && !isInsideCommentType2)
				output += current;
		}

		return output;
	}

	public static String convertToString(List<String> lines) {
		String str = new String();
		for (String line : lines) {
			str += line + "\n";
		}
		return str;
	}

	public static String[] convertToStringArray(List<String> lines) {
		String[] str = new String[lines.size()];

		for (int i = 0; i < lines.size(); i++)
			str[i] = lines.get(i);

		return str;
	}

	public static void writeToFile(File file, String content) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		if (!file.exists())
			file.getParentFile().mkdirs();

		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static List<String> readFileContent(File file) {
		List<String> content = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			while ((str = in.readLine()) != null)
				content.add(str);
			in.close();
		} catch (IOException e) {
		}
		return content;
	}

	public static List<String> convertToList(String[] texts) {
		List<String> output = new ArrayList<String>();
		for (String text : texts)
			if (text.length() > 0)
				output.add(text);
		return output;
	}
}
