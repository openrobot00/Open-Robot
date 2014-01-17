package kr.openrobot.simulator.core.resources;

import kr.openrobot.simulator.xml.XMLParser;


public class DeviceResources {
	// if you add some device, it count up.
	public static int deviceCount;
	
	public static  String simulType;
	public static  String deviceType;
	public static String deviceName;
	public static boolean loop = true;
	
	// LED
	public static boolean ledEnable = false;
	public static String ledPin;
	public static String ledPort;
	public static String ledDelay;
	// Switch
	public static boolean switchEnable = false;
	public static String switchPin;
	public static String switchPort;
	
	// DC Motor, Stepping Motor -- 현재 미구현
	public static boolean dcEnable = false;
	public static boolean steppingEnable = false;
	public static String timer;
	// Servo Motor
	public static boolean servoEnable = false;
	public static int upAngle;
	public static int downAngle;
	public static double angleSpeed; // NTC Servo Motor Spec = 0.17m/60˚
	
//	public DeviceResources() {
//		XMLParser parser = new XMLParser();
//		parser.readXMLFile();
//	}
}
