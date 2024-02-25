package florianbutz.codenode.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.google.common.base.CaseFormat;

import florianbutz.codenode.main.ErrorCode;


public class TreePanel extends JPanel{

    private List<CodeNode> nodes;

    public Color backgroundColor = new Color(100, 100, 100);
    public Color textColor = new Color(225, 225, 225);
    public Color descriptionTextColor = new Color(150, 150, 150);

    public Color nodeBackgroundColor = new Color(50, 50, 50);

    public Color connectionColor = new Color(75, 75, 75, 50);
    
    public int borderRadius = 20;
    
    public int viewportWidth = 500;
    public int viewportHeight = 500;

    private int lastViewportPosX = 0;
    private int lastViewportPosY = 0;
    private int newViewportPosX = 0;
    private int newViewportPosY = 0;
    
    private int viewportPosX = 0;
    private int viewportPosY = 0;    

    private int startX, startY;
    private int deltaX, deltaY;
    private int rawStartX, rawStartY;
    private int dragNodeX, dragNodeY;
    CodeNode draggedNode;

    public float lastViewportScaleFactor = 1f;
    public float viewportScaleFactor = 1f;
    public float newViewportScaleFactor = 1f;
    
    public int scrollSpeed = 35;
    public float moveViewportSmoothing = 0.3f;
    public float scaleViewportSmoothing = 0.65f;
    
    public float backgroundTransparency = 0.15f;
    
    public void ResetViewport() {
    	SetViewportPosX(0);
    	SetViewportPosY(0);
    	newViewportScaleFactor = 1.0f;
    }
    
    public void SetViewportPosX(int newPos) {
    	newViewportPosX = newPos;
    }
    public void SetViewportPosY(int newPos) {
    	newViewportPosY = newPos;
    }

    private String pattern1Path = "/florianbutz/codenode/img/pattern1.png";
    private String pattern2Path = "/florianbutz/codenode/img/pattern2.png";
    private String pattern3Path = "/florianbutz/codenode/img/pattern3.png";
    
    private String activePattern = pattern1Path;
    
    public int updateRate = 13;
    
    public void ChangePattern(int pType) {
    	switch (pType) {
		case 0:
			activePattern = pattern1Path;
			break;
		case 1:
			activePattern = pattern2Path;
			break;
		case 2:
			activePattern = pattern3Path;
			break;
		default:
			activePattern = pattern1Path;
			break;
		}
    }
    
    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    
    public void ResetGraph() {
    	nodes = new ArrayList<CodeNode>();
    }
    
