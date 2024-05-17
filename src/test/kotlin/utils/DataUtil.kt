package utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

class DataUtil {

    object YAMLParse {
        private val mapper = let {
            val mapper = ObjectMapper(YAMLFactory())
            mapper.registerModule(KotlinModule())
            mapper
        }

        fun <T: Any> parseProp(fileName: String, prop: KClass<T>): T {
            return Files.newBufferedReader(FileSystems.getDefault().getPath(fileName)).use { mapper.readValue(it, prop.java) }
        }
    }

    @Throws(IOException::class)
    fun readTextFromFile(path: String): String? {
        return Files.readString(Paths.get(path))
    }

    fun jsonBuilder(jsonFile: String): JSONObject {
        val parser = JSONParser()
        return parser.parse(jsonFile) as JSONObject
    }

    fun updateKeyInJsonFile(jsonFile: String, key: String, value: String): JSONObject {
        val json = jsonBuilder(jsonFile)
        json[key] = value
        return json
    }

    fun removeKeyInJsonFile(jsonFile: String, key: String): JSONObject {
        val json = jsonBuilder(jsonFile)
        json.remove(key)
        return json
    }
}
