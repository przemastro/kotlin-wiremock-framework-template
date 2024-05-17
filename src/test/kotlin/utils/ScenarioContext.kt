package utils

import enums.Context

class ScenarioContext {

    private var scenarioContext: MutableMap<String, Any>? = null

    fun setContext(key: Context, value: Any) {
        scenarioContext = HashMap()
        scenarioContext!![key.toString()] = value;
    }

    fun getContext(key: Context): Any? {
        return scenarioContext!![key.toString()]
    }

}