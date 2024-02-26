package florianbutz.codenode.main;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import florianbutz.codenode.gui.CodeNode;
import florianbutz.codenode.gui.CodeNodeGUI;
import florianbutz.codenode.gui.TreePanel;

public class Main {

	public static String softwareVersion = "1.0.2R";
	public static String licenseString = "MIT License\r\n"
			+ "\r\n"
			+ "Copyright (c) 2024 Florian Butz\r\n"
			+ "\r\n"
			+ "Permission is hereby granted, free of charge, to any person obtaining a copy\r\n"
			+ "of this software and associated documentation files (the \"Software\"), to deal\r\n"
			+ "in the Software without restriction, including without limitation the rights\r\n"
			+ "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\r\n"
			+ "copies of the Software, and to permit persons to whom the Software is\r\n"
			+ "furnished to do so, subject to the following conditions:\r\n"
			+ "\r\n"
			+ "The above copyright notice and this permission notice shall be included in all\r\n"
			+ "copies or substantial portions of the Software.\r\n"
			+ "\r\n"
			+ "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\r\n"
			+ "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\r\n"
			+ "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\r\n"
			+ "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\r\n"
			+ "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\r\n"
			+ "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\r\n"
			+ "SOFTWARE.";
	
	public static void main(String[] args) {
        CodeNodeGUI.BuildWindow(args);

        System.out.println("Running on Java version: " + System.getProperty("java.runtime.version"));
    }
	
	public static String outputString;
	
	static String newLineString = "\n<br>";
	static String typeFormat = "<b><font color='#b854d4'>@</font></b>";
	static String enumNameFormat = "<font color='#084319'>@</font>";
	static String enumConstantNameFormat = "<font color='#14250b'>@</font>";
	static String methodNameFormat = "<font color='#6684e1'>@</font>";
	static String variableNameFormat = "<font color='#1fad83'>@</font>";
	static String declarationModiFormat = "<font color='#7d7a68'>@</font>";

	public static Color classColor = new Color(255, 211, 105);
	public static Color enumColor = new Color(30, 225, 75);
	public static Color enumEntryColor = new Color(15, 125, 35);
	public static Color variableColor = new Color(31, 149, 211);
	public static Color localVariableColor = new Color(200, 125, 24);
	public static Color methodColor = new Color(242, 30, 24);
	
	static String tabString = "&nbsp;&nbsp;&nbsp;&nbsp;";
	
	public static String FormatText(String text, String format) {
		String[] splitFormat = format.split("@");

		text = text.replace("<", "[");
		text = text.replace(">", "]");
		String formattedText = splitFormat[0] + text + splitFormat[1];
		return formattedText;
	}
	
