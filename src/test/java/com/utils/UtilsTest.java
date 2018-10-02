package com.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void removeComments() {
		assertEquals("", Utils.removeComments(""));
		assertEquals("", Utils.removeComments("//fddfs"));
		assertEquals("", Utils.removeComments("/*fddfs*/"));
		assertEquals("int a= 0;int a=1;", Utils.removeComments("int a= 0;//fsdf stwert fsf\nint a=1;"));
		assertEquals("int a= 0;int a=1;", Utils.removeComments("int a= 0;/*asd fsl*/int a=1;"));
		assertEquals("int a= 0;int a=1;", Utils.removeComments("int a= 0;/*asd fs \njksfh sdg l*/int a=1;"));

	}

	@Test
	public void removeQuote() {
		assertEquals("", Utils.removeQuote("'a'"));
		assertEquals("", Utils.removeQuote("\"123\""));
		assertEquals("ab", Utils.removeQuote("a\"123\"b"));
		assertEquals("!gArgs.GetBoolArg(, true)", Utils.removeQuote("!gArgs.GetBoolArg(\"-usehd\", true)"));
		assertEquals("a++c", Utils.removeQuote("a+\"'b'\"+c"));

	}
}
