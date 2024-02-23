package florianbutz.codenode.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;


public class CodeNode {
    
	private String text;
	private String description;
    private int x, y;
    private List<CodeNode> connections;
    private Dimension dimensions;
    
    public Color borderColor = new Color(255, 211, 105);

    public String getText() { return text;}
    public String getDescription() { return description;}
    public List<CodeNode> getConnections() { return connections;}
    public int getX() { return x;}
    public int getY() { return y;}
    public void SetPosition(int x, int y) { this.x = x; this.y = y; }
    public Dimension getDimension() {return dimensions;}
    
    public boolean isDragged = false;
    
    public CodeNode(String text, int x, int y, int width, int height, List<CodeNode> connections, String description) {
    	this.text = text;
    	this.x = x;
    	this.y = y;
    	if(connections != null)
    		this.connections = connections;
    	else
    		this.connections = new ArrayList<CodeNode>();
    	dimensions = new Dimension(width, height);
    	this.description = description;
    }
}
