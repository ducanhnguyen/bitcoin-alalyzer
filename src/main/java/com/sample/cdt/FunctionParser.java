package com.sample.cdt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;

public class FunctionParser {

	public static void main(String[] args) throws Exception {
		String function = "int test(){int a=0;a++;}";
		FunctionParser functionParser = new FunctionParser();
		functionParser.parse(function.toCharArray());
	}

	public FunctionParser() {
	}

	public void parse(char[] statement) throws Exception {
		IASTTranslationUnit u = getIASTTranslationUnit(statement);
		ASTVisitor visitor = new ASTVisitor() {

			@Override
			public int visit(IASTDeclaration declaration) {
				if (declaration instanceof IASTFunctionDefinition)
					System.out.println("Khai bao ham : " + declaration.getRawSignature());

				else if (declaration instanceof IASTSimpleDeclaration)
					System.out.println("Khai bao kieu don gian : " + declaration.getRawSignature());

				return PROCESS_CONTINUE;
			}

			@Override
			public int leave(IASTDeclaration declaration) {
				System.out.println("leave...");
				return PROCESS_CONTINUE;
			}

		};
		visitor.shouldVisitDeclarations = true;
		u.accept(visitor);
	}

	private IASTTranslationUnit getIASTTranslationUnit(char[] code) throws Exception {
		FileContent fc = FileContent.create("", code);
		Map<String, String> macroDefinitions = new HashMap<String, String>();
		String[] includeSearchPaths = new String[0];
		IScannerInfo si = new ScannerInfo(macroDefinitions, includeSearchPaths);
		IncludeFileContentProvider ifcp = IncludeFileContentProvider.getEmptyFilesProvider();
		IIndex idx = null;
		int options = ILanguage.OPTION_IS_SOURCE_UNIT;
		IParserLogService log = new DefaultLogService();
		return GPPLanguage.getDefault().getASTTranslationUnit(fc, si, ifcp, idx, options, log);
	}
}