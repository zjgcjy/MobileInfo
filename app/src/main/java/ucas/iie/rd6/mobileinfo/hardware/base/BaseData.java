package ucas.iie.rd6.mobileinfo.hardware.base;

public class BaseData {
    public static final String UNKNOWN_PARAM = "$unknown";
    public static class Bluetooth {
        public static final String BLUETOOTH_ADDRESS = "bluetoothAddress";
        public static final String IS_ENABLED = "isEnabled";
        public static final String DEVICE = "device";
        public static final String PHONE_NAME = "phoneName";

        public static class Device {
            public static String ADDRESS = "address";
            public static String NAME = "name";
        }
    }

    public static class Cpu {
        public static final String CPU_NAME = "cpuName";
        public static final String CPU_PART = "cpuPart";
        public static final String BOGO_MIPS="bogoMIPs";
        public static final String FEATURES="features";
        public static final String CPU_IMPLEMENTER="cpuImplementer";
        public static final String CPU_ARCHITECTURE="cpuArchitecture";
        public static final String CPU_VARIANT="cpuVariant";
        public static final String CPU_FREQ = "cpuFreq";
        public static final String CPU_MAX_FREQ = "cpuMaxFreq";
        public static final String CPU_MIN_FREQ = "cpuMinFreq";
        public static final String CPU_HARDWARE = "cpuHardware";
        public static final String CPU_CORES = "cpuCores";
        public static final String CPU_TEMP = "cpuTemp";
        public static final String CPU_ABI = "cpuAbi";
    }
}