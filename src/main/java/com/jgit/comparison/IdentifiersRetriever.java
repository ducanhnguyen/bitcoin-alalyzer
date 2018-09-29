package com.jgit.comparison;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTIdExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTIdExpression;

import com.sample.cdt.ASTUtils;
import com.utils.Utils;

/**
 * Get all identifier in a code snippet
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class IdentifiersRetriever {
	private String codeSnippet = new String();
	private List<String> identifiers = new ArrayList<String>();

	public static void main(String[] args) {
//		String codeSnippet = "int test(){int a=0;a++;}";
		File snippetFile = new File("./data/Csample.cpp");

		if (snippetFile.exists()) {
			String codeSnippet = Utils.convertToString(Utils.readFileContent(snippetFile));
			IdentifiersRetriever identifierRetriever = new IdentifiersRetriever();
			identifierRetriever.setCodeSnippet(codeSnippet);
			List<String> identifiers = identifierRetriever.getIdentifier();
			System.out.println("identifiers: " + identifiers);
		}
	}

	public List<String> getIdentifier() {
		List<String> identifiers = new ArrayList<String>();

		if (codeSnippet.length() > 0) {
			// create ast of the code snippet
			ILanguage lang = codeSnippet.toLowerCase().endsWith(".c") ? GCCLanguage.getDefault()
					: GPPLanguage.getDefault();
			IASTTranslationUnit u = ASTUtils.getIASTTranslationUnit(codeSnippet.toCharArray(), "", null, lang);

			// get identifiers
			IASTNode firstChild = u.getTranslationUnit().getChildren()[0];
			System.out.println(firstChild.getRawSignature());
			List<IASTIdExpression> astIdentifiers = getAstIdentifiers(firstChild);

			for (IASTIdExpression astIdentifier : astIdentifiers) {
				identifiers.add(astIdentifier.toString());
			}
		}
		return identifiers;
	}

	/**
	 * Get all identifiers in an ASTNode
	 *
	 * @param ast
	 * @return
	 */
	public List<IASTIdExpression> getAstIdentifiers(IASTNode ast) {
		final List<IASTIdExpression> astIdentifiers = new ArrayList<IASTIdExpression>();

		ASTVisitor visitor = new ASTVisitor() {
			@Override
			public int visit(IASTExpression expression) {
				if (expression instanceof CPPASTIdExpression)
					astIdentifiers.add((CPPASTIdExpression) expression);
				else if (expression instanceof CASTIdExpression)
					astIdentifiers.add((CASTIdExpression) expression);
				return ASTVisitor.PROCESS_CONTINUE;
			}
		};

		visitor.shouldVisitExpressions = true;

		ast.accept(visitor);
		return astIdentifiers;
	}

	public String getCodeSnippet() {
		return codeSnippet;
	}

	public void setCodeSnippet(String codeSnippet) {
		this.codeSnippet = codeSnippet;
	}

	public List<String> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
	}
}
