package eu.iashchak.linkgpt.utils;

import eu.iashchak.linkgpt.errors.UnsupportedOperatingSystemException;

import java.net.URISyntaxException;


/**
 * Utility class to handle loading of native libraries based on the operating system and architecture.
 */
public class NativeLibrary {
    /**
     * Loads the required native library based on the detected operating system and architecture.
     *
     * @throws SecurityException                   if a security manager exists and its checkLink method doesn't allow loading of the specified dynamic library.
     * @throws UnsatisfiedLinkError                if the file does not exist.
     * @throws UnsupportedOperatingSystemException if the operating system is not supported.
     */
    public static String getLibraryPath() throws SecurityException, UnsatisfiedLinkError, UnsupportedOperatingSystemException, URISyntaxException {
        SupportedPlatform supportedPlatform = getOperatingSystem();
        return supportedPlatform.getPlatformPath();
    }

    /**
     * Detects the operating system and architecture to determine the appropriate supported platform.
     *
     * @return SupportedPlatform enum representing the detected platform.
     * @throws UnsupportedOperatingSystemException if the operating system or architecture is not supported.
     */
    private static SupportedPlatform getOperatingSystem() throws UnsupportedOperatingSystemException {
        String osName = System.getProperty("os.name").toLowerCase();
        String osArch = System.getProperty("os.arch").toLowerCase();
        String vmName = System.getProperty("java.vm.name").toLowerCase();
        if (vmName.contains("dalvik") && osArch.contains("aarch64")) {
            return SupportedPlatform.Aarch64LinuxAndroid;
        } else if (osName.contains("win")) {
            if (osArch.contains("86")) {
                return SupportedPlatform.I686PcWindowsGnu;
            } else if (osArch.contains("amd64") || osArch.contains("x86_64")) {
                return SupportedPlatform.X86_64PcWindowsGnu;
            } else if (osArch.contains("aarch64")) {
                return SupportedPlatform.Aarch64PcWindowsGnu;
            }
        } else if (osName.contains("mac")) {
            if (osArch.contains("x86_64")) {
                return SupportedPlatform.X86_64AppleDarwin;
            } else if (osArch.contains("aarch64")) {
                return SupportedPlatform.Aarch64AppleDarwin;
            }
        } else if (osName.contains("nux")) {
            if (osArch.contains("86")) {
                return SupportedPlatform.I686UnknownLinuxGnu;
            } else if (osArch.contains("amd64") || osArch.contains("x86_64")) {
                return SupportedPlatform.X86_64UnknownLinuxGnu;
            } else if (osArch.contains("aarch64")) {
                return SupportedPlatform.Aarch64UnknownLinuxGnu;
            }
        }

        throw new UnsupportedOperatingSystemException(String.format("OS not supported: %s, Arch: %s, VM: %s", osName, osArch, vmName));
    }

}
