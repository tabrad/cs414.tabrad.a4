package view;

import javax.swing.ImageIcon;

public class DriverIcon extends ClickableLabel 
{
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private String license;

	public DriverIcon(String license, int x, int y) 
	{
		super(new ImageIcon(DriverIcon.class.getResource("images/sports-car.png")));
		this.license = license;
		this.x = x;
		this.y = y;
	}
	
	@Override
	protected void onClick()
	{
		DriverDialog dialog = new DriverDialog(license, x, y, false, false);
		dialog.showDialog();
	}

}
