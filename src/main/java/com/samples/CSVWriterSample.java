package com.samples;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.opencsv.CSVWriter;

public class CSVWriterSample {
	private static final String STRING_ARRAY_SAMPLE = "./string-array-sample.csv";

	public static void main(String[] args) throws IOException {
		Writer writer = Files.newBufferedWriter(Paths.get(STRING_ARRAY_SAMPLE));

		CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
		String[] headerRecord = { "Name", "Email", "Phone", "Country" };
		csvWriter.writeNext(headerRecord);

		csvWriter.writeNext(new String[] { "Sundar Pichai ♥", "sundar.pichai@gmail.com", "+1-1111111111", "India" });
		csvWriter.writeNext(new String[] { "Satya Nadella", "satya.nadella@outlook.com", "+1-1111111112", "India" });
		csvWriter.close();
	}
}
