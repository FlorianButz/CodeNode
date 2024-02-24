package florianbutz.codenode.main;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import florianbutz.codenode.gui.CodeNode;
import florianbutz.codenode.gui.CodeNodeGUI;
import florianbutz.codenode.gui.TreePanel;

public class Main {

	public static String softwareVersion = "1.0.0";
	
	public static void main(String[] args) {
        CodeNodeGUI.BuildWindow();
    }
	
	public static String outputString;
	
	static String newLineString = "\n<br>";
	static String typeFormat = "<b><font color='#b854d4'>@</font></b>";
	static String methodNameFormat = "<font color='#6684e1'>@</font>";
	static String variableNameFormat = "<font color='#1fad83'>@</font>";
	static String declarationModiFormat = "<font color='#7d7a68'>@</font>";

	static Color classColor = new Color(255, 211, 105);
	static Color variableColor = new Color(31, 149, 211);
	static Color methodColor = new Color(242, 30, 24);
	
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
	            outputString += newLineString;
	        });

	        outputString += "</font>" + newLineString + "Variablen:" + newLineString + newLineString;
	        
	        compilationUnit.findAll(FieldDeclaration.class).forEach(field -> {
	            com.github.javaparser.ast.type.Type fieldType = field.getVariable(0).getType();

	            if(field.isPublic())
	            	outputString += FormatText(tabString + "+", declarationModiFormat);
	            else
	            	outputString += FormatText(tabString + "-", declarationModiFormat);
	            outputString += " " + FormatText(field.getVariable(0).toString(), variableNameFormat) + " : " + FormatText(fieldType.toString(), typeFormat) + newLineString;
	        });
	        
			new DeclarationVisitor(panel).visit(compilationUnit, null);
	        
	        return outputString + "</html>";
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
}

final class DeclarationVisitor extends VoidVisitorAdapter<Void> {

	TreePanel panel;
	
	public DeclarationVisitor(TreePanel panel) {
		this.panel = panel;
		panel.ResetGraph();
	}
	
    int classCounter = 0;
	
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        super.visit(n, arg);
        System.out.println("Class: " + n.getName());
        
        List<CodeNode> connectedToClass = new ArrayList<CodeNode>();
        
        int counter = 2;
        int nodeCounter = -1;
        for (MethodDeclaration methodDeclaration : n.getMethods()) {
			
			String descriptionString = methodDeclaration.isPublic() ? "public" : "private";
			descriptionString += "@n@n";
			StringBuilder descriptionBuilder = new StringBuilder(descriptionString);
			
			methodDeclaration.findAll(VariableDeclarator.class).forEach(variable -> {
				    descriptionBuilder.append("~").append(variable.getNameAsString()).append(" : ").append(variable.getTypeAsString()).append("@n");
			});
			
			String finalDescriptionString = descriptionBuilder.toString();
			
			int yPos = 100;
			if(nodeCounter != -1)
			{
				String[] lines = connectedToClass.get(nodeCounter).getDescription().split("@n");
				yPos += connectedToClass.get(nodeCounter).getY() + (connectedToClass.get(nodeCounter).getDimension().height/2);
			}
			
			CodeNode node = panel.CreateNode(methodDeclaration.getNameAsString() + "(" + methodDeclaration.getParameters() + ") : " + methodDeclaration.getTypeAsString(), 750*classCounter, yPos, 50, 50, null, Main.methodColor, finalDescriptionString);
			connectedToClass.add(node);
			counter++;
			nodeCounter++;
		}
        
        int fieldCounter = 1;
        for (FieldDeclaration fieldDeclaration : n.getFields()) {        	
			CodeNode node = panel.CreateNode(fieldDeclaration.getVariable(0).getNameAsString() + " : " + fieldDeclaration.getVariable(0).getTypeAsString(), 750*classCounter, -100 * fieldCounter, 50, 50, null, Main.variableColor, fieldDeclaration.isPublic() ? "public" : "private");
			connectedToClass.add(node);
			counter++;
			fieldCounter++;
		}

        panel.CreateNode(n.getNameAsString(), (750*classCounter)-250, 0, 50, 50, connectedToClass, Main.classColor, n.getExtendedTypes() + " : " + n.getImplementedTypes());
        
        classCounter++;
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
