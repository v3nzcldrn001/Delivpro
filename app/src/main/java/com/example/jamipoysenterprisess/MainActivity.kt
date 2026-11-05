package com.example.jamipoysenterprises

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JamipoysEnterprisesApp(
                onSendEmail = { emailSubject, emailBody ->
                    sendResultEmail(
                        subject = emailSubject,
                        body = emailBody
                    )
                }
            )
        }
    }

    private fun sendResultEmail(
        subject: String,
        body: String
    ) {
        try {

            /*
             * Open the user's email application.
             *
             * The recipient is already set to the
             * Jamipoy's Enterprises admin email.
             */

            val emailIntent = Intent(
                Intent.ACTION_SENDTO
            ).apply {

                data = Uri.parse(
                    "mailto:yuweeisla@gmail.com"
                )

                putExtra(
                    Intent.EXTRA_SUBJECT,
                    subject
                )

                putExtra(
                    Intent.EXTRA_TEXT,
                    body
                )
            }

            startActivity(emailIntent)

        } catch (exception: Exception) {

            /*
             * If no email application is installed,
             * try the general email sharing intent.
             */

            val fallbackIntent = Intent(
                Intent.ACTION_SEND
            ).apply {

                type = "message/rfc822"

                putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf("yuweeisla@gmail.com")
                )

                putExtra(
                    Intent.EXTRA_SUBJECT,
                    subject
                )

                putExtra(
                    Intent.EXTRA_TEXT,
                    body
                )
            }

            startActivity(
                Intent.createChooser(
                    fallbackIntent,
                    "Send Result"
                )
            )
        }
    }
}


// ============================================================
// MAIN APP
// ============================================================

