package com.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTIdExpression;

public class ASTUtils {
	
//	public static void main(String[] args) {
//		File snippetFile = new File ("./data/Csample.cpp");
//		ASTUtils.printTree(snippetFile);
//	}

	public static IASTTranslationUnit getIASTTranslationUnit(char[] source, String filePath,
			Map<String, String> macroList, ILanguage lang) {
		FileContent reader = FileContent.create(filePath, source);
		String[] includeSearchPaths = new String[0];
		IScannerInfo scanInfo = new ScannerInfo(macroList, includeSearchPaths);
		IncludeFileContentProvider fileCreator = IncludeFileContentProvider.getSavedFilesProvider();
		int options = ILanguage.OPTION_IS_SOURCE_UNIT;
		IParserLogService log = new DefaultLogService();

		try {
			return lang.getASTTranslationUnit(reader, scanInfo, fileCreator, null, options, log);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void printTree(IASTNode n, String s) {
		String content = n.getRawSignature().replaceAll("[\r\n]", "");
		IASTNode[] child = n.getChildren();
		System.out.println(s + content + ": " + n.getClass().getSimpleName());
		for (IASTNode c : child)
			ASTUtils.printTree(c, s + "   ");

	}

	/**
	 * Print content of abstract tree from source code <br/>
	 * Example: From the ASTNode of this function: <br/>
	 * 
	 * <pre>
	 * int class_test1(SinhVien sv) {
	 * 	if ((1.0 + 2) > x)
	 * 		return true;
	 * 	if (1 + 2 > x + 1 + 3)
	 * 		return true;
	 * }
	 * </pre>
	 * 
	 * We have the content of ASTNode:
	 * 
	 * <pre>
	 | int class_test1(SinhVien sv){	if ((1.0+2)>x)		return true;	if (1+2>x+1+3)		return true;}: CPPASTTranslationUnit
	 |    int class_test1(SinhVien sv){	if ((1.0+2)>x)		return true;	if (1+2>x+1+3)		return true;}: CPPASTFunctionDefinition
	 |       int: CPPASTSimpleDeclSpecifier
	 |       class_test1(SinhVien sv): CPPASTFunctionDeclarator
	 |          class_test1: CPPASTName
	 |          SinhVien sv: CPPASTParameterDeclaration
	 |             SinhVien: CPPASTNamedTypeSpecifier
	 |                SinhVien: CPPASTName
	 |             sv: CPPASTDeclarator
	 |                sv: CPPASTName
	 |       {	if ((1.0+2)>x)		return true;	if (1+2>x+1+3)		return true;}: CPPASTCompoundStatement
	 |          if ((1.0+2)>x)		return true;: CPPASTIfStatement
	 |             (1.0+2)>x: CPPASTBinaryExpression
	 |                (1.0+2): CPPASTUnaryExpression
	 |                   1.0+2: CPPASTBinaryExpression
	 |                      1.0: CPPASTLiteralExpression
	 |                      2: CPPASTLiteralExpression
	 |                x: CPPASTIdExpression
	 |                   x: CPPASTName
	 |             return true;: CPPASTReturnStatement
	 |                true: CPPASTLiteralExpression
	 |          if (1+2>x+1+3)		return true;: CPPASTIfStatement
	 |             1+2>x+1+3: CPPASTBinaryExpression
	 |                1+2: CPPASTBinaryExpression
	 |                   1: CPPASTLiteralExpression
	 |                   2: CPPASTLiteralExpression
	 |                x+1+3: CPPASTBinaryExpression
	 |                   x+1: CPPASTBinaryExpression
	 |                      x: CPPASTIdExpression
	 |                         x: CPPASTName
	 |                      1: CPPASTLiteralExpression
	 |                   3: CPPASTLiteralExpression
	 |             return true;: CPPASTReturnStatement
	 |                true: CPPASTLiteralExpression
	 * 
	 * </pre>
	 */
	public static void printTree(File snippetFile) {
		try {
			List<String> content = Utils.readFileContent(snippetFile);
			String contentStr = Utils.convertToString(content);
			ILanguage lang = snippetFile.getName().toLowerCase().endsWith(".c") ? GCCLanguage.getDefault()
					: GPPLanguage.getDefault();
			IASTTranslationUnit u = ASTUtils.getIASTTranslationUnit(contentStr.toCharArray(),
					snippetFile.getAbsolutePath(), null, lang);

			ASTUtils.printTree(u, " | ");
		} catch (Exception e) {

		}
	}

	final static List<CPPASTIdExpression> ids = new ArrayList<CPPASTIdExpression>();

	/**
	 * Get all CPPASTIdExpression objects in ASTNode
	 *
	 * @param ast
	 * @return
	 */
	public static List<CPPASTIdExpression> getIds(IASTNode ast) {

		ASTVisitor visitor = new ASTVisitor() {
			@Override
			public int visit(IASTExpression expression) {
				if (expression instanceof CPPASTIdExpression)
					ids.add((CPPASTIdExpression) expression);
				return ASTVisitor.PROCESS_CONTINUE;
			}
		};

		visitor.shouldVisitExpressions = true;

		ast.accept(visitor);
		return ids;
	}

}
