import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MazeBuilderPanel extends JPanel implements MazeConstants, MouseListener, ActionListener
{
	private MazeCell[][] theGrid;
	private int selectionMode;
	private JLabel statusLabel;
	private Place startLoc;
	private Place endLoc;
	private Deque<Place> optimal; // for part three, finding the optimal path.
	
	public MazeBuilderPanel()
	{
		super();
		theGrid = new MazeCell[NUM_ROWS][NUM_COLS];
		for (int r=0; r<NUM_ROWS; r++)
			for (int c=0; c<NUM_COLS; c++)
				theGrid[r][c] = new MazeCell(r,c);
		startLoc = new Place((int)(Math.random()*NUM_ROWS-2)+1,(int)(Math.random()*NUM_COLS-2)+1);
		theGrid[startLoc.row()][startLoc.column()].setStart(true);
		endLoc = new Place(NUM_ROWS-2,NUM_COLS-2);
		theGrid[NUM_ROWS-2][NUM_COLS-2].setEnd(true);
		addMouseListener(this);
		optimal = new ArrayDeque<>();
		selectionMode = END_MODE;
	}
	
	public void actionPerformed(ActionEvent aEvt)
	{
		System.out.println("action.");
		repaint();
	}
	
	
	/**
	 * determines whether the given place location is within the maze, but not on one of the edges.
	 * For instance, suppose you had a maze with NUM_ROWS = 6, and NUM_COLS = 7. Then
	 * the "X" values below would be the in-bounds locations.
	 *        0123456
	 *       +-------+
	 *     0 |       |
	 *     1 | XXXXX |
	 *     2 | XXXXX |
	 *     3 | XXXXX |
	 *     4 | XXXXX |
	 *     5 |       |
	 *       +-------+
	 * @param p - a place to consider.
	 * @return whether the place corresponds to one of the "X" locations above
	 */
	public boolean inBounds(Place p)
	{
		boolean isInBounds = false; // replace this line - it is just here so that the program compiles.
		//TODO: Insert your code here.

		if (p.row() < NUM_ROWS - 1 && p.row() > 0){
			if (p.column() < NUM_COLS - 1 && p.column() > 0){
				isInBounds = true;
			}
		}
		//---------------------------
		return isInBounds;
		
	}
	
	/**
	 * gets the "MazeCell" item in theGrid in the given location
	 * @param p - the location of the cell we want to get.
	 * @return - the MazeCell at this location.
	 */
	public MazeCell cellAt(Place p)
	{
		// TODO: Insert your code here.
	    MazeCell mc = theGrid[p.row()][p.column()];
	    return mc;
	}
	
	
	/**
	 * selects one Place from the list, "choices", at random, removes it from the list and returns it.
	 * @param choices - a list of Places.
	 * @return one of the items on the list of places; if the list was initially empty, returns null.
	 * postcondition: if the list had any items, it now has one fewer item than before - the item removed
	 *                 from this list is what is returned.
	 */
	public Place pickPlaceOffList(List<Place> choices)
	{
		// TODO: Insert your code here.

		int random = (int)(Math.random()*choices.size());

		if (choices.size() > 0){
			Place chosenItem = choices.get(random);
			choices.remove(random);
			return chosenItem;
		}

		else {
			return null;
		}

	}
	
	
	/**
	 * returns a list of the in-bounds places immediately next to the given place where there are cells with
	 * the given SOLID/HOLLOW state.
	 * @param p - the center of the neighborhood under consideration
	 * @param state - which type of blocks (SOLID/HOLLOW) that we want to collect.
	 * @return - a list of places that a) are in-bounds, b) have the given state, and c)  are neighbors of "p."
	 */
	public List<Place> getNeighborsOfState(Place p, int state)
	{
		ArrayList<Place> result = new ArrayList<Place>();
		// TODO: Insert your code here.

		if (inBounds(p.north()) && cellAt(p.north()).getStatus() == state){
			result.add(p.north());
		}

		if (inBounds(p.south()) && cellAt(p.south()).getStatus() == state){
			result.add(p.south());
		}

		if (inBounds(p.east()) && cellAt(p.east()).getStatus() == state){
			result.add(p.east());
		}

		if (inBounds(p.west()) && cellAt(p.west()).getStatus() == state){
			result.add(p.west());
		}

		//------------------------------------------------
		return result;
	}
	

	/**
	 * fills in the entire maze with solid rock. Typically done before generating a whole new maze.
	 * 
	 */
	public void resetMazeToSolid()
	{
		for (int r=0; r<NUM_ROWS; r++)
			for (int c=0; c<NUM_COLS; c++)
				theGrid[r][c].setStatus(SOLID);
		resetSolveStates();
		repaint();
		setStatus("Maze refilled.");
		
		
	}
	
	/**
	 * resets all the "solve" states from the last time we solved the maze.
	 */
	public void resetSolveStates()
	{
		for (int r=0;r<NUM_ROWS; r++)
			for (int c=0; c<NUM_COLS; c++)
			{
				theGrid[r][c].setPopped(false);
				theGrid[r][c].setPushed(false);
			}
		optimal.clear();
		repaint();
	}
	
	
	
	
	/**
	 * clears the entire maze and reconstructs it. 
	 */
	public void doRebuild()
	{
		resetMazeToSolid();
		// TODO: insert your code here..... You will need to create a stack or queue to do this.
		//    Note that your maze should start at startLoc, but should keep going as long as there are still spaces to consider.
		//    It does not have to end (or even reach) endLoc.

		//begins with the starting location
		Deque<Place> stack = new ArrayDeque<>();
		stack.push(startLoc);

		while (stack.size() > 0){
		//picks a place off the stack
			Place p = stack.pop();

		//checks to see if it has 2 or more hollow inbound neighbors
			if (getNeighborsOfState(p,HOLLOW).size() < 2){

			//creates a list of neighbors
				List<Place> neighbors = getNeighborsOfState(p, SOLID);

			//makes it hollow
				cellAt(p).setStatus(HOLLOW);

			//adds the other neighbors to the stack
				while (neighbors.size() > 0){
					Place n = pickPlaceOffList(neighbors);
					stack.push(n);
				}
			}
		}

		
		//-------------------------------
		setStatus("Maze rebuilt.");
	}
	


	/**
	 * attempts to find a path from the start location to the end location.
	 */
	public void doSolve()
	{
		resetSolveStates();
		setStatus("Searching maze");
		// TODO: insert your code here.... You will need to create a stack or queue to do this.
		//  you are looking for a path from startLoc to endLoc. For this "to do", don't worry about the "optimal" yet.

	//pushes the startLoc
		Deque<Place> stack = new ArrayDeque<>();
		stack.push(startLoc);
		cellAt(startLoc).setPushed(true);

		while (stack.size() > 0){

		//pops the location
			Place p = stack.pop();
			cellAt(p).setPopped(true);

		//if the location is endLoc, exit the while loop
		if (p.equals(endLoc)) {
			setStatus("Found path");
			return;

		}

		//pushes the neighbors that are HOLLOW and not pushed/popped already
			List<Place> neighbors = getNeighborsOfState(p,HOLLOW);

			for (Place n: neighbors){

				if (cellAt(n).isPushed() == false && cellAt(n).isPopped() == false){
					stack.push(n);
					cellAt(n).setPushed(true);
				}
			}

		}


		// TODO: OPTIONAL update the code you just wrote to find the "optimal" route.
		//  NOTE: there is already a Stack<Place> variable called "optimal" that is a member variable for this class (see
		//  line 22), and if you put places in "optimal", those cells will automatically be drawn in red.
		

		//--------------------------------------
		setStatus("No Path found."); // you may want to add a happier message elsewhere in your code!
		repaint();
		return;
	}
	
	/**
	 * draws the maze in the main part of the window.
	 */
	public void paintComponent(Graphics g)
	{
		System.out.println("painting.");
		for (int r=0; r<NUM_ROWS; r++)
			for (int c=0; c<NUM_COLS; c++)
			{
				theGrid[r][c].drawSelf(g);
				
				// ---------------- Only used in part D ---------------------
				// draws a red circle in all "optimal" cells.
				if (optimal.contains(new Place(r,c)))
				{
					g.setColor(Color.RED);
					g.drawOval(c*CELL_SIZE+CELL_SIZE/2-OPTIMAL_PATH_CIRCLE_RADIUS, 
							   r*CELL_SIZE+CELL_SIZE/2-OPTIMAL_PATH_CIRCLE_RADIUS, 
							   2*OPTIMAL_PATH_CIRCLE_RADIUS,
							   2*OPTIMAL_PATH_CIRCLE_RADIUS);
				}
				// ----------------------------------------------------------
			}
	}

	
// ---------------------------   GUI stuff  ---------------------------------------------
	/**
	 * used by the frame to tell this class about the status label. You shouldn't need to mess with this.
	 * @param lab
	 */
	public void attachStatusLabel(JLabel lab)
	{
		statusLabel = lab;
	}
	/**
	 * Allows the program to display a short status message in the bottom of the window.
	 * @param stat
	 */
	public void setStatus(String stat)
	{
		statusLabel.setText(stat);
		statusLabel.repaint();
	}
	
	/**
	 * accessor/modifier for the selectionMode. If this is in START_MODE, then clicking in the maze should reset the
	 * starting location in the maze. If this is in END_MODE, then clicking in the maze should reset the ending
	 * location in the maze.
	 * @return
	 */
	public int getSelectionMode()
	{
		return selectionMode;
	}
	public void setSelectionMode(int selectionMode)
	{
		System.out.println("MBP: setting selection mode: "+selectionMode);
		this.selectionMode = selectionMode;
	}
	
	// ---------------------------- used MouseListener methods -------------------------
	@Override
	public void mouseReleased(MouseEvent e)
	{
		System.out.println("dealing with mouse click in panel.");
		// TODO Auto-generated method stub
		int r = e.getY()/CELL_SIZE;
		int c = e.getX()/CELL_SIZE;
		Place clickedPlace = new Place(r,c);
		System.out.println(clickedPlace);
		if (! inBounds(clickedPlace))
		{
			setStatus("Invalid location");
			return; // this isn't eligible
		}
		if (selectionMode==START_MODE)
		{
			if (startLoc.equals(clickedPlace))
				return; // no change.
			theGrid[startLoc.row()][startLoc.column()].setStart(false);
			theGrid[clickedPlace.row()][clickedPlace.column()].setStart(true);
			startLoc = clickedPlace;
			setStatus("Start moved: "+startLoc);
			resetSolveStates();
			repaint();
		}
		else if (selectionMode==END_MODE)
		{
			if (endLoc.equals(clickedPlace))
				return; // no change.
			theGrid[endLoc.row()][endLoc.column()].setEnd(false);
			theGrid[clickedPlace.row()][clickedPlace.column()].setEnd(true);
			endLoc = clickedPlace;
			setStatus("End moved: "+endLoc);
			resetSolveStates();
			repaint();
		}
	}
	
	// ------------------------------------  unused MouseListener methods.-----------------------------
	@Override
	public void mouseClicked(MouseEvent e)
	{	}
	@Override
	public void mousePressed(MouseEvent e)
	{	}
	@Override
	public void mouseEntered(MouseEvent e)
	{ }
	@Override
	public void mouseExited(MouseEvent e)
	{ }
	
	

}
