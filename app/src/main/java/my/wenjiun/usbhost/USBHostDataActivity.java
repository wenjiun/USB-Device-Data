package my.wenjiun.usbhost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class USBHostDataActivity extends Activity {

	private TextView textViewInfo;
	private UsbManager mUsbManager;
	protected UsbInterface intf;
	protected int type;
	protected int usbNumber = 0;
	protected String vendorIdHex;
	protected String productIdHex;
	private String data = "";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		textViewInfo = (TextView) findViewById(R.id.textViewInfo);	
		Button buttonGet = (Button)findViewById(R.id.buttonGet);
		
		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		if(mUsbManager == null ){
			textViewInfo.setText(R.string.usb_host_not_supported);
			buttonGet.setEnabled(false);
		}
		
		buttonGet.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
				usbNumber  = deviceList.size();
				if(usbNumber==0) {
					textViewInfo.setText(R.string.no_usb_device_attached);										
				} else {					
					StringBuilder builder = new StringBuilder();
					builder.append("USB device count: " + usbNumber + "\n");					
					for(int k=0;k<usbNumber;k++) {
						UsbDevice device = deviceList.get((String)(deviceList.keySet().toArray())[k]);
						builder.append("\nUSB Device ID: " + device.getDeviceId() + "\n");
						builder.append("Name: "  + device.getDeviceName() + "\n");
						int vendorId = device.getVendorId();
						vendorIdHex = Integer.toHexString(vendorId);
						switch(vendorIdHex.length()) {
						case 1:
							vendorIdHex = "000" + vendorIdHex;
							break;
						case 2:
							vendorIdHex = "00" + vendorIdHex;
							break;
						case 3:
							vendorIdHex = "0" + vendorIdHex;
							break;
						}
						builder.append("Vendor ID: "  + vendorId + " (0x" + vendorIdHex + ")\n");
						int productId = device.getProductId();
						productIdHex = Integer.toHexString(productId);
						switch(productIdHex.length()) {
						case 1:
							productIdHex = "000" + productIdHex;
							break;
						case 2:
							productIdHex = "00" + productIdHex;
							break;
						case 3:
							productIdHex = "0" + productIdHex;
							break;
						}
						builder.append("Product ID: "  + productId + " (0x" + productIdHex + ")\n");
						int usbClass = device.getDeviceClass();
						switch(usbClass) {
						case 1:
							builder.append("Class: "  + usbClass  + " (Audio)\n");
							break;
						case 2:
							builder.append("Class: "  + usbClass  + " (Communications and CDC Control)\n");
							break;
						case 3:
							builder.append("Class: "  + usbClass  + " (Human Interface Device)\n");
							break;
						case 5:
							builder.append("Class: "  + usbClass  + " (Physical)\n");
							break;		
						case 6:
							builder.append("Class: "  + usbClass  + " (Image)\n");
							break;
						case 7:
							builder.append("Class: "  + usbClass  + " (Printer)\n");
							break;
						case 8:
							builder.append("Class: "  + usbClass  + " (Mass Storage)\n");
							break;
						case 9:
							builder.append("Class: "  + usbClass  + " (Hub)\n");
							break;
						case 10:
							builder.append("Class: "  + usbClass  + " (CDC-Data)\n");
							break;
						case 11:
							builder.append("Class: "  + usbClass  + " (Smart Card)\n");
							break;
						case 13:
							builder.append("Class: "  + usbClass  + " (Content Security)\n");
							break;
						case 14:
							builder.append("Class: "  + usbClass  + " (Video)\n");
							break;
						case 15:
							builder.append("Class: "  + usbClass  + " (Personal Healthcare)\n");
							break;
						case 16:
							builder.append("Class: "  + usbClass  + " (Audio/Video Devices)\n");
							break;
						case 220:
							builder.append("Class: "  + usbClass  + " (Diagnostic Device)\n");
							break;
						case 224:
							builder.append("Class: "  + usbClass  + " (Wireless Controller)\n");
							break;
						case 239:
							builder.append("Class: "  + usbClass  + " (Miscellaneous)\n");
							break;
						case 254:
							builder.append("Class: "  + usbClass  + " (Application Specific)\n");
							break;							
						case 255:
							builder.append("Class: "  + usbClass  + " (Vendor Specific)\n");
							break;	
						default:
							builder.append("Class: "  + usbClass + "\n");
							break;
						}
						builder.append("Subclass: "  + device.getDeviceSubclass() + "\n");
						builder.append("Protocol: "  + device.getDeviceProtocol() + "\n");

						int interfaceCount = device.getInterfaceCount();
						builder.append("Interface count: " + interfaceCount + "\n");
						for (int i = 0; i < interfaceCount; i++) {
							intf = device.getInterface(i);
							builder.append("\n\tUSB Interface ID: " + intf.getId() + "\n");
							int interfaceClass = intf.getInterfaceClass();
							switch(interfaceClass) {
							case 1:
								builder.append("\tClass: "  + interfaceClass  + " (Audio)\n");
								break;
							case 2:
								builder.append("\tClass: "  + interfaceClass  + " (Communications and CDC Control)\n");
								break;
							case 3:
								builder.append("\tClass: "  + interfaceClass  + " (Human Interface Device)\n");
								break;
							case 5:
								builder.append("\tClass: "  + interfaceClass  + " (Physical)\n");
								break;						
							case 6:
								builder.append("\tClass: "  + interfaceClass  + " (Image)\n");
								break;
							case 7:
								builder.append("\tClass: "  + interfaceClass  + " (Printer)\n");
								break;
							case 8:
								builder.append("\tClass: "  + interfaceClass  + " (Mass Storage)\n");
								break;
							case 9:
								builder.append("\tClass: "  + interfaceClass  + " (Hub)\n");
								break;
							case 10:
								builder.append("\tClass: "  + interfaceClass  + " (CDC-Data)\n");
								break;
							case 11:
								builder.append("\tClass: "  + interfaceClass  + " (Smart Card)\n");
								break;
							case 13:
								builder.append("\tClass: "  + interfaceClass  + " (Content Security)\n");
								break;
							case 14:
								builder.append("\tClass: "  + interfaceClass  + " (Video)\n");
								break;
							case 15:
								builder.append("\tClass: "  + interfaceClass  + " (Personal Healthcare)\n");
								break;
							case 16:
								builder.append("\tClass: "  + interfaceClass  + " (Audio/Video Devices)\n");
								break;
							case 220:
								builder.append("\tClass: "  + interfaceClass  + " (Diagnostic Device)\n");
								break;
							case 224:
								builder.append("\tClass: "  + interfaceClass  + " (Wireless Controller)\n");
								break;
							case 239:
								builder.append("\tClass: "  + interfaceClass  + " (Miscellaneous)\n");
								break;
							case 254:
								builder.append("\tClass: "  + interfaceClass  + " (Application Specific)\n");
								break;							
							case 255:
								builder.append("\tClass: "  + interfaceClass  + " (Vendor Specific)\n");
								break;							
							default:
								builder.append("\tClass: "  + interfaceClass + "\n");
								break;
							}
							builder.append("\tSubclass: "  + intf.getInterfaceSubclass() + "\n");
							builder.append("\tProtocol: "  + intf.getInterfaceProtocol() + "\n");
							int endpointCount = intf.getEndpointCount();
							builder.append("\tEndpoint count: " + endpointCount + "\n");
							for (int j = 0; j < endpointCount; j++) {
								UsbEndpoint ep = intf.getEndpoint(j);
								builder.append("\n\t\tEndpoint No: "  + ep.getEndpointNumber() + "\n");
								int direction = ep.getDirection();
								switch(direction) {
								case UsbConstants.USB_DIR_IN:
									builder.append("\t\tDirection: "  + direction + " (device to host)\n");
									break;
								case UsbConstants.USB_DIR_OUT:
									builder.append("\t\tDirection: "  + direction + " (host to device)\n");
									break;
								}
								builder.append("\t\tAddress: "  + ep.getAddress() + "\n");
								builder.append("\t\tAttributes: "  + ep.getAttributes() + "\n");
								builder.append("\t\tMax Packet Size: "  + ep.getMaxPacketSize() + "\n");
								builder.append("\t\tInterval: "  + ep.getInterval() + "\n");
								type = ep.getType();
								String typeString = "";
								switch (type) {
								case UsbConstants.USB_ENDPOINT_XFER_CONTROL:
									typeString = " (control)";
									break;
								case UsbConstants.USB_ENDPOINT_XFER_ISOC:
									typeString = " (isochronous)";
									break;
								case UsbConstants.USB_ENDPOINT_XFER_BULK:
									typeString = " (bulk)";
									break;
								case UsbConstants.USB_ENDPOINT_XFER_INT:
									typeString = " (interrupt)";
									break;
								}
								builder.append("\t\tEndpoint type: " + type + typeString
										+ "\n");
							}
						}						
					}
					data = builder.toString();
					textViewInfo.setText(data);	
				}
			}
		});

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.export:
			if(usbNumber == 0) {
				Toast.makeText(USBHostDataActivity.this, "No data to export", Toast.LENGTH_SHORT).show();
			} else {
				Date currentDate = new Date();
				SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
				String stringDate = s.format(currentDate);
				String filepath = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/usb_device_" + stringDate  + ".txt";
				FileWriter fw;
				try {
					fw = new FileWriter(filepath);
					fw.write(data);
					fw.close();	
					Toast.makeText(USBHostDataActivity.this, "Data written to " + filepath, Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					Toast.makeText(USBHostDataActivity.this, "Failed to export", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

			}
			break;
		case R.id.share:
			if(usbNumber == 0) {
				Toast.makeText(USBHostDataActivity.this, "No data to share", Toast.LENGTH_SHORT).show();
			} else {
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "USB device data");
				shareIntent.putExtra(Intent.EXTRA_TEXT, data);
				startActivity(shareIntent);
			}
			break;
		}
		return true;
	}
}