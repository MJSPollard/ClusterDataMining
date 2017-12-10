package project4;

public class Particle
{
	private XYClass location;
	private speedClass speed;
	private double fitness;

	public Particle()
	{
		
	}
	
	public double getFitness() {
		generateFitness();
		return fitness;
	}
	
	public void generateFitness() {
		double x = this.location.getX();
		double y = this.location.getY();
		
		 fitness = Math.pow((2.8125 - x + x * Math.pow(y, 4)), 2) +
				 Math.pow((2.25 - x + x * Math.pow(y, 2)), 2) +
				 Math.pow((1.5 - x + x * y), 2);
	}
	
	public void setLocation(XYClass location) {
		this.location = location;
	}
	
	public XYClass getLocation() {
		return location;
	}
	
	public void setSpeed(speedClass speed) {
		this.speed = speed;
	}
	
	public speedClass getSpeed() {
		return speed;
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
	
	private class speedClass
	{
		private double SX;
		private double SY;
		
		private speedClass(double sx, double sy) 
		{
			SX = sx;
			SY = sy;
		}
		
		private double getSX()
		{
			return SX;
		}
		
		private double getSY()
		{
			return SY;
		}
		
		private void setSX(double sx)
		{
			SX = sx;
		}
		
		private void setSY(double sy)
		{
			SY = sy;
		}
	}
}