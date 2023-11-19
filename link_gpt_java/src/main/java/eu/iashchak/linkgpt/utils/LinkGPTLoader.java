/*
 * MIT License
 *
 * Copyright (c) 2023 Andrei Iashchak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package eu.iashchak.linkgpt.utils;

import cz.adamh.utils.NativeUtils;
import eu.iashchak.crossutils.PlatformDetector;
import eu.iashchak.crossutils.Triplet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Loads the library
 */
public class LinkGPTLoader {

    static final String libDirName = "link_gpt";
    static final String libName = "link_gpt_jni";
    static final Path jarLibsDir = Paths.get("/resources", "main", "libs");
    static final Path pathLibsDir = Paths.get(System.getProperty("user.dir"), "build", "resources", "main", "libs");
    private static final Logger logger = Logger.getLogger(LinkGPTLoader.class.getName());

    /**
     * Loads the library
     */
    public static void load() {
        Triplet triplet = PlatformDetector.detectPlatform();
        String libFileName = triplet.getLibFileName(libName);
        try {
            loadFromPath(triplet, libFileName);
        } catch (IOException e) {
            logger.info("Unable to load " + libFileName + " from path. It seems that the you ran the program from the jar file. Loading from jar");
            try {
                loadFromJar(triplet, libFileName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    /**
     * Loads the library from the jar file
     * @param triplet platform triplet
     * @param lib    library name
     * @throws IOException if the library cannot be loaded
     */
    static void loadFromJar(Triplet triplet, String lib) throws IOException {
        Path path = Paths.get(jarLibsDir.toString(), libDirName, triplet.getName(), lib);

        String unixPath = path.toString().replace('\\', '/');

        logger.info(String.format("Loading %s from jar", unixPath));

        NativeUtils.loadLibraryFromJar(unixPath);
    }

    /**
     * Loads the library from the path
     * @param triplet platform triplet
     * @param lib    library name
     * @throws IOException if the library cannot be loaded
     */
    static void loadFromPath(Triplet triplet, String lib) throws UnsatisfiedLinkError, IOException {
        // check if the file exists
        Path path = Paths.get(pathLibsDir.toString(), libDirName, triplet.getName(), lib);

        logger.info(String.format("Loading %s from path", path.toAbsolutePath()));

        if (!path.toFile().exists()) {
            throw new IOException("File " + path.toAbsolutePath() + " does not exist");
        }

        System.load(path.toAbsolutePath().toString());
    }

}
