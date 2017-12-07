package project4;

public class Particle
{
	private XYClass location, velocity;
	private double fitness;

	public Particle()
	{
		
	}
	
	
	private class XYClass
	{
		private double X, Y;
		
		private XYClass(double x, double y)
		{
			X = x;
			Y = y;
		}
		
		private double getX()
		{
			return X;
		}
		
		private double getY()
		{
			return Y;
		}
		
		private void setX(double x)
		{
			X = x;
		}
		
		private void setY(double y)
		{
			Y = y;
		}
	}
}