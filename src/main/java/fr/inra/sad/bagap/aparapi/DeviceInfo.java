package fr.inra.sad.bagap.aparapi;

import com.aparapi.device.Device;
import com.aparapi.internal.kernel.KernelManager;
import com.aparapi.internal.kernel.KernelPreferences;

public class DeviceInfo {
	
	public static void main(String[] args) {
		KernelPreferences preferences = KernelManager.instance().getDefaultPreferences();
		System.out.println(KernelManager.instance().bestDevice());
		System.out.println("-- Devices in preferred order --");
		for (Device device : preferences.getPreferredDevices(null)) {
			System.out.println("----------");
			System.out.println(device);
		}
	}
}