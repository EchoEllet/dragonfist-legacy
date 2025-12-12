import com.google.gson.JsonElement
import com.google.gson.JsonParser

plugins {
    alias(libs.plugins.moddevgradle) apply false
    alias(libs.plugins.kotlin.jvm) apply false
}

val modId: String by project

tasks.register("formatJson") {
    // Note: This script was generated with the assistance of AI for minor maintenance tasks only.
    // It is intended for convenience and does not affect core functionality.

    group = "formatting"
    description = "Formats JSON files with custom inline/multiline rules"

    val maxInlineLength = 80
    val indentStep = "  "

    val filePaths = listOf(
        "common/src/main/resources/data/${modId}/epicfight_mobpatch/shifu.json",
        "common/src/main/resources/data/${modId}/epicfight_mobpatch/knight.json",
        "common/src/main/templates/epicfight_mobpatch/bandit_template.json",
    )

    doLast {

        fun formatJson(element: JsonElement, indentLevel: Int = 0): String {
            val indent = indentStep.repeat(indentLevel)
            return when {
                element.isJsonObject -> {
                    val obj = element.asJsonObject
                    val entries = obj.entrySet().map { (k, v) ->
                        "\"$k\": ${formatJson(v, indentLevel + 1)}"
                    }
                    val inline = "{ ${entries.joinToString(", ")} }"
                    if (inline.length <= maxInlineLength) inline
                    else {
                        val multiLine = entries.joinToString(",\n") { "${indentStep.repeat(indentLevel + 1)}$it" }
                        "{\n$multiLine\n$indent}"
                    }
                }

                element.isJsonArray -> {
                    val arr = element.asJsonArray
                    val items = arr.map { formatJson(it, indentLevel + 1) }
                    val inline = "[ ${items.joinToString(", ")} ]"
                    if (inline.length <= maxInlineLength) inline
                    else {
                        val multiLine = items.joinToString(",\n") { "${indentStep.repeat(indentLevel + 1)}$it" }
                        "[\n$multiLine\n$indent]"
                    }
                }

                element.isJsonPrimitive -> {
                    val prim = element.asJsonPrimitive
                    if (prim.isString) "\"${prim.asString}\"" else prim.toString()
                }

                element.isJsonNull -> "null"
                else -> element.toString()
            }
        }

        for (path in filePaths) {
            val file = File(path)
            if (!file.exists()) {
                println("\"$path\" not found!")
                continue
            }

            val jsonText = file.readText()
            val jsonElement = JsonParser.parseString(jsonText)
            val formatted = formatJson(jsonElement)
            file.writeText(formatted)
            println("Formatted JSON saved to $path")
        }
    }
}
