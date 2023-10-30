package com.ygorxkharo.obey.web.data.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
private data class SerializedDummyClass(
    @SerialName("question_number") val question: Int,
    @SerialName("answer") val answer: String
)

class JsonStringToObjectDeserializerTest {

    @Test
    fun test_When_DeserializingJSONStringToObject_Expect_SerializableObjectToBeReturned() {
        //Arrange
        val expectedQuestionValue = 1
        val expectedAnswerValue = "spice"
        val jsonString = """
            {
                "question_number" : 1,
                "answer": "spice"
            }
        """.trimIndent()

        //Act
        val actualOutput = JsonStringToObjectDeserializer.convertFromString(SerializedDummyClass.serializer(), jsonString)

        //Assert
        assertEquals(expectedQuestionValue, actualOutput.question)
        assertEquals(expectedAnswerValue, actualOutput.answer)
    }
}