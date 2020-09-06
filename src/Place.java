
public class Place
{
	private int r, c;
	
	public Place(int inR, int inC)
	{
		r = inR;
		c = inC;
	}
	
	public Place()
	{
		this(-1,-1);
	}
	
	public int row()
	{
		return r;
	}
	
	public int column()
	{
		return c;
	}

	public String toString()
	{
		return "["+r+","+c+"]";
	}
	
	public boolean equals(Object other)
	{
		if (! (other instanceof Place))
			return false;
		Place otherPlace = (Place)other;
		boolean match = false;
		//check whether the data in otherPlace matches the data in this Place.
		//--------------
		//TODO: insert your code here.
		if (otherPlace.row() == this.row() && otherPlace.column() == this.column()){
			return true;
		}
		//--------------
		return match;
	}	
	
	/**
	 * returns the Place that is one cell north of this one. Note: this means you are making a new Place.
	 * @return the Place north of this one.
	 */
	public Place north()
	{
		// TODO: write the code for this.
		Place northPlace = new Place(this.row()-1, this.column());
		return northPlace;
	}
	
	/**
	 * returns the Place that is one cell south of this one.
	 * @return the Place south of this one.
	 */
	public Place south()
	{
		// TODO: write the code for this.
		Place southPlace = new Place(this.row()+1, this.column());
		return southPlace;
	}
	/**
	 * returns the Place that is one cell east of this one.
	 * @return the Place east of this one.
	 */
	public Place east()
	{
		// TODO: write the code for this.
		Place eastPlace = new Place(this.row(), this.column()+1);
		return eastPlace;
	}
	/**
	 * returns the Place that is one cell west of this one.
	 * @return the Place west of this one.
	 */
	public Place west()
	{
		// TODO: write the code for this.
		Place westPlace = new Place(this.row(), this.column()-1);
		return westPlace;
	}
	
	/**
	 * determines whether the given Place is adjacent in cardinal direction from this Place.
	 * @param candidate - a location to consider.
	 * @return - whether the candidate is immediately to the north, south, east or west.
	 */
	public boolean isNeighbor(Place candidate)
	{
		boolean isNextDoor = false;
		//TODO: insert your code here.

		if (this.north().equals(candidate)){
			isNextDoor = true;
		}
		else if (this.south().equals(candidate)){
			isNextDoor = true;
		}

		else if (this.east().equals(candidate)) {
			isNextDoor = true;
		}

		else if (this.west().equals(candidate)){
			isNextDoor = true;
		}


		return isNextDoor;
	}
	
}