    public TreePanel() {
    	nodes = new ArrayList<CodeNode>();
    	
    	Timer timer = new Timer(); 
        TimerTask task = new RepainTreePanel(this);
    	
        timer.schedule(task, updateRate, updateRate);
        
        addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(!e.isShiftDown() && !e.isControlDown()) SetViewportPosY(viewportPosY - (e.getWheelRotation() * scrollSpeed));
				else if(e.isShiftDown() && !e.isControlDown()) SetViewportPosX(viewportPosX - (e.getWheelRotation() * scrollSpeed));
				else if(!e.isShiftDown() && e.isControlDown()) {
					int notches = e.getWheelRotation();
		            if (notches < 0) {
		                newViewportScaleFactor = clamp(viewportScaleFactor * 1.15f, 0.5f, 2f);
		            } else {
		                newViewportScaleFactor = clamp(viewportScaleFactor * 0.85f, 0.5f, 2f);
		            }
				}
			}
		});
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX() - viewportPosX;
                startY = e.getY() - viewportPosY;
                
                rawStartX = e.getX();
                rawStartY = e.getY();

                draggedNode = isDraggingNode(nodes, new HashSet<CodeNode>());
                if(draggedNode != null) {
                	dragNodeX = draggedNode.getX();
                	dragNodeY = draggedNode.getY();
                	draggedNode.isDragged = true;
                }	
            }
            @Override
            public void mouseReleased(MouseEvent e) {            	
            	if(draggedNode != null) draggedNode.isDragged = false;
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
        	
        	@Override
        	public void mouseMoved(MouseEvent e) {
        		deltaX = e.getX();
        		deltaY = e.getY();
        	}
        	
            @Override
            public void mouseDragged(MouseEvent e) {     
            	deltaX = e.getX();
        		deltaY = e.getY();

        		if(draggedNode == null) {
        			SetViewportPosX(e.getX() - startX);
        			SetViewportPosY(e.getY() - startY);
        		}else {
        			int newDragNodeX = dragNodeX + (e.getX() - rawStartX);
        			int newDragNodeY = dragNodeY + (e.getY() - rawStartY);

        			draggedNode.SetPosition(newDragNodeX, newDragNodeY);
        		}

            }
        });
    }
    
    public CodeNode isDraggingNode(List<CodeNode> codeNodes, Set<CodeNode> visitedNodes) {
        if(mousePoint == null) return null;
        
    	int mouseX = (int)mousePoint.getX();
        int mouseY = (int)mousePoint.getY();

        for (CodeNode node : codeNodes) {
            // Check if the node is already visited to avoid infinite recursion
            if(visitedNodes != null) {
            	if (visitedNodes.contains(node)) {
                	continue;
        		}
            }
            
            Rectangle nodeRectangle = new Rectangle();
        	nodeRectangle.x = GetViewportX(node.getX()) - node.getDimension().width / 2;
        	nodeRectangle.y =  GetViewportY(node.getY()) - node.getDimension().height / 2;
            nodeRectangle.width = node.getDimension().width;
            nodeRectangle.height = node.getDimension().height;

            if (nodeRectangle.contains(mouseX, mouseY)) {
                return node;
            }
            
            // Mark the current node as visited before the recursive call
            visitedNodes.add(node);

            if (node.getConnections() != null && node.getConnections().size() != 0) {
                CodeNode subnode = isDraggingNode(node.getConnections(), visitedNodes);
                if (subnode != null) return subnode;
            }
        }

        return null;
    }
    
    int lerp(int a, int b, float f)
    {
        return (int) (a + f * (b - a));
    }
    
    float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
    }
    
    private Point2D mousePoint;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        viewportPosX = lerp(lastViewportPosX, newViewportPosX, moveViewportSmoothing);
        viewportPosY = lerp(lastViewportPosY, newViewportPosY, moveViewportSmoothing);
        
        lastViewportPosX = viewportPosX;
        lastViewportPosY = viewportPosY;
        
        viewportScaleFactor = lerp(lastViewportScaleFactor, newViewportScaleFactor, scaleViewportSmoothing);
        
        lastViewportScaleFactor = viewportScaleFactor;
        
        Graphics2D graphics2D = (Graphics2D)g;
        
        graphics2D.setColor(backgroundColor);
        graphics2D.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), borderRadius, borderRadius);
        
        AffineTransform at = new AffineTransform();
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        at.setToIdentity();
        at.translate(centerX, centerY);
        at.scale(viewportScaleFactor, viewportScaleFactor);
        at.translate(-centerX, -centerY);
        graphics2D.transform(at);
        
        //Set  anti-alias!
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        		RenderingHints.VALUE_ANTIALIAS_ON); 
        
        // Set anti-alias for text
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        		RenderingHints.VALUE_TEXT_ANTIALIAS_ON); 
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, backgroundTransparency);
        graphics2D.setComposite(alphaComposite);
        
        try {
        	URL imageUrl = getClass().getResource(activePattern);
        	BufferedImage image;
        	image = ImageIO.read(imageUrl);
        	
        	graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        	
        	int repeats = 12;
        	int imageWidth = 400;
        	int imageHeight = 400;
        	
        	int teleportDistance = 400;
        	
        	for(int x = 0; x < repeats; x++) {
        		for(int y = 0; y < repeats; y++) {
        			graphics2D.drawImage(
        					image,
        					GetViewportX(((int)((-viewportPosX - (imageWidth/2) * repeats) / teleportDistance)) * teleportDistance) + (imageWidth * x),
        					GetViewportY(((int)((-viewportPosY - (imageHeight/2) * repeats) / teleportDistance)) * teleportDistance) + (imageHeight * y),
        					imageWidth,
        					imageHeight,
        					this);
        		}
        	}
        	
        } catch (IOException e) {
        	CodeNodeGUI.DisplayError("Textur konnte nicht geladen werden.", e.getLocalizedMessage(), ErrorCode.RessourceLoadFailure);
        }
        
        alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        graphics2D.setComposite(alphaComposite);
        
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.transform(at);

        mousePoint = new Point(deltaX, deltaY);
        try {
            AffineTransform inverseTransform = at.createInverse();
            inverseTransform.transform(mousePoint, mousePoint);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        
    	// Draw connections
        for (CodeNode node : nodes) {
            DrawConnections(node, graphics2D);
        }
    	
        // Draw nodes
        for (CodeNode node : nodes) {
            DrawNode(node, graphics2D);
        }
    }
    
    public void DrawConnections(CodeNode node, Graphics2D g) {
    	for (CodeNode connectedNode : node.getConnections()) {
    		g.setPaint(new GradientPaint(GetViewportX(node.getX()), GetViewportY(node.getY()), node.borderColor, GetViewportX(connectedNode.getX()), GetViewportY(connectedNode.getY()), connectedNode.borderColor));
            g.setStroke(new BasicStroke(5));
        	g.draw(new Line2D.Float(
        			GetViewportX(node.getX()),
        			GetViewportY(node.getY()),
        			GetViewportX(connectedNode.getX()),
        			GetViewportY(connectedNode.getY())));
            DrawNode(connectedNode, g);
        }
    }
    
    public void DrawNode(CodeNode node, Graphics2D g) {    	
    	Rectangle nodeRectangle = new Rectangle();
    	nodeRectangle.x = (int)((GetViewportX(node.getX()) - node.getDimension().width / 2));
    	nodeRectangle.y =  (int)((GetViewportY(node.getY()) - node.getDimension().height / 2));
    	nodeRectangle.width = node.getDimension().width;
    	nodeRectangle.height = node.getDimension().height;
    	
    	Color bgColor = new Color(
    			(int)((nodeBackgroundColor.getRed()*node.borderColor.getRed())/255), 
    			(int)((nodeBackgroundColor.getGreen()*node.borderColor.getGreen())/255), 
    			(int)((nodeBackgroundColor.getBlue()*node.borderColor.getBlue())/255));
    	
        g.setColor(bgColor);
        g.fillRoundRect(nodeRectangle.x, nodeRectangle.y, nodeRectangle.width, nodeRectangle.height, 15, 15);
        g.setStroke(new BasicStroke(2));
        
        if(!node.isDragged)
        	g.setColor(node.borderColor);
        else
        	g.setColor(Color.white);
        
        g.drawRoundRect(nodeRectangle.x, nodeRectangle.y, nodeRectangle.width, nodeRectangle.height, 15, 15);
        
        g.setColor(textColor);
        
        Font font = new Font("NotoSans", Font.BOLD, 15);
        FontMetrics metrics = g.getFontMetrics(font);
        
        int x = nodeRectangle.x + (nodeRectangle.width - metrics.stringWidth(node.getText())) / 2;
        
        g.setFont(font);
        
        g.drawString(node.getText(), x, nodeRectangle.y + metrics.getHeight() + 5);
        
        g.setColor(descriptionTextColor);
        
        Font fontDesc = new Font("NotoSans", Font.BOLD, 10);
        FontMetrics metricsDesc = g.getFontMetrics(fontDesc);
        g.setFont(fontDesc);
        
        int counter = 1;
        for (String sub : node.getDescription().split("@n")) {
            int xDesc = nodeRectangle.x + (nodeRectangle.width - metricsDesc.stringWidth(sub)) / 2;
            
        	g.drawString(sub, xDesc, nodeRectangle.y + metricsDesc.getHeight() * 2 + (13 * counter));
        	counter++;
		}
    }
    
    public int GetViewportX(int x) {
    	return (x + viewportPosX);
    }
    public int GetViewportY(int y) {
    	return (y + viewportPosY);
    }
    
    public int GetViewportXUnscaled(int x) {
    	return (int)((x + viewportPosX )* viewportScaleFactor);
    }
    public int GetViewportYUnscaled(int y) {
    	return (int)((y + viewportPosY )* viewportScaleFactor);
    }
    
    public void AddNode(CodeNode node) {
    	nodes.add(node);
    }
    
    public CodeNode CreateNode(String title, int x, int y, int width, int height, List<CodeNode> connectedNodes, Color nodeColor, String description) {
    	int sizedHeight = 0;
    	
    	int longestDescLineCount = 0;
    	
    	if(description == "") sizedHeight = 40;
    	else {
    		String[] lines = description.split("@n");
    		
    		for(int i = 0; i < lines.length; i++) {
    			if(lines[i].length() > longestDescLineCount) longestDescLineCount = lines[i].length();
    		}
    		
    		sizedHeight = 45 + (int)(13 * lines.length);
    	}
    	
    	int sizedWidth = width;
    	if(title.length() > longestDescLineCount)
    		sizedWidth = (int)(title.length() * 8f) + width;
    	else
    		sizedWidth = (int)(longestDescLineCount * 4f) + width;
    	
    	System.out.println(longestDescLineCount);

    	if(title.length() > longestDescLineCount)
    		System.out.println("Title size");
    	else
    		System.out.println("Description size");
    	
    	CodeNode node = new CodeNode(title, x, y+(sizedHeight/2), sizedWidth, sizedHeight, connectedNodes, description);
    	node.borderColor = nodeColor;
    	AddNode(node);
    	return node;
    }
}

class RepainTreePanel extends TimerTask 
{
	TreePanel panel;
	
	public RepainTreePanel(TreePanel panel) {
		this.panel = panel;
	}
	
	public void run() 
	{
	   panel.repaint();
	} 
} 