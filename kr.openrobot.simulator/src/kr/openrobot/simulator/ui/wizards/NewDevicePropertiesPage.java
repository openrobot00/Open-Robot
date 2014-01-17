package kr.openrobot.simulator.ui.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewDevicePropertiesPage extends WizardPage{

	private ISelection selection;
	private Text jointTouqueText;
	private Text linkLengthText;
	private Text linkMassText;
	private Text jointRPMText;
	private Text jointResolutionText;
	private Text jointVoltageText;

	public static String jointTouque;
	public static String linkLength;
	public static String linkMass;
	public static String jointRPM;
	public static String jointResolution;
	public static String jointVoltage;

	protected NewDevicePropertiesPage(ISelection selection) {
		super("wizardPage");
		setTitle("Set Properties");
		setDescription("Set Link & Joint Properties");
		this.selection = selection;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();

		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 4;
		addJointProperties(container);
		addLinkProperties(container);
		dialogChanged();
		setControl(container);
	}


	private void addJointProperties(Composite container) {

		final Label jointTitleLabel = new Label(container, SWT.NULL);
		jointTitleLabel.setText("Joint Properties");
		GridData gridTitleData = new GridData(GridData.FILL_HORIZONTAL);

		gridTitleData.horizontalSpan = 2;
		gridTitleData.verticalSpan = 1;
		jointTitleLabel.setLayoutData(gridTitleData);

		final Label jointLabel = new Label(container, SWT.NULL);
		jointLabel.setText("Joint Touque (Nm): ");
		jointTouqueText = new Text(container, SWT.MULTI);
		jointTouqueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jointTouqueText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		jointTouqueText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = event.text.length() == 0
						|| Character.isDigit(event.text.charAt(0));
			}
		});

		final Label jointLabel2 = new Label(container, SWT.NULL);
		jointLabel2.setText("Joint RPM: ");
		jointRPMText = new Text(container, SWT.MULTI);
		jointRPMText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jointRPMText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		jointRPMText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = event.text.length() == 0
						|| Character.isDigit(event.text.charAt(0));
				System.out.println(jointRPMText.getText());
			}

		});

		final Label jointLabel3 = new Label(container, SWT.NULL);
		jointLabel3.setText("Joint Resolution: ");
		jointResolutionText = new Text(container, SWT.MULTI);
		jointResolutionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jointResolutionText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		jointResolutionText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = event.text.length() == 0
						|| Character.isDigit(event.text.charAt(0));
			}
		});

		final Label jointLabel4 = new Label(container, SWT.NULL);
		jointLabel4.setText("Joint Voltage (V): ");
		jointVoltageText = new Text(container, SWT.MULTI);
		jointVoltageText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jointVoltageText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		jointVoltageText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = event.text.length() == 0
						|| Character.isDigit(event.text.charAt(0));
			}
		});
	}

	private void addLinkProperties(Composite container) {

		final Label linkTitleLabel = new Label(container, SWT.NULL);
		GridData gridTitleData = new GridData(GridData.FILL_HORIZONTAL);
		linkTitleLabel.setText("Link Properties");
		gridTitleData.horizontalSpan = 2;
		gridTitleData.verticalSpan = 1;
		linkTitleLabel.setLayoutData(gridTitleData);

		final Label linkLabel = new Label(container, SWT.NULL);
		linkLabel.setText("Link Length (m): ");
		linkLengthText = new Text(container, SWT.MULTI);
		linkLengthText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		linkLengthText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		linkLengthText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = event.text.length() == 0
						|| Character.isDigit(event.text.charAt(0));
			}
		});

		final Label linkLabel2 = new Label(container, SWT.NULL);
		linkLabel2.setText("Link Weight (kg): ");
		linkMassText = new Text(container, SWT.MULTI);
		linkMassText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		linkMassText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		linkMassText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = event.text.length() == 0
						|| Character.isDigit(event.text.charAt(0));
			}
		});

		//		mcuTypeCombo = new Combo(container, SWT.READ_ONLY | SWT.DROP_DOWN);
		//		mcuTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//		mcuTypeCombo.setItems(new String[]
		//				{"ATmega128"});
		//		mcuTypeCombo.setText("ATmega128");
		//		mcuType = "ATmega128";
		//		mcuTypeCombo.addSelectionListener(new SelectionAdapter() {
		//			public void widgetSelected(SelectionEvent event) {
		//				mcuType = mcuTypeCombo.getItem(mcuTypeCombo.getSelectionIndex());
		//			}
		//		});
	}



	private void dialogChanged() {
		// TODO Auto-generated method stub
		// value is null
		if(getTextValue(jointTouqueText).length() == 0) {
			this.setPageComplete(false);
			return;
		}
		if(getTextValue(jointRPMText).length() == 0) {
			this.setPageComplete(false);
			return;
		}
		if(getTextValue(jointResolutionText).length() == 0) {
			this.setPageComplete(false);
			return;
		}
		if(getTextValue(jointVoltageText).length() == 0) {
			this.setPageComplete(false);
			return;
		}
		if(getTextValue(linkLengthText).length() == 0) {
			this.setPageComplete(false);
			return;
		}
		if(getTextValue(linkMassText).length() == 0) {
			this.setPageComplete(false);
			return;
		}

		// out of range
		if(Integer.parseInt(getTextValue(jointTouqueText)) > 100
				|| Integer.parseInt(getTextValue(jointTouqueText)) < 0)
			updateStatus("Out of Range: Joint Touque");
		if(Integer.parseInt(getTextValue(jointRPMText)) > 100
				|| Integer.parseInt(getTextValue(jointRPMText)) < 0)
			updateStatus("Out of Range: Joint RPM");
		if(Integer.parseInt(getTextValue(jointResolutionText)) > 100
				|| Integer.parseInt(getTextValue(jointResolutionText)) < 0)
			updateStatus("Out of Range: Joint Resolution");
		if(Integer.parseInt(getTextValue(jointVoltageText)) > 100
				|| Integer.parseInt(getTextValue(jointVoltageText)) < 0)
			updateStatus("Out of Range: Joint Voltage.");
		if(Integer.parseInt(getTextValue(linkLengthText)) > 100
				|| Integer.parseInt(getTextValue(linkLengthText)) < 0)
			updateStatus("Out of Range: Link Length");
		if(Integer.parseInt(getTextValue(linkMassText)) > 100
				|| Integer.parseInt(getTextValue(linkMassText)) < 0)
			updateStatus("Out of Range: Link Weight");

		// correct input
		if(Integer.parseInt(getTextValue(jointTouqueText)) < 100
				|| Integer.parseInt(getTextValue(jointTouqueText)) >= 0) {
			setDescription("Set Link & Joint Properties");
			jointTouque = getTextValue(jointTouqueText);
		}
		if(Integer.parseInt(getTextValue(jointRPMText)) < 100
				|| Integer.parseInt(getTextValue(jointRPMText)) >= 0) {
			setDescription("Set Link & Joint Properties");
			jointRPM = getTextValue(jointRPMText);
		}
		if(Integer.parseInt(getTextValue(jointResolutionText)) < 100
				|| Integer.parseInt(getTextValue(jointResolutionText)) >= 0) {
			setDescription("Set Link & Joint Properties");
			jointResolution = getTextValue(jointResolutionText);
		}
		if(Integer.parseInt(getTextValue(jointVoltageText)) < 100
				|| Integer.parseInt(getTextValue(jointVoltageText)) >= 0) {
			setDescription("Set Link & Joint Properties");
			jointVoltage = getTextValue(jointVoltageText);
		}
		if(Integer.parseInt(getTextValue(linkLengthText)) < 100
				|| Integer.parseInt(getTextValue(linkLengthText)) >= 0) {
			setDescription("Set Link & Joint Properties");
			linkLength = getTextValue(linkLengthText);
		}
		if(Integer.parseInt(getTextValue(linkMassText)) < 100
				|| Integer.parseInt(getTextValue(linkMassText)) >= 0) {
			setDescription("Set Link & Joint Properties");
			linkMass = getTextValue(linkMassText);
		}

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