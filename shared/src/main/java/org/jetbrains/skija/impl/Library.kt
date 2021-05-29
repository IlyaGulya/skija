package org.jetbrains.skija.impl

import lombok.SneakyThrows
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

object Library {
    internal @Volatile
    var _loaded = false
    fun staticLoad() {
        if (!_loaded && "false" != System.getProperty("skija.staticLoad")) load()
    }

    fun readResource(path: String?): String? {
        val url = Library::class.java.getResource(path) ?: return null
        try {
            url.openStream().use { `is` ->
                val bytes = `is`.readAllBytes()
                return String(bytes).trim { it <= ' ' }
            }
        } catch (e: IOException) {
            return null
        }
    }

    @Synchronized
    fun load() {
        if (_loaded) return
        val version = readResource("/skija.version")
        val tempDir = File(System.getProperty("java.io.tmpdir"), "skija_" + (version ?: "" + System.nanoTime()))
        val os = System.getProperty("os.name").toLowerCase()
        if (os.contains("mac") || os.contains("darwin")) {
            val file = if ("aarch64" == System.getProperty("os.arch")) "libskija_arm64.dylib" else "libskija_x64.dylib"
            val library = _extract("/", file, tempDir)
            System.load(library.absolutePath)
        } else if (os.contains("windows")) {
            _extract("/", "icudtl.dat", tempDir)
            val library = _extract("/", "skija.dll", tempDir)
            System.load(library.absolutePath)
        } else if (os.contains("nux") || os.contains("nix")) {
            val library = _extract("/", "libskija.so", tempDir)
            System.load(library.absolutePath)
        } else throw RuntimeException("Unknown operation system")
        if (tempDir.exists() && version == null) {
            Runtime.getRuntime().addShutdownHook(Thread {
                try {
                    Files.walk(tempDir.toPath())
                        .map { obj: Path -> obj.toFile() }
                        .sorted(Comparator.reverseOrder())
                        .forEach { f: File ->
                            Log.debug("Deleting $f")
                            f.delete()
                        }
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            })
        }
        _loaded = true
        _nAfterLoad()
    }

    internal @SneakyThrows
    fun _extract(resourcePath: String, fileName: String, tempDir: File): File {
        val file: File
        val url = Library::class.java.getResource(resourcePath + fileName)
        if (url == null) {
            file = File(fileName)
            require(file.exists()) { "Library file $fileName not found in $resourcePath" }
        } else if (url.protocol === "file") {
            file = File(url.toURI())
        } else {
            file = File(tempDir, fileName)
            if (!file.exists()) {
                if (!tempDir.exists()) tempDir.mkdirs()
                url.openStream().use { `is` -> Files.copy(`is`, file.toPath(), StandardCopyOption.REPLACE_EXISTING) }
            }
        }
        Log.debug("Loading $file")
        return file
    }

    internal external fun _nAfterLoad()
}