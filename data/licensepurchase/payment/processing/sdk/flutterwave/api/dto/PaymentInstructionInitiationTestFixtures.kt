package com.ygorxkharo.obey.web.data.licensepurchase.payment.processing.sdk.flutterwave.api.dto

object PaymentInstructionInitiationTestFixtures {

    val paymentRequestJsonString = """{"tx_ref":"tx_2","amount":20,"currency":"ZAR","country":"ZA","redirect_url":"https://webhook.site/9d0b00ba-9a69-44fa-a43d-a82c33c36fdc","payment_options":" ","payment_plan":"13458","customer":{"email":"markbolo20@gmail.com","phone_number":"0905837465","name":"Mark Bolo"},"customizations":{"title":"Pied Piper Payments","description":"Middleout isn't free. Pay the price","logo":"https://assets.piedpiper.com/logo.png"}}""".trimIndent()
    val paymentResponseJsonString = """{"status":"success","message":"Hosted Link","data":{"link":"https://ravemodal-dev.herokuapp.com/v3/hosted/pay/a04a10d191246aab0101"}}""".trimIndent()
}