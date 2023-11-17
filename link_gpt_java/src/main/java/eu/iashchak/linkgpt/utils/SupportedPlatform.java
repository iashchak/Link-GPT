package eu.iashchak.linkgpt.utils;


import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Enumeration of supported platforms, each with a specific library path.
 */
public enum SupportedPlatform {
    I686AppleDarwin("i686_apple_darwin"),
    X86_64AppleDarwin("x86_64_apple_darwin"),
    Aarch64AppleDarwin("aarch64_apple_darwin"),
    I686UnknownLinuxGnu("i686_unknown_linux_gnu"),
    X86_64UnknownLinuxGnu("x86_64_unknown_linux_gnu"),
    Aarch64UnknownLinuxGnu("aarch64_unknown_linux_gnu"),
    I686PcWindowsGnu("i686_pc_windows_gnu"),
    X86_64PcWindowsGnu("x86_64-pc-windows-gnu"),
    Aarch64PcWindowsGnu("aarch64_pc_windows_gnu"),
    Aarch64LinuxAndroid("aarch64_linux_android");

    private final String platformPath;

    SupportedPlatform(String libraryPath) {
        this.platformPath = libraryPath;
    }

    public String getPlatformPath() throws URISyntaxException {
        String libsRoot = "libs\\link_gpt";
        String libName = "link_gpt_jni";
        String mappedLibName = System.mapLibraryName(libName);
        String fullPath = String.format("%s/%s/%s", libsRoot, this.platformPath, mappedLibName);
        URL resource = NativeLibrary.class.getClassLoader().getResource(fullPath);
        assert resource != null;
        return Paths.get(resource.toURI()).toAbsolutePath().toString();
    }
}