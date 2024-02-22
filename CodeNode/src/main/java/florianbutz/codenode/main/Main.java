package florianbutz.codenode.main;

import java.io.IOException;
import java.nio.file.Path;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import florianbutz.codenode.gui.CodeNodeGUI;

public class Main {

	public static String outputString;
	
	public static void main(String[] args) {
        CodeNodeGUI.BuildWindow();
    }
	
	public static String ParseCodes(String filePath) {

        // Parse the class from the string
		Path path = Path.of(filePath);
        CompilationUnit compilationUnit;
		try {
			compilationUnit = StaticJavaParser.parse(path);

	        outputString = "";
	        
	        outputString += "Methoden:\r\r";
	        
	        compilationUnit.findAll(MethodDeclaration.class).forEach(method -> {
	            com.github.javaparser.ast.type.Type returnType = method.getType();
	            System.out.println("Method: " + returnType + " " + method.getName() +
	                               " (Access Modifier: " + method.isPublic() + ")");
	            
	            if(method.isPublic())
	            	outputString += "+";
	            else
	            	outputString += "-";
	            outputString += " " + method.getNameAsString() + " : " + returnType + "\r";
	            
	            method.findAll(VariableDeclarator.class).forEach(variable -> {
		            outputString += "    <clor=red>~ " + variable.getNameAsString() + " : " + variable.getTypeAsString() + "\r";
	            });
	            outputString += "\r";
	        });

	        outputString += "\rVariablen:\r\r";
	        
	        compilationUnit.findAll(FieldDeclaration.class).forEach(field -> {
	            com.github.javaparser.ast.type.Type fieldType = field.getVariable(0).getType();
	            System.out.println("Field: " + fieldType + " " + field.getVariables() +
	                               " (Access Modifier: " + field.isPublic() + ")");
	            if(field.isPublic())
	            	outputString += "+";
	            else
	            	outputString += "-";
	            outputString += " " + field.getVariable(0) + " : " + fieldType + "\r";
	        });
	        
	        return outputString;
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
}
