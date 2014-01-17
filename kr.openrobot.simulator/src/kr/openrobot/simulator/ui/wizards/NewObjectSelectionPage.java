package kr.openrobot.simulator.ui.wizards;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewObjectSelectionPage extends WizardPage {

	private ISelection selection;
	private Text containerText;
	
	private Combo mcuTypeCombo;
	private Combo deviceModeCombo;
	private Combo mcuPortCombo;
	private Combo devTypeCombo;
	private Text devNameText;
	private Combo devTimerCombo;
	private Text devUpAngleText;
	private Text devDownAngleText;
	private Text devSpeedText;

	private Text fileText;
	
	public String mcuType;
	public String devMode;
	public String devType;
	public String devName;
	public String devTimer;
	public String devUpAngle;
	public String devDownAngle;
	public String devSpeed;
	public String mcuPort;
	public String mcuTimerPort;
	private Combo mcuTimerPortCombo;

	/**
	 * Constructor for SampleNewWizardPage.
	 * @param selection 
	 * 
	 * @param pageName
	 */
	public NewObjectSelectionPage(ISelection selection) {
		super("wizardPage");
		setTitle("Select Simulator");
		setDescription("Select simulator model & MCU type");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 4;
		//addRobotType(container);
		addMcuType(container);
		addDeviceProperties(container);


		dialogChanged();
		setControl(container);
	}


	private void addMcuType(Composite container) {
		// TODO Auto-generated method stub
		final Label mcuLabel = new Label(container, SWT.NULL);
		mcuLabel.setText("MCU Select");
		mcuTypeCombo = new Combo(container, SWT.READ_ONLY | SWT.DROP_DOWN);
		mcuTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mcuTypeCombo.setItems(new String[]
				{"ATmega128"});
		mcuTypeCombo.setText("ATmega128");
		mcuType = "ATmega128";
		mcuTypeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				mcuType = mcuTypeCombo.getItem(mcuTypeCombo.getSelectionIndex());
			}
		});
		
		final Label devModeLabel = new Label(container, SWT.NULL);
		devModeLabel.setText("Device Mode 1: ");
		deviceModeCombo = new Combo(container, SWT.READ_ONLY | SWT.DROP_DOWN);
		deviceModeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		deviceModeCombo.setItems(new String[]
				{"ATmega128", "ATmega6", "ATmega16", "ATmega64", "ATtiny2313"});
		deviceModeCombo.setText("ATmega128");
		devMode = "ATmega128";
		deviceModeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				devMode = deviceModeCombo.getItem(deviceModeCombo.getSelectionIndex());
			}
		});
		
		final Label mcuPortLabel = new Label(container, SWT.NULL);
		mcuPortLabel.setText("IO Port: ");
		mcuPortCombo = new Combo(container, SWT.READ_ONLY | SWT.DROP_DOWN);
		mcuPortCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mcuPortCombo.setItems(new String[]
				{"PORTA", "PORTB", "PORTC", "PORTD", "PORTE", "PORTF"});
		mcuPortCombo.setText("PORTA");
		mcuPort = "PORTA";
		mcuPortCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				//dialogChanged();
				mcuPort = mcuPortCombo.getItem(mcuPortCombo.getSelectionIndex());
			}
		});
		
		final Label mcuTimerPortLabel = new Label(container, SWT.NULL);
		mcuTimerPortLabel.setText("Timer Port : ");
		mcuTimerPortCombo = new Combo(container, SWT.READ_ONLY | SWT.DROP_DOWN);
		mcuTimerPortCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mcuTimerPortCombo.setItems(new String[]
				{"Timer/Counter0", "Timer/Counter1"});
		mcuTimerPortCombo.setText("Timer/Counter0");
		mcuTimerPort = "Timer/Counter0";
		mcuTimerPortCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				mcuPort = mcuTimerPortCombo.getItem(mcuTimerPortCombo.getSelectionIndex());
			}
		});
	}

	private void addDeviceProperties(Composite container) {
		final Label devTypeLabel = new Label(container, SWT.NULL);
		devTypeLabel.setText("Device Type: ");
		devTypeCombo = new Combo(container, SWT.READ_ONLY | SWT.DROP_DOWN);
		devTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		devTypeCombo.setItems(new String[]
				{"DC Motor", "Stepping Motor", "Servo Motor"});
		devTypeCombo.setText("DC Motor");
		devType = "DC Motor";
		devTypeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				devType = devTypeCombo.getItem(devTypeCombo.getSelectionIndex());
			}
		});


		final Label devNameLabel = new Label(container, SWT.NULL);
		devNameLabel.setText("Device Name: ");
		devNameText = new Text(container, SWT.MULTI);
		devNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		devNameText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = event.text.length() == 0
						|| Character.isLetter(event.text.charAt(0));
			}
		});
		devNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
				devName = devNameText.getText();
			}
		});

		final Label devTimerLabel = new Label(container, SWT.NULL);
		devTimerLabel.setText("Timer Type: ");
		devTimerCombo = new Combo(container, SWT.READ_ONLY | SWT.DROP_DOWN);
		devTimerCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		devTimerCombo.setItems(new String[]
				{"Timer/Counter1A", "Timer/Counter1B", "Timer/Counter1C",
				"Timer/Counter3A", "Timer/Counter3B", "Timer/Counter3C"});
		devTimerCombo.setText("Timer/Counter1A");
		devTimer = "Timer/Counter1A";
		devTimerCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				devTimer = devTimerCombo.getItem(devTimerCombo.getSelectionIndex());
			}
		});



		final Label devUpAngleLabel = new Label(container, SWT.NULL);
		devUpAngleLabel.setText("Motor Up Angle: ");
		devUpAngleText = new Text(container, SWT.MULTI);
		devUpAngleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		devUpAngleText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		devUpAngleText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = event.text.length() == 0
						|| Character.isDigit(event.text.charAt(0));
			}
		});

		final Label devDownAngleLabel = new Label(container, SWT.NULL);
		devDownAngleLabel.setText("Motor Down Angle: ");
		devDownAngleText = new Text(container, SWT.MULTI);
		devDownAngleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		devDownAngleText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		devDownAngleText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = event.text.length() == 0
						|| Character.isDigit(event.text.charAt(0));
			}
		});

		final Label devSpeedLabel = new Label(container, SWT.NULL);
		devSpeedLabel.setText("Motor Speed: ");
		devSpeedText = new Text(container, SWT.MULTI);
		devSpeedText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		devSpeedText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		devSpeedText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = event.text.length() == 0
						|| Character.isDigit(event.text.charAt(0));
			}
		});

	}

	private void dialogChanged() {
		// TODO Auto-generated method stub
		// value is null
		if(getTextValue(devUpAngleText).length() == 0) {
			this.setPageComplete(false);
			return;
		}
		if(getTextValue(devDownAngleText).length() == 0) {
			this.setPageComplete(false);
			return;
		}
		if(getTextValue(devSpeedText).length() == 0) {
			this.setPageComplete(false);
			return;
		}
		if(getTextValue(devNameText).length() == 0) {
			this.setPageComplete(false);
			return;
		}
		// out of range
		if(Integer.parseInt(getTextValue(devUpAngleText)) > 90
				|| Integer.parseInt(getTextValue(devUpAngleText)) < 0)
			updateStatus("Out of Range: Up Angle");
		if(Integer.parseInt(getTextValue(devDownAngleText)) > 90
				|| Integer.parseInt(getTextValue(devDownAngleText)) < 0)
			updateStatus("Out of Range: Down Angle");
		if(Integer.parseInt(getTextValue(devSpeedText)) > 90
				|| Integer.parseInt(getTextValue(devSpeedText)) < 0)
			updateStatus("Out of Range: Motor Speed");
		// 180degree over
		if(Integer.parseInt(getTextValue(devUpAngleText)) 
				+ Integer.parseInt(getTextValue(devDownAngleText)) > 180)
			updateStatus("Must 'up angle' + 'down angle' <= 180");
		if(Integer.parseInt(getTextValue(devUpAngleText)) 
				+ Integer.parseInt(getTextValue(devDownAngleText)) < 180)
			setDescription("Select simulator model & MCU type");
		if(Character.isDigit(getTextValue(devNameText).charAt(0)))
			updateStatus("Device name first character allow only letter.");
		
		// correct input
		if(Integer.parseInt(getTextValue(devUpAngleText)) < 90
				|| Integer.parseInt(getTextValue(devUpAngleText)) >= 0) {
			setDescription("Select simulator model & MCU type");
			devUpAngle = getTextValue(devUpAngleText);
		}
		if(Integer.parseInt(getTextValue(devDownAngleText)) < 90
				|| Integer.parseInt(getTextValue(devDownAngleText)) >= 0) {
			setDescription("Select simulator model & MCU type");
			devDownAngle = getTextValue(devDownAngleText);
		}
		if(Integer.parseInt(getTextValue(devSpeedText)) < 90
				|| Integer.parseInt(getTextValue(devSpeedText)) > 0) {
			setDescription("Select simulator model & MCU type");
			devSpeed = getTextValue(devSpeedText);			
		}
		
//		if((devTimerCombo.getItem(devTimerCombo.getSelectionIndex()) == "Timer/Counter1A"
//				|| devTimerCombo.getItem(devTimerCombo.getSelectionIndex()) =="Timer/Counter1B"
//				|| devTimerCombo.getItem(devTimerCombo.getSelectionIndex()) =="Timer/Counter1C")
//				&& mcuPortCombo.getItem(mcuPortCombo.getSelectionIndex()) == "PORTB")
//				updateStatus("Already use PORTB");
//		if((devTimerCombo.getItem(devTimerCombo.getSelectionIndex()) == "Timer/Counter3A"
//				|| devTimerCombo.getItem(devTimerCombo.getSelectionIndex()) =="Timer/Counter3B"
//				|| devTimerCombo.getItem(devTimerCombo.getSelectionIndex()) =="Timer/Counter3C")
//				&& mcuPortCombo.getItem(mcuPortCombo.getSelectionIndex()) == "PORTE")
//				updateStatus("Already use PORTB");
		this.setPageComplete(false);
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getTextValue(Text text) {
		return text.getText();
	}
}