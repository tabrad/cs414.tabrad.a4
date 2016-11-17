package view;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import common.Booth;
import controller.GarageController;
import model.Location;

public class GarageView extends JPanel
{
	private static final long serialVersionUID = 1L;
	private static GarageView instance = null;
	GarageController garageController;
	private static int iconWidth = 32;
	private static int iconHeight = 32;
	Set<Location> stalls = new HashSet<Location>();
	Set<Location> roads = new HashSet<Location>();
	Booth boothEntrance;
	Booth boothExit;
	Set<Booth> booths = new HashSet<Booth>();
	
	//images
	BufferedImage imgBooth = null;
	BufferedImage imgGateClosed = null;
	BufferedImage imgGateOpen = null;
	BufferedImage imgStall = null;
	BufferedImage imgRoad = null;
	BufferedImage imgCar = null;
	BufferedImage imgFull = null;
	BufferedImage imgNotFull = null;
	
	//timer for polling the server
	Timer timer = new Timer();
	
	public static GarageView getInstance() throws RemoteException
	{
		if(instance == null)
			instance = new GarageView();
		
		return instance;
	}
	
	private GarageView() throws RemoteException
	{
		garageController = GarageController.getInstance();
		int max = garageController.getMaxOccupancy();
		System.out.println("max:"+max);
		stalls = garageController.getParkingStalls();
		roads = garageController.getRoad();
		boothEntrance = garageController.getBooth(false);
		boothExit = garageController.getBooth(true);
		booths.add(boothEntrance);
		booths.add(boothExit);
		
		loadImages();
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e)
			{
				int x = (e.getX()/iconWidth) + 1;
				int y = (e.getY()/iconHeight) + 1;
				try {
					garageController.garageClicked(x, y);
				} catch (RemoteException e1) {}
			}
		});
		
		 timer.scheduleAtFixedRate(new TimerTask() {
		        @Override
		        public void run() {
		            repaint();
		            //System.out.println("repaint called");
		        }
		    }, 1000, 1000);
	}
	
	private void loadImages()
	{
		try 
		{
		    imgBooth = ImageIO.read(GarageView.class.getResource("images/booth.png"));
		    imgGateClosed = ImageIO.read(GarageView.class.getResource("images/gate-closed.png"));
		    imgGateOpen = ImageIO.read(GarageView.class.getResource("images/gate-open.png"));
		    imgStall = ImageIO.read(GarageView.class.getResource("images/stall.png"));
		    imgRoad = ImageIO.read(GarageView.class.getResource("images/road.png"));
		    imgCar = ImageIO.read(GarageView.class.getResource("images/sports-car.png"));
		    imgFull = ImageIO.read(GarageView.class.getResource("images/full.png"));
		    imgNotFull = ImageIO.read(GarageView.class.getResource("images/not-full.png"));
		} catch (IOException e) {
		}
	}
	
	//paintComponent is called in repaint()
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//draw background grid
		for(int x = 0; x < 639; x += iconWidth)
			g.drawLine(2, x, 639, x);
		
		for(int y = 0; y < 639; y += iconHeight)
			g.drawLine(y, 2, y, 639);
		
		//paint parking stalls
		for(Location stall : stalls)
			g.drawImage(imgStall, stall.x * iconWidth - iconWidth, stall.y * iconHeight - iconHeight, null);
		
		//paint roads
		for(Location road : roads)
			g.drawImage(imgRoad, road.x * iconWidth - iconWidth, road.y * iconHeight - iconHeight, null);
		
		//paint drivers
		Set<Location> locations = new HashSet<Location>();
		try{
			locations = garageController.getDriversLocations();
		}catch(Exception e){}
		for(Location location : locations)
			g.drawImage(imgCar, location.x * iconWidth - iconWidth, location.y * iconHeight - iconHeight, null);

		//paint booths and gates
		for(Booth booth : booths)
		{
			try{
				int x = booth.getLocation().x;
				int y = booth.getLocation().y;
				g.drawImage(imgBooth, x * iconWidth - iconWidth, y * iconHeight - iconHeight, null);
				
				//put gate in the middle of the road
				y -= 1;
				x += 1;
				g.drawImage((booth.gateIsOpen() ? imgGateOpen : imgGateClosed), x * iconWidth - iconWidth, y * iconHeight - iconHeight, null);
			}catch(Exception e){}
		}
		
		//paint full sign
		g.drawImage((garageController.isFull() ? imgFull : imgNotFull), 1 * iconWidth - iconWidth, 2 * iconHeight - iconHeight, null);
	}
}
