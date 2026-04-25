package com.example.firstappandroidstudio.lapteva.reimer.programintent

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstappandroidstudio.lapteva.reimer.programintent.ui.theme.ProgramIntentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProgramIntentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactScreen()
                }
            }
        }
    }
}

@Composable
fun ContactScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.contact_book),
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )
        ActionButton(
            text = stringResource(R.string.btn_call),
            onClick = { callPhone(context) }
        )

        ActionButton(
            text = stringResource(R.string.btn_email),
            onClick = { sendEmail(context) }
        )

        ActionButton(
            text = stringResource(R.string.btn_map),
            onClick = { showOnMap(context) }
        )

        ActionButton(
            text = stringResource(R.string.btn_share),
            onClick = { shareContact(context) }
        )
    }
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(56.dp)
    ) {
        Text(text = text, fontSize = 18.sp)
    }
}

private fun callPhone(context: Context) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:${context.getString(R.string.phone_number)}")
    }
    startIntentSafe(context, intent, context.getString(R.string.dialing_number))
}

private fun sendEmail(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.email_address)))
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
    }
    startIntentSafe(context, intent, context.getString(R.string.sending_email))
}

private fun showOnMap(context: Context) {
    val lat = 60.0237
    val lon = 30.2289
    val label = context.getString(R.string.office_label)
    val geoUri = Uri.parse("geo:0,0?q=$lat,$lon($label)")
    val intent = Intent(Intent.ACTION_VIEW, geoUri)
    startIntentSafe(context, intent, context.getString(R.string.open_the_card))
}

private fun shareContact(context: Context) {
    val text = """
        Контакт:
        Телефон: ${context.getString(R.string.phone_number)}
        Email: ${context.getString(R.string.email_address)}
    """.trimIndent()

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }

    val chooser = Intent.createChooser(sendIntent, context.getString(R.string.share_title))
    startIntentSafe(context, chooser, context.getString(R.string.share))
}

private fun startIntentSafe(context: Context, intent: Intent, actionName: String) {
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "${context.getString(R.string.no_app_found)} для $actionName", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun ContactScreenPreview() {
    ProgramIntentTheme {
        ContactScreen()
    }
}