	public static String ParseCodes(TreePanel panel, String filePath) {
		
        // Parse the class from the string
		Path path = Path.of(filePath);
        CompilationUnit compilationUnit;
		try {
			compilationUnit = StaticJavaParser.parse(path);

	        outputString = "<html>";
	        
	        outputString += "Methoden:" + newLineString + newLineString;
	        
	        compilationUnit.findAll(MethodDeclaration.class).forEach(method -> {
	            com.github.javaparser.ast.type.Type returnType = method.getType();
	            
	            if(method.isPublic())
	            	outputString += FormatText(tabString + "+", declarationModiFormat);
	            else
	            	outputString += FormatText(tabString + "-", declarationModiFormat);
	            outputString += " " + FormatText(method.getNameAsString(), methodNameFormat) + " : " + FormatText(returnType.toString(), typeFormat) + newLineString;
	            
	            method.findAll(VariableDeclarator.class).forEach(variable -> {
		            outputString += FormatText(tabString + tabString + "~ ", declarationModiFormat) + FormatText(variable.getNameAsString() , variableNameFormat) + " : " + FormatText(variable.getTypeAsString(), typeFormat) + newLineString;
	            });
	        });

	        outputString += newLineString + "Variablen:" + newLineString + newLineString;
	        
	        compilationUnit.findAll(FieldDeclaration.class).forEach(field -> {
	            com.github.javaparser.ast.type.Type fieldType = field.getVariable(0).getType();

	            if(field.isPublic())
	            	outputString += FormatText(tabString + "+", declarationModiFormat);
	            else
	            	outputString += FormatText(tabString + "-", declarationModiFormat);
	            outputString += " " + FormatText(field.getVariable(0).toString(), variableNameFormat) + " : " + FormatText(fieldType.toString(), typeFormat) + newLineString;
	        });
	        
	        outputString += newLineString + "Enums:" + newLineString + newLineString;
	        
	        compilationUnit.findAll(EnumDeclaration.class).forEach(method -> {
	            
	            if(method.isPublic())
	            	outputString += FormatText(tabString + "+", declarationModiFormat);
	            else
	            	outputString += FormatText(tabString + "-", declarationModiFormat);
	            outputString += " " + FormatText(method.getNameAsString(), enumNameFormat) + newLineString;
	            
	            method.findAll(EnumConstantDeclaration.class).forEach(variable -> {
		            outputString += FormatText(tabString + tabString + "~ ", declarationModiFormat) + FormatText(variable.getNameAsString() , enumConstantNameFormat) + newLineString;
	            });
	            outputString += newLineString;
	        });
	        
			new DeclarationVisitor(panel).visit(compilationUnit, null);
	        
	        return outputString + "</html>";
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}

	public static void CopyStructTextToClipboard(String text) {
		CleanerProperties props = new CleanerProperties();
		props.setPruneTags("script");
		String result = new HtmlCleaner(props).clean(text).getText().toString().replace("&nbsp;", "");

		StringSelection stringSelection = new StringSelection(result);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
}

final class DeclarationVisitor extends VoidVisitorAdapter<Void> {

	TreePanel panel;
	
	public DeclarationVisitor(TreePanel panel) {
		this.panel = panel;
		panel.ResetGraph();
	}
	
    int classCounter = 0;
    int previousMaxLocalVarCount = 0;
	
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        super.visit(n, arg);
        System.out.println("Class: " + n.getName());
        
        List<CodeNode> connectedToClass = new ArrayList<CodeNode>();
        
        int localVarCounter = 1;
        int counter = 2;
        int nodeCounter = -1;
        
        int maxLocalVarCounterValue = 0;
        
        for (MethodDeclaration methodDeclaration : n.getMethods()) {
			List<VariableDeclarator> variables = methodDeclaration.findAll(VariableDeclarator.class);
			maxLocalVarCounterValue += variables.size();
        }
        
        System.out.println(maxLocalVarCounterValue);
        maxLocalVarCounterValue = (int)(maxLocalVarCounterValue / 4);
        
        for (MethodDeclaration methodDeclaration : n.getMethods()) {
			
			String descriptionString = methodDeclaration.isPublic() ? "public" : "private";
			
			int yPos = 100;
			if(nodeCounter != -1)
			{
				String[] lines = connectedToClass.get(nodeCounter).getDescription().split("@n");
				yPos += connectedToClass.get(nodeCounter).getY() + (connectedToClass.get(nodeCounter).getDimension().height/2);
			}

			List<CodeNode> methodConnections = new ArrayList<CodeNode>(); 
			
			List<VariableDeclarator> variables = methodDeclaration.findAll(VariableDeclarator.class);
			int varCounting = 0 - variables.size() / 2;
			for(int v = 0; v < variables.size(); v++) {
				CodeNode varNode = panel.CreateNode(variables.get(v).getNameAsString() + " : " + variables.get(v).getTypeAsString(),
						750*classCounter + (previousMaxLocalVarCount * 200) + 200 + (200 * localVarCounter),
						yPos + (varCounting * 50),
						50, 50,
						null,
						Main.localVariableColor,
						"");
				varCounting++;
				methodConnections.add(varNode);
			}
			
			CodeNode node = panel.CreateNode(methodDeclaration.getNameAsString() + "(" + methodDeclaration.getParameters().toString().replace("[", "").replace("]", "") + ") : " + methodDeclaration.getTypeAsString(),
					750*classCounter + previousMaxLocalVarCount * 200,
					yPos,
					50, 50,
					methodConnections,
					Main.methodColor,
					descriptionString
			);
			
			connectedToClass.add(node);
			counter++;
			nodeCounter++;
			
			if(localVarCounter < maxLocalVarCounterValue)
				localVarCounter++;
			else 
				localVarCounter = 1;
		}
        
        int fieldCounter = 1;
        for (FieldDeclaration fieldDeclaration : n.getFields()) {        	
			CodeNode node = panel.CreateNode(fieldDeclaration.getVariable(0).getNameAsString() + " : " + fieldDeclaration.getVariable(0).getTypeAsString(), 750*classCounter + previousMaxLocalVarCount * 200, -100 * fieldCounter, 50, 50, null, Main.variableColor, fieldDeclaration.isPublic() ? "public" : "private");
			connectedToClass.add(node);
			counter++;
			fieldCounter++;
		}

        panel.CreateNode(n.getNameAsString(), (750*classCounter + previousMaxLocalVarCount * 250)-250, 0, 50, 50, connectedToClass, Main.classColor, "Extends: " + n.getExtendedTypes().toString().replace("[", "").replace("]", "") + "@nImplements: " + n.getImplementedTypes().toString().replace("[", "").replace("]", ""));
        
        previousMaxLocalVarCount += maxLocalVarCounterValue;
        classCounter++;
    }

    public void visit(EnumDeclaration n, Void arg) {
    	List<CodeNode> connectedToEnum = new ArrayList<CodeNode>();
    	int counter = 0;
    	int nodeCounter = -1;
        int fieldCounter = 1;
        int localVarCounter = 1;
        int enumEntryCounter = 1;
        
        for (FieldDeclaration fieldDeclaration : n.getFields()) {        	
			CodeNode node = panel.CreateNode(fieldDeclaration.getVariable(0).getNameAsString() + " : " + fieldDeclaration.getVariable(0).getTypeAsString(), 750*classCounter + previousMaxLocalVarCount * 200, -100 * fieldCounter, 50, 50, null, Main.variableColor, fieldDeclaration.isPublic() ? "public" : "private");
			connectedToEnum.add(node);
			counter++;
			fieldCounter++;
		}
    	
        int maxLocalVarCounterValue = 0;
        
        for (MethodDeclaration methodDeclaration : n.getMethods()) {
			List<VariableDeclarator> variables = methodDeclaration.findAll(VariableDeclarator.class);
			maxLocalVarCounterValue += variables.size();
        }
        
        System.out.println(maxLocalVarCounterValue);
        maxLocalVarCounterValue = (int)(maxLocalVarCounterValue / 4);
        
        for (MethodDeclaration methodDeclaration : n.getMethods()) {
			
			String descriptionString = methodDeclaration.isPublic() ? "public" : "private";
			
			int yPos = 100;
			if(nodeCounter != -1)
			{
				String[] lines = connectedToEnum.get(nodeCounter).getDescription().split("@n");
				yPos += connectedToEnum.get(nodeCounter).getY() + (connectedToEnum.get(nodeCounter).getDimension().height/2);
			}

			List<CodeNode> methodConnections = new ArrayList<CodeNode>(); 
			
			List<VariableDeclarator> variables = methodDeclaration.findAll(VariableDeclarator.class);
			int varCounting = 0 - variables.size() / 2;
			for(int v = 0; v < variables.size(); v++) {
				CodeNode varNode = panel.CreateNode(variables.get(v).getNameAsString() + " : " + variables.get(v).getTypeAsString(),
						750*classCounter + (previousMaxLocalVarCount * 200) + 200 + (200 * localVarCounter),
						yPos + (varCounting * 50),
						50, 50,
						null,
						Main.localVariableColor,
						"");
				varCounting++;
				methodConnections.add(varNode);
			}
			
			CodeNode node = panel.CreateNode(methodDeclaration.getNameAsString() + "(" + methodDeclaration.getParameters().toString().replace("[", "").replace("]", "") + ") : " + methodDeclaration.getTypeAsString(),
					750*classCounter + previousMaxLocalVarCount * 200,
					yPos,
					50, 50,
					methodConnections,
					Main.methodColor,
					descriptionString
			);
			
			connectedToEnum.add(node);
			counter++;
			nodeCounter++;
			
			if(localVarCounter < maxLocalVarCounterValue)
				localVarCounter++;
			else 
				localVarCounter = 1;
		}
        
        for(int e = 0; e < n.getEntries().size(); e++) {
        	EnumConstantDeclaration entry = n.getEntries().get(e);
			CodeNode node = panel.CreateNode(entry.getNameAsString(),
					(750*classCounter + previousMaxLocalVarCount * 250)-250,
					-100 * (e + 1),
					50, 50,
					null,
					Main.enumEntryColor, "");
			connectedToEnum.add(node);
        }
        
        panel.CreateNode(n.getNameAsString(),
        		(750*classCounter + previousMaxLocalVarCount * 250)-250,
        		0,
        		50,
        		50,
        		connectedToEnum,
        		Main.enumColor,
        		"");
        System.out.println(n.getFields());
    }
    
    @Override
    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);
    }

    @Override
    public void visit(VariableDeclarator n, Void arg) {
        super.visit(n, arg);
    }
}
