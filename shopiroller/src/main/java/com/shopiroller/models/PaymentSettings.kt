package com.shopiroller.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PaymentSettings(

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("deliveryConditions")
    val deliveryConditions: String? = null,

    @field:SerializedName("cancellationProcedure")
    val cancellationProcedure: String? = null,

    @field:SerializedName("distanceSalesContract")
    val distanceSalesContract: String? = null,

    @field:SerializedName("paymentAccounts")
    val paymentAccounts: List<BankAccount?>? = null,

    @field:SerializedName("supportedPaymentTypes")
    val supportedPaymentTypes: List<SupportedPaymentType?>? = null,

    @field:SerializedName("defaultCurrency")
    val defaultCurrency: String? = null,

    @field:SerializedName("deliveryConditionsTitle")
    val deliveryConditionsTitle: String? = null,

    @field:SerializedName("cancellationProcedureTitle")
    val cancellationProcedureTitle: String? = null

) : Serializable