@Composable
fun JamipoysEnterprisesApp(
    onSendEmail: (String, String) -> Unit
) {

    var currentScreen by remember {
        mutableStateOf("welcome")
    }

    // --------------------------------------------------------
    // DELIVERY INFORMATION
    // --------------------------------------------------------

    var consigneeName by remember {
        mutableStateOf("")
    }

    var stsDrNumber by remember {
        mutableStateOf("")
    }

    var deliveryDate by remember {
        mutableStateOf("")
    }

    var plateNumber by remember {
        mutableStateOf("")
    }

    var truckTemp by remember {
        mutableStateOf("")
    }

    var sealNumber by remember {
        mutableStateOf("")
    }


    // --------------------------------------------------------
    // PRODUCTION INFORMATION
    // --------------------------------------------------------

    var totalPlantKilos by remember {
        mutableStateOf("")
    }

    var plantHds by remember {
        mutableStateOf("")
    }

    var totalHdsReceived by remember {
        mutableStateOf("")
    }


    // --------------------------------------------------------
    // RECEIVED KILOS ITEMS
    // --------------------------------------------------------

    val receivedKilosItems = remember {
        mutableStateListOf("")
    }


    // --------------------------------------------------------
    // CALCULATED VALUES
    // --------------------------------------------------------

    var totalReceivedKilos by remember {
        mutableStateOf(0.0)
    }

    var lossWeight by remember {
        mutableStateOf(0.0)
    }

    var variance by remember {
        mutableStateOf(0.0)
    }


    // --------------------------------------------------------
    // CONFIRMATION DIALOG
    // --------------------------------------------------------

    var showConfirmDialog by remember {
        mutableStateOf(false)
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF7F9F8)
    ) {

        when (currentScreen) {

            // =================================================
            // WELCOME
            // =================================================

            "welcome" -> {

                WelcomeScreen(
                    onStartClick = {
                        currentScreen = "input"
                    }
                )
            }


            // =================================================
            // INPUT
            // =================================================

            "input" -> {

                InputScreen(

                    consigneeName = consigneeName,

                    onConsigneeNameChange = {
                        consigneeName = it
                    },

                    stsDrNumber = stsDrNumber,

                    onStsDrNumberChange = {
                        stsDrNumber = it
                    },

                    deliveryDate = deliveryDate,

                    onDeliveryDateChange = {
                        deliveryDate = it
                    },

                    plateNumber = plateNumber,

                    onPlateNumberChange = {
                        plateNumber = it
                    },

                    truckTemp = truckTemp,

                    onTruckTempChange = {
                        truckTemp = it
                    },

                    sealNumber = sealNumber,

                    onSealNumberChange = {
                        sealNumber = it
                    },

                    totalPlantKilos = totalPlantKilos,

                    onTotalPlantKilosChange = {
                        totalPlantKilos = it
                    },

                    receivedKilosItems = receivedKilosItems,

                    plantHds = plantHds,

                    onPlantHdsChange = {
                        plantHds = it
                    },

                    totalHdsReceived = totalHdsReceived,

                    onTotalHdsReceivedChange = {
                        totalHdsReceived = it
                    },

                    onSubmitClick = {

                        val plantTotal =
                            totalPlantKilos.toDoubleOrNull()
                                ?: 0.0

                        val receivedTotal =
                            receivedKilosItems.sumOf {
                                it.toDoubleOrNull()
                                    ?: 0.0
                            }

                        val calculatedLossWeight =
                            plantTotal - receivedTotal

                        val calculatedVariance =
                            if (plantTotal > 0.0) {

                                (
                                        calculatedLossWeight /
                                                plantTotal
                                        ) * 100.0

                            } else {

                                0.0
                            }

                        totalReceivedKilos =
                            receivedTotal

                        lossWeight =
                            calculatedLossWeight

                        variance =
                            calculatedVariance

                        currentScreen =
                            "calculation"
                    }
                )
            }


            // =================================================
            // CALCULATION
            // =================================================

            "calculation" -> {

                CalculationScreen(

                    totalPlantKilos =
                        totalPlantKilos.toDoubleOrNull()
                            ?: 0.0,

                    totalReceivedKilos =
                        totalReceivedKilos,

                    lossWeight =
                        lossWeight,

                    variance =
                        variance,

                    onBackClick = {
                        currentScreen = "input"
                    },

                    onContinueClick = {
                        currentScreen = "result"
                    }
                )
            }


            // =================================================
            // RESULTS
            // =================================================

            "result" -> {

                ResultScreen(

                    consigneeName =
                        consigneeName,

                    stsDrNumber =
                        stsDrNumber,

                    deliveryDate =
                        deliveryDate,

                    plateNumber =
                        plateNumber,

                    truckTemp =
                        truckTemp,

                    sealNumber =
                        sealNumber,

                    totalPlantKilos =
                        totalPlantKilos.toDoubleOrNull()
                            ?: 0.0,

                    receivedKilosItems =
                        receivedKilosItems,

                    totalReceivedKilos =
                        totalReceivedKilos,

                    plantHds =
                        plantHds.toDoubleOrNull()
                            ?: 0.0,

                    totalHdsReceived =
                        totalHdsReceived.toDoubleOrNull()
                            ?: 0.0,

                    lossWeight =
                        lossWeight,

                    variance =
                        variance,

                    onNewEntryClick = {

                        consigneeName = ""
                        stsDrNumber = ""
                        deliveryDate = ""
                        plateNumber = ""
                        truckTemp = ""
                        sealNumber = ""

                        totalPlantKilos = ""
                        plantHds = ""
                        totalHdsReceived = ""

                        receivedKilosItems.clear()
                        receivedKilosItems.add("")

                        totalReceivedKilos = 0.0
                        lossWeight = 0.0
                        variance = 0.0

                        currentScreen = "input"
                    },

                    onConfirmClick = {

                        showConfirmDialog = true
                    }
                )
            }
        }
    }


    // ============================================================
    // CONFIRMATION DIALOG
    // ============================================================

    if (showConfirmDialog) {

        AlertDialog(

            onDismissRequest = {
                showConfirmDialog = false
            },

            title = {

                Text(
                    text = "Confirm Result",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F5E43)
                )
            },

            text = {

                Text(
                    text =
                        "Are you sure you want to confirm this result and send it to the admin?\n\n" +
                                "Admin Email:\n" +
                                "yuweeisla@gmail.com"
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        showConfirmDialog = false


                        // ------------------------------------
                        // CREATE EMAIL SUBJECT
                        // ------------------------------------

                        val emailSubject =
                            "Jamipoy's Enterprises - Confirmed Production Result"


                        // ------------------------------------
                        // CREATE EMAIL BODY
                        // ------------------------------------

                        val emailBody =
                            buildResultEmail(

                                consigneeName =
                                    consigneeName,

                                stsDrNumber =
                                    stsDrNumber,

                                deliveryDate =
                                    deliveryDate,

                                plateNumber =
                                    plateNumber,

                                truckTemp =
                                    truckTemp,

                                sealNumber =
                                    sealNumber,

                                totalPlantKilos =
                                    totalPlantKilos.toDoubleOrNull()
                                        ?: 0.0,

                                receivedKilosItems =
                                    receivedKilosItems,

                                totalReceivedKilos =
                                    totalReceivedKilos,

                                plantHds =
                                    plantHds.toDoubleOrNull()
                                        ?: 0.0,

                                totalHdsReceived =
                                    totalHdsReceived.toDoubleOrNull()
                                        ?: 0.0,

                                lossWeight =
                                    lossWeight,

                                variance =
                                    variance
                            )


                        // ------------------------------------
                        // OPEN EMAIL APP
                        // ------------------------------------

                        onSendEmail(
                            emailSubject,
                            emailBody
                        )
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFF1F5E43),
                        contentColor =
                            Color.White
                    )
                ) {

                    Text(
                        text = "CONFIRM & SEND",
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {
                        showConfirmDialog = false
                    }

                ) {

                    Text(
                        text = "CANCEL",
                        color = Color(0xFF1F5E43)
                    )
                }
            }
        )
    }
}


