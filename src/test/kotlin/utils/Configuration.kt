package utils

import models.DataModel
import models.EnvModel

object Configuration {

    fun dataModel(): Any {
        val env = DataUtil.YAMLParse.parseProp("src/test/resources/environment.yml", EnvModel::class)
        val prop = DataUtil.YAMLParse.parseProp("src/test/resources/properties.yml", DataModel::class)
        return when (env.env) {
            "test" -> prop.test
            "dev" -> prop.dev
            "local" -> prop.local
            else -> {
                print("Select environment from test, dev or local")
            }
        }
    }

    fun envModel(): Any {
        val env = DataUtil.YAMLParse.parseProp("src/test/resources/environment.yml", EnvModel::class)
        return env.env
    }
}