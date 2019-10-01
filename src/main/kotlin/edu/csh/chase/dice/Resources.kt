package edu.csh.chase.dice

import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringWriter
import java.lang.Exception
import java.nio.file.Paths

object Resources {

    val s: String = File.separator

    fun load(name: String): InputStream {
        return try{
            javaClass.classLoader.getResourceAsStream(name)
        }catch (e: Exception) {
            val f = goUp(File(javaClass.getResource(s).path))

            return Paths.get(f.path, "src${s}main${s}resources$s", name).toUri().toURL().openStream()
        }
    }

    //Dirty hack, oh well
    private fun goUp(file: File): File {
        if (file.isDirectory && file.listFiles().any { it.name == "src" }) {
            return file
        }

        return goUp(file.parentFile)
    }

}