// ============================================================
// CREATE EMAIL BODY
// ============================================================

fun buildResultEmail(

    consigneeName: String,

    stsDrNumber: String,

    deliveryDate: String,

    plateNumber: String,

    truckTemp: String,

    sealNumber: String,

    totalPlantKilos: Double,

    receivedKilosItems: List<String>,

    totalReceivedKilos: Double,

    plantHds: Double,

    totalHdsReceived: Double,

    lossWeight: Double,

    variance: Double

): String {

    val builder =
        StringBuilder()


    builder.append(
        "JAMIPOY'S ENTERPRISES\n"
    )

    builder.append(
        "CONFIRMED PRODUCTION RESULT\n"
    )

    builder.append(
        "====================================\n\n"
    )


    // ========================================================
    // DELIVERY INFORMATION
    // ========================================================

    builder.append(
        "DELIVERY INFORMATION\n"
    )

    builder.append(
        "------------------------------------\n"
    )

    builder.append(
        "CONSIGNEE NAME: "
    )

    builder.append(
        displayOptional(consigneeName)
    )

    builder.append("\n")


    builder.append(
        "STS/DR#: "
    )

    builder.append(
        displayOptional(stsDrNumber)
    )

    builder.append("\n")


    builder.append(
        "DELIVERY DATE: "
    )

    builder.append(
        displayOptional(deliveryDate)
    )

    builder.append("\n")


    builder.append(
        "PLATE NUMBER: "
    )

    builder.append(
        displayOptional(plateNumber)
    )

    builder.append("\n")


    builder.append(
        "TRUCK TEMP: "
    )

    if (truckTemp.isBlank()) {

        builder.append("—")

    } else {

        builder.append(
            formatNumber(
                truckTemp.toDoubleOrNull()
                    ?: 0.0
            )
        )

        builder.append(
            " °C"
        )
    }

    builder.append("\n")


    builder.append(
        "SEAL#: "
    )

    builder.append(
        displayOptional(sealNumber)
    )

    builder.append("\n\n")


    // ========================================================
    // PRODUCTION RESULTS
    // ========================================================

    builder.append(
        "PRODUCTION RESULTS\n"
    )

    builder.append(
        "------------------------------------\n"
    )


    builder.append(
        "Total Plant Kilos: "
    )

    builder.append(
        formatKg(totalPlantKilos)
    )

    builder.append("\n\n")


    builder.append(
        "RECEIVED KILOS PER ITEM:\n"
    )


    receivedKilosItems.forEachIndexed {
            index,
            itemValue ->

        val itemKilos =
            itemValue.toDoubleOrNull()
                ?: 0.0

        builder.append(
            "Item ${index + 1}: "
        )

        builder.append(
            formatKg(itemKilos)
        )

        builder.append("\n")
    }


    builder.append("\n")


    builder.append(
        "Total Received Kilos: "
    )

    builder.append(
        formatKg(totalReceivedKilos)
    )

    builder.append("\n")


    builder.append(
        "Plant HDS: "
    )

    builder.append(
        formatNumber(plantHds)
    )

    builder.append("\n")


    builder.append(
        "Total HDS Received: "
    )

    builder.append(
        formatNumber(totalHdsReceived)
    )

    builder.append("\n")


    builder.append(
        "Loss Weight: "
    )

    builder.append(
        formatKg(lossWeight)
    )

    builder.append("\n")


    builder.append(
        "Variance: "
    )

    builder.append(
        formatPercentage(variance)
    )

    builder.append("\n\n")


    // ========================================================
    // STATUS
    // ========================================================

    builder.append(
        "====================================\n"
    )

    builder.append(
        "STATUS: CONFIRMED\n"
    )

    builder.append(
        "====================================\n\n"
    )

    builder.append(
        "This result was confirmed through the\n"
    )

    builder.append(
        "Jamipoy's Enterprises application."
    )


    return builder.toString()
}


// ============================================================
// WELCOME SCREEN
// ============================================================

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit
) {

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 30.dp,
                vertical = 30.dp
            )
            .navigationBarsPadding(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.SpaceBetween
    ) {

        Spacer(
            modifier = Modifier.height(1.dp)
        )


        Text(

            text =
                "Welcome to Jamipoy's\nEnterprises",

            color =
                Color(0xFF1F5E43),

            fontSize =
                30.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.Center,

            lineHeight =
                38.sp
        )


        Button(

            onClick =
                onStartClick,

            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 20.dp),

            shape =
                RoundedCornerShape(14.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        Color(0xFF1F5E43),
                    contentColor =
                        Color.White
                )
        ) {

            Text(

                text = "START",

                fontSize = 18.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// ============================================================
// INPUT SCREEN
// ============================================================

@Composable
fun InputScreen(

    consigneeName: String,
    onConsigneeNameChange: (String) -> Unit,

    stsDrNumber: String,
    onStsDrNumberChange: (String) -> Unit,

    deliveryDate: String,
    onDeliveryDateChange: (String) -> Unit,

    plateNumber: String,
    onPlateNumberChange: (String) -> Unit,

    truckTemp: String,
    onTruckTempChange: (String) -> Unit,

    sealNumber: String,
    onSealNumberChange: (String) -> Unit,

    totalPlantKilos: String,
    onTotalPlantKilosChange: (String) -> Unit,

    receivedKilosItems: MutableList<String>,

    plantHds: String,
    onPlantHdsChange: (String) -> Unit,

    totalHdsReceived: String,
    onTotalHdsReceivedChange: (String) -> Unit,

    onSubmitClick: () -> Unit
) {

    val totalReceivedKilos =
        receivedKilosItems.sumOf {

            it.toDoubleOrNull()
                ?: 0.0
        }


    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 30.dp,
                bottom = 30.dp
            )
            .navigationBarsPadding(),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(

            text = "Production Details",

            color =
                Color(0xFF1F5E43),

            fontSize =
                28.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.Center
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        Text(

            text =
                "Enter all required information",

            color =
                Color.Gray,

            fontSize =
                15.sp,

            textAlign =
                TextAlign.Center
        )


        Spacer(
            modifier = Modifier.height(25.dp)
        )


        SectionTitle(
            text =
                "Delivery Information (Optional)"
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        TextInputField(

            value =
                consigneeName,

            onValueChange =
                onConsigneeNameChange,

            label =
                "CONSIGNEE NAME",

            placeholder =
                "Enter consignee name"
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        TextInputField(

            value =
                stsDrNumber,

            onValueChange =
                onStsDrNumberChange,

            label =
                "STS/DR#",

            placeholder =
                "Enter STS/DR number"
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        TextInputField(

            value =
                deliveryDate,

            onValueChange =
                onDeliveryDateChange,

            label =
                "DELIVERY DATE",

            placeholder =
                "Enter delivery date"
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        TextInputField(

            value =
                plateNumber,

            onValueChange =
                onPlateNumberChange,

            label =
                "PLATE NUMBER",

            placeholder =
                "Enter plate number"
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        DecimalInputField(

            value =
                truckTemp,

            onValueChange =
                onTruckTempChange,

            label =
                "TRUCK TEMP",

            placeholder =
                "Example: -2.50 °C",

            allowNegative =
                true
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        TextInputField(

            value =
                sealNumber,

            onValueChange =
                onSealNumberChange,

            label =
                "SEAL#",

            placeholder =
                "Enter seal number"
        )


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        SectionTitle(
            text =
                "Production Information"
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        InputLabel(
            text =
                "Total Plant Kilos"
        )


        DecimalInputField(

            value =
                totalPlantKilos,

            onValueChange =
                onTotalPlantKilosChange,

            placeholder =
                "Enter total plant kilos",

            allowNegative =
                false
        )


        Spacer(
            modifier = Modifier.height(22.dp)
        )


        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(

                text =
                    "Received Kilos Per Item",

                color =
                    Color(0xFF1F5E43),

                fontSize =
                    17.sp,

                fontWeight =
                    FontWeight.Bold
            )


            TextButton(

                onClick = {
                    receivedKilosItems.add("")
                }

            ) {

                Text(

                    text =
                        "+ ADD ITEM",

                    color =
                        Color(0xFF1F5E43),

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        receivedKilosItems.forEachIndexed {
                index,
                value ->

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                DecimalInputField(

                    value =
                        value,

                    onValueChange = {
                        receivedKilosItems[index] =
                            it
                    },

                    label =
                        "Item ${index + 1}",

                    placeholder =
                        "Enter kilos",

                    modifier =
                        Modifier.weight(1f),

                    allowNegative =
                        false
                )


                Spacer(
                    modifier = Modifier.width(8.dp)
                )


                if (
                    receivedKilosItems.size > 1
                ) {

                    TextButton(

                        onClick = {

                            receivedKilosItems
                                .removeAt(index)
                        }

                    ) {

                        Text(

                            text =
                                "REMOVE",

                            color =
                                Color(0xFFB3261E),

                            fontSize =
                                12.sp
                        )
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }


        Card(

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(14.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color(0xFFE8F2ED)
                )
        ) {

            Column(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
            ) {

                Text(

                    text =
                        "Total Received Kilos",

                    color =
                        Color(0xFF1F5E43),

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Medium
                )


                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )


                Text(

                    text =
                        formatKg(
                            totalReceivedKilos
                        ),

                    color =
                        Color(0xFF1F5E43),

                    fontSize =
                        24.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(22.dp)
        )


        InputLabel(
            text =
                "Plant HDS"
        )


        DecimalInputField(

            value =
                plantHds,

            onValueChange =
                onPlantHdsChange,

            placeholder =
                "Enter plant HDS",

            allowNegative =
                false
        )


        Spacer(
            modifier =
                Modifier.height(22.dp)
        )


        InputLabel(
            text =
                "Total HDS Received"
        )


        DecimalInputField(

            value =
                totalHdsReceived,

            onValueChange =
                onTotalHdsReceivedChange,

            placeholder =
                "Enter total HDS received",

            allowNegative =
                false
        )


        Spacer(
            modifier =
                Modifier.height(32.dp)
        )


        Button(

            onClick =
                onSubmitClick,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(60.dp),

            shape =
                RoundedCornerShape(14.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        Color(0xFF1F5E43),
                    contentColor =
                        Color.White
                )
        ) {

            Text(

                text =
                    "SUBMIT",

                fontSize =
                    18.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier =
                Modifier.height(10.dp)
        )
    }
}


// ============================================================
// CALCULATION SCREEN
// ============================================================

@Composable
fun CalculationScreen(

    totalPlantKilos: Double,
    totalReceivedKilos: Double,
    lossWeight: Double,
    variance: Double,

    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(24.dp)
                .navigationBarsPadding(),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(

            text =
                "Calculation",

            color =
                Color(0xFF1F5E43),

            fontSize =
                30.sp,

            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(30.dp)
        )


        CalculationCard(

            title =
                "Total Plant Kilos",

            value =
                formatKg(
                    totalPlantKilos
                )
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        Text(

            text =
                "−",

            color =
                Color.Gray,

            fontSize =
                28.sp,

            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(5.dp)
        )


        CalculationCard(

            title =
                "Total Received Kilos",

            value =
                formatKg(
                    totalReceivedKilos
                )
        )


        Spacer(
            modifier =
                Modifier.height(15.dp)
        )


        Text(

            text =
                "=",

            color =
                Color.Gray,

            fontSize =
                28.sp,

            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(5.dp)
        )


        Card(

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(16.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color(0xFFE8F2ED)
                )
        ) {

            Column(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(22.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(

                    text =
                        "Loss Weight",

                    color =
                        Color(0xFF1F5E43),

                    fontSize =
                        16.sp,

                    fontWeight =
                        FontWeight.Medium
                )


                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )


                Text(

                    text =
                        formatKg(
                            lossWeight
                        ),

                    color =
                        Color(0xFF1F5E43),

                    fontSize =
                        30.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(30.dp)
        )


        Text(

            text =
                "Variance Calculation",

            color =
                Color(0xFF1F5E43),

            fontSize =
                20.sp,

            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(15.dp)
        )


        Text(

            text =
                "(Loss Weight ÷ Total Plant Kilos) × 100",

            color =
                Color.DarkGray,

            fontSize =
                16.sp,

            textAlign =
                TextAlign.Center
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        Text(

            text =
                String.format(

                    Locale.US,

                    "(%.2f ÷ %.2f) × 100",

                    lossWeight,

                    totalPlantKilos
                ),

            color =
                Color.Gray,

            fontSize =
                15.sp,

            textAlign =
                TextAlign.Center
        )


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        Card(

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(16.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color(0xFF1F5E43)
                )
        ) {

            Column(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(

                    text =
                        "Variance",

                    color =
                        Color.White,

                    fontSize =
                        17.sp
                )


                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )


                Text(

                    text =
                        formatPercentage(
                            variance
                        ),

                    color =
                        Color.White,

                    fontSize =
                        36.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(35.dp)
        )


        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            TextButton(

                onClick =
                    onBackClick,

                modifier =
                    Modifier.weight(1f)
            ) {

                Text(

                    text =
                        "BACK",

                    color =
                        Color(0xFF1F5E43),

                    fontWeight =
                        FontWeight.Bold
                )
            }


            Button(

                onClick =
                    onContinueClick,

                modifier =
                    Modifier
                        .weight(1f)
                        .height(55.dp),

                shape =
                    RoundedCornerShape(12.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFF1F5E43)
                    )
            ) {

                Text(

                    text =
                        "CONTINUE",

                    color =
                        Color.White,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}


// ============================================================
// RESULT SCREEN
// ============================================================

@Composable
fun ResultScreen(

    consigneeName: String,
    stsDrNumber: String,
    deliveryDate: String,
    plateNumber: String,
    truckTemp: String,
    sealNumber: String,

    totalPlantKilos: Double,

    receivedKilosItems: MutableList<String>,

    totalReceivedKilos: Double,

    plantHds: Double,
    totalHdsReceived: Double,

    lossWeight: Double,
    variance: Double,

    onNewEntryClick: () -> Unit,

    onConfirmClick: () -> Unit
) {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(24.dp)
                .navigationBarsPadding(),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(

            text =
                "RESULTS",

            color =
                Color(0xFF1F5E43),

            fontSize =
                30.sp,

            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        SectionTitle(
            text =
                "Delivery Information"
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        ResultRow(

            label =
                "CONSIGNEE NAME",

            value =
                displayOptional(
                    consigneeName
                )
        )


        ResultRow(

            label =
                "STS/DR#",

            value =
                displayOptional(
                    stsDrNumber
                )
        )


        ResultRow(

            label =
                "DELIVERY DATE",

            value =
                displayOptional(
                    deliveryDate
                )
        )


        ResultRow(

            label =
                "PLATE NUMBER",

            value =
                displayOptional(
                    plateNumber
                )
        )


        ResultRow(

            label =
                "TRUCK TEMP",

            value =

                if (
                    truckTemp.isBlank()
                ) {

                    "—"

                } else {

                    "${
                        formatNumber(
                            truckTemp.toDoubleOrNull()
                                ?: 0.0
                        )
                    } °C"
                }
        )


        ResultRow(

            label =
                "SEAL#",

            value =
                displayOptional(
                    sealNumber
                )
        )


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        SectionTitle(
            text =
                "Receipt Summary"
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        ResultRow(

            label =
                "Total Plants Kilos",

            value =
                formatKg(
                    totalPlantKilos
                )
        )


        Card(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.dp
                    ),

            shape =
                RoundedCornerShape(12.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                )
        ) {

            Column(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
            ) {

                Text(

                    text =
                        "Total Received Kilos Per Item",

                    color =
                        Color(0xFF1F5E43),

                    fontSize =
                        16.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )


                receivedKilosItems.forEachIndexed {
                        index,
                        itemValue ->

                    val itemKilos =
                        itemValue.toDoubleOrNull()
                            ?: 0.0


                    Row(

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 5.dp
                                ),

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(

                            text =
                                "Item ${index + 1}",

                            color =
                                Color.DarkGray,

                            fontSize =
                                15.sp
                        )


                        Text(

                            text =
                                formatKg(
                                    itemKilos
                                ),

                            color =
                                Color(0xFF1F5E43),

                            fontSize =
                                15.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )


                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(

                        text =
                            "Total Received",

                        color =
                            Color.DarkGray,

                        fontSize =
                            15.sp,

                        fontWeight =
                            FontWeight.Bold
                    )


                    Text(

                        text =
                            formatKg(
                                totalReceivedKilos
                            ),

                        color =
                            Color(0xFF1F5E43),

                        fontSize =
                            16.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }


        ResultRow(

            label =
                "Plant HDS",

            value =
                formatNumber(
                    plantHds
                )
        )


        ResultRow(

            label =
                "Total HDS Received",

            value =
                formatNumber(
                    totalHdsReceived
                )
        )


        ResultRow(

            label =
                "Loss Weight",

            value =
                formatKg(
                    lossWeight
                )
        )


        Card(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.dp
                    ),

            shape =
                RoundedCornerShape(14.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color(0xFFE8F2ED)
                )
        ) {

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(

                    text =
                        "Variance",

                    color =
                        Color(0xFF1F5E43),

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Text(

                    text =
                        formatPercentage(
                            variance
                        ),

                    color =
                        Color(0xFF1F5E43),

                    fontSize =
                        20.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        // ====================================================
        // NEW ENTRY + CONFIRM BUTTONS
        // ====================================================

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            // -----------------------------------------------
            // NEW ENTRY
            // -----------------------------------------------

            Button(

                onClick =
                    onNewEntryClick,

                modifier =
                    Modifier
                        .weight(1f)
                        .height(60.dp),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            Color.White,

                        contentColor =
                            Color(0xFF1F5E43)
                    )
            ) {

                Text(

                    text =
                        "NEW ENTRY",

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            // -----------------------------------------------
            // CONFIRM
            // -----------------------------------------------

            Button(

                onClick =
                    onConfirmClick,

                modifier =
                    Modifier
                        .weight(1f)
                        .height(60.dp),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            Color(0xFF1F5E43),

                        contentColor =
                            Color.White
                    )
            ) {

                Text(

                    text =
                        "CONFIRM",

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(10.dp)
        )
    }
}


// ============================================================
// SECTION TITLE
// ============================================================

@Composable
fun SectionTitle(
    text: String
) {

    Text(

        text =
            text,

        modifier =
            Modifier.fillMaxWidth(),

        color =
            Color(0xFF1F5E43),

        fontSize =
            20.sp,

        fontWeight =
            FontWeight.Bold
    )
}


// ============================================================
// INPUT LABEL
// ============================================================

@Composable
fun InputLabel(
    text: String
) {

    Text(

        text =
            text,

        modifier =
            Modifier.fillMaxWidth(),

        color =
            Color(0xFF1F5E43),

        fontSize =
            17.sp,

        fontWeight =
            FontWeight.Bold
    )


    Spacer(
        modifier =
            Modifier.height(8.dp)
    )
}


// ============================================================
// NORMAL TEXT INPUT
// ============================================================

@Composable
fun TextInputField(

    value: String,

    onValueChange:
        (String) -> Unit,

    label: String,

    placeholder: String
) {

    OutlinedTextField(

        value =
            value,

        onValueChange =
            onValueChange,

        modifier =
            Modifier.fillMaxWidth(),

        label = {

            Text(
                text =
                    label
            )
        },

        placeholder = {

            Text(
                text =
                    placeholder
            )
        },

        singleLine =
            true,

        shape =
            RoundedCornerShape(12.dp)
    )
}


// ============================================================
// DECIMAL INPUT FIELD
// ============================================================

@Composable
fun DecimalInputField(

    value: String,

    onValueChange:
        (String) -> Unit,

    placeholder: String,

    label: String? = null,

    modifier: Modifier =
        Modifier,

    allowNegative: Boolean =
        false
) {

    val positivePattern =
        Regex(
            "^\\d*(\\.\\d{0,2})?$"
        )

    val negativePattern =
        Regex(
            "^-?\\d*(\\.\\d{0,2})?$"
        )


    val pattern =

        if (allowNegative) {

            negativePattern

        } else {

            positivePattern
        }


    OutlinedTextField(

        value =
            value,

        onValueChange = { newValue ->

            if (
                newValue.isEmpty()
            ) {

                onValueChange(
                    newValue
                )

                return@OutlinedTextField
            }


            if (
                allowNegative &&
                newValue == "-"
            ) {

                onValueChange(
                    newValue
                )

                return@OutlinedTextField
            }


            if (
                newValue.matches(
                    pattern
                )
            ) {

                onValueChange(
                    newValue
                )
            }
        },

        modifier =
            modifier.fillMaxWidth(),

        label =

            if (
                label != null
            ) {

                {

                    Text(
                        text =
                            label
                    )
                }

            } else {

                null
            },

        placeholder = {

            Text(
                text =
                    placeholder
            )
        },

        singleLine =
            true,

        keyboardOptions =
            KeyboardOptions(

                keyboardType =
                    KeyboardType.Decimal
            ),

        shape =
            RoundedCornerShape(12.dp)
    )
}


// ============================================================
// CALCULATION CARD
// ============================================================

@Composable
fun CalculationCard(

    title: String,

    value: String
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(14.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(

                text =
                    title,

                color =
                    Color.Gray,

                fontSize =
                    14.sp
            )


            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )


            Text(

                text =
                    value,

                color =
                    Color(0xFF1F5E43),

                fontSize =
                    22.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// ============================================================
// RESULT ROW
// ============================================================

@Composable
fun ResultRow(

    label: String,

    value: String
) {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 10.dp
                ),

        shape =
            RoundedCornerShape(12.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(

                text =
                    label,

                color =
                    Color.DarkGray,

                fontSize =
                    15.sp
            )


            Text(

                text =
                    value,

                color =
                    Color(0xFF1F5E43),

                fontSize =
                    16.sp,

                fontWeight =
                    FontWeight.Bold,

                textAlign =
                    TextAlign.End
            )
        }
    }
}


// ============================================================
// OPTIONAL VALUE DISPLAY
// ============================================================

fun displayOptional(
    value: String
): String {

    return if (
        value.isBlank()
    ) {

        "—"

    } else {

        value
    }
}


// ============================================================
// FORMAT NUMBER
// ============================================================

fun formatNumber(
    value: Double
): String {

    return String.format(

        Locale.US,

        "%.2f",

        value
    )
}


// ============================================================
// FORMAT KILOS
// ============================================================

fun formatKg(
    value: Double
): String {

    return String.format(

        Locale.US,

        "%.2f kg",

        value
    )
}


// ============================================================
// FORMAT PERCENTAGE
// ============================================================

fun formatPercentage(
    value: Double
): String {

    return String.format(

        Locale.US,

        "%.2f%%",

        value
    )
}