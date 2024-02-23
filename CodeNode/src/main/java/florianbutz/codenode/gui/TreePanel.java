package florianbutz.codenode.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
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
    
    public float viewportScale = 1f;
    
    public int scrollSpeed = 35;
    public float viewportSmoothing = 0.3f;
    
    public float backgroundTransparency = 0.15f;
    
    public void ResetViewport() {
    	SetViewportPosX(0);
    	SetViewportPosY(0);
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
    
    public void ResetGraph() {
    	nodes = new ArrayList<CodeNode>();
    }
    
    public TreePanel() {
    	nodes = new ArrayList<CodeNode>();
    	
    	Timer timer = new Timer(); 
        TimerTask task = new RepainTreePanel(this);
    	
        timer.schedule(task, 15, 15);
        
        addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(!e.isShiftDown()) SetViewportPosY(viewportPosY - (e.getWheelRotation() * scrollSpeed));
				else SetViewportPosX(viewportPosX - (e.getWheelRotation() * scrollSpeed));
			}
		});
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX() - (int)(viewportPosX * viewportScale);
                startY = e.getY() - (int)(viewportPosY * viewportScale);
                
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
            public void mouseDragged(MouseEvent e) {

                deltaX = e.getX() - viewportPosX;
                deltaY = e.getY() - viewportPosY;
            	
            	if(draggedNode == null) {
            		SetViewportPosX(e.getX() - (int)(startX*viewportScale));
            		SetViewportPosY(e.getY() - (int)(startY*viewportScale));
            	}else {
            		int newDragNodeX = dragNodeX + (e.getX() - rawStartX);
                    int newDragNodeY = dragNodeY + (e.getY() - rawStartY);
                    
                    draggedNode.SetPosition(newDragNodeX, newDragNodeY);
				}
            }
        });

    }
    
    public CodeNode isDraggingNode(List<CodeNode> codeNodes, Set<CodeNode> visitedNodes) {
        int mouseX = GetViewportX(startX);
        int mouseY = GetViewportY(startY);

        for (CodeNode node : codeNodes) {
            // Check if the node is already visited to avoid infinite recursion
            if(visitedNodes != null) {
            	if (visitedNodes.contains(node)) {
                	continue;
        		}
            }

            Rectangle nodeRectangle = new Rectangle();
            nodeRectangle.x = GetViewportX(node.getX()) - node.getDimension().width / 2;
            nodeRectangle.y = GetViewportY(node.getY()) - node.getDimension().height / 2;
            nodeRectangle.width = (int)(node.getDimension().width * viewportScale);
            nodeRectangle.height = (int)(node.getDimension().height * viewportScale);

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
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        viewportPosX = lerp(lastViewportPosX, newViewportPosX, viewportSmoothing);
        viewportPosY = lerp(lastViewportPosY, newViewportPosY, viewportSmoothing);
        
        lastViewportPosX = viewportPosX;
        lastViewportPosY = viewportPosY;
        
        Graphics2D graphics2D = (Graphics2D)g;
        
        //Set  anti-alias!
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        		RenderingHints.VALUE_ANTIALIAS_ON); 
        
        // Set anti-alias for text
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        		RenderingHints.VALUE_TEXT_ANTIALIAS_ON); 
        
        graphics2D.setColor(backgroundColor);
        graphics2D.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), borderRadius, borderRadius);
        
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, backgroundTransparency);
        graphics2D.setComposite(alphaComposite);
        	
        try {
        	URL imageUrl = getClass().getResource(activePattern);
        	BufferedImage image;
        	image = ImageIO.read(imageUrl);
        	
        	graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        	graphics2D.drawImage(image, GetViewportXUnscaled(((int)((-viewportPosX - 75) / 50)) * 50), GetViewportYUnscaled(((int)((-viewportPosY - 75) / 50)) * 50), (int)(1000 * 1f), (int)(1000 * 1f), this);
        } catch (IOException e) {
        	CodeNodeGUI.DisplayError("Textur konnte nicht geladen werden.", e.getLocalizedMessage(), ErrorCode.RessourceLoadFailure);
        }
        
        alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        graphics2D.setComposite(alphaComposite);
        
    	graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
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
        	g.setColor(connectionColor);
            g.setStroke(new BasicStroke(5 * viewportScale));
        	g.draw(new Line2D.Float(GetViewportX(node.getX()), GetViewportY(node.getY()), GetViewportX(connectedNode.getX()), GetViewportY(connectedNode.getY())));
            DrawNode(connectedNode, g);
        }
    }
    
    public void DrawNode(CodeNode node, Graphics2D g) {    	
    	Rectangle nodeRectangle = new Rectangle();
    	nodeRectangle.x = GetViewportX(node.getX()) - node.getDimension().width / 2;
    	nodeRectangle.y =  GetViewportY(node.getY()) - node.getDimension().height / 2;
    	nodeRectangle.width = (int)(node.getDimension().width * viewportScale);
    	nodeRectangle.height = (int)(node.getDimension().height * viewportScale);
    	
        g.setColor(nodeBackgroundColor);
        g.fillRoundRect(nodeRectangle.x, nodeRectangle.y, nodeRectangle.width, nodeRectangle.height, 15, 15);
        g.setStroke(new BasicStroke(2));
        
        if(!node.isDragged)
        	g.setColor(node.borderColor);
        else
        	g.setColor(Color.white);
        
        g.drawRoundRect(nodeRectangle.x, nodeRectangle.y, nodeRectangle.width, nodeRectangle.height, 15, 15);
        
        g.setColor(textColor);
        
        Font font = new Font("NotoSans", Font.BOLD, (int)(15*viewportScale));
        FontMetrics metrics = g.getFontMetrics(font);
        
        int x = nodeRectangle.x + (nodeRectangle.width - metrics.stringWidth(node.getText())) / 2;
        int y = nodeRectangle.y + ((nodeRectangle.height - metrics.getHeight()) / 2) + metrics.getAscent();
        
        g.setFont(font);
        
        g.drawString(node.getText(), x, nodeRectangle.y + metrics.getHeight() + 5);
        
        g.setColor(descriptionTextColor);
        
        Font fontDesc = new Font("NotoSans", Font.BOLD, (int)(10*viewportScale));
        FontMetrics metricsDesc = g.getFontMetrics(fontDesc);
        g.setFont(fontDesc);
        
        int counter = 1;
        for (String sub : node.getDescription().split("@n")) {
            int xDesc = nodeRectangle.x + (nodeRectangle.width - metricsDesc.stringWidth(sub)) / 2;
        	
        	g.drawString(sub, xDesc, nodeRectangle.y + metricsDesc.getHeight() * 2 + 20 * counter);
        	counter++;
		}
    }
    
    public int GetViewportX(int x) {
    	return (int)((x + viewportPosX) * viewportScale);
    }
    public int GetViewportY(int y) {
    	return (int)((y + viewportPosY) * viewportScale);
    }
    
    public int GetViewportXUnscaled(int x) {
    	return x + viewportPosX;
    }
    public int GetViewportYUnscaled(int y) {
    	return y + viewportPosY;
    }
    
    public void AddNode(CodeNode node) {
    	nodes.add(node);
    }
    
    public CodeNode CreateNode(String title, int x, int y, int width, int height, List<CodeNode> connectedNodes, Color nodeColor, String description) {
    	int sizedWidth = (int)(title.length() * 7.5f) + width;
    	int sizedHeight = 0;
    	
    	if(description == "") sizedHeight = 40;
    	else {
    		String[] lines = description.split("@n");
    		
    		sizedHeight = 45 + (20 * lines.length);
    	}
    	
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