package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dev.samstevens.totp.code.CodeGenerator
import dev.samstevens.totp.code.DefaultCodeGenerator
import dev.samstevens.totp.time.SystemTimeProvider
import dev.samstevens.totp.time.TimeProvider
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


val Context.dataStore by preferencesDataStore(name = "totp_prefs")
val TOTP_KEY = stringPreferencesKey("secret_totp_chiffre")

val ID_KEY = stringPreferencesKey("totp_user_id")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                MainAppScreen()
            }
        }
    }
}

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun MainAppScreen(){
    val audioManager = LocalContext.current.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val isWorking = audioManager.getProperty(AudioManager.PROPERTY_SUPPORT_SPEAKER_NEAR_ULTRASOUND) == "true"
    Log.d("isWorking", isWorking.toString())
    val timeProvider: TimeProvider = SystemTimeProvider()
    val codeGenerator: CodeGenerator = DefaultCodeGenerator()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val ggwaveManager = remember { GgwaveManager() }

    val savedSecretState = context.dataStore.data
        .map { preferences -> preferences[TOTP_KEY] }
        .collectAsState(initial = null)

    val savedIdState = context.dataStore.data
        .map { preferences -> preferences[ID_KEY] }
        .collectAsState(initial = null)
    val currentId = savedIdState.value
    val currentSecret = savedSecretState.value
    if (!currentSecret.isNullOrEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Statut : Connecté (Secret sauvegardé)")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val totpCode = codeGenerator.generate(currentSecret, timeProvider.time)

                val payloadToSend = "$totpCode$currentId"

                ggwaveManager.playTextByAudio(payloadToSend)

            }) {
                Text("Badger")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                coroutineScope.launch {
                    context.dataStore.edit { preferences ->
                        preferences.remove(TOTP_KEY)
                        preferences.remove(ID_KEY)
                    }
                    Toast.makeText(context, "Déconnecter !", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Se déconnecter")
            }
        }
    } else {
        Button(onClick = {
            val options = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
                .build()

            val scanner = GmsBarcodeScanning.getClient(context, options)

            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    barcode.rawValue?.let { scannedValue ->
                        val segments = scannedValue.split(",")

                        val extractedId = segments.find { it.startsWith("id=") }?.substringAfter("id=")
                        val extractedSecret = segments.find { it.startsWith("secret=") }?.substringAfter("secret=")

                        if (extractedId != null && extractedSecret != null) {
                            coroutineScope.launch {
                                context.dataStore.edit { preferences ->
                                    preferences[TOTP_KEY] = extractedSecret
                                    preferences[ID_KEY] = extractedId
                                }
                                Toast.makeText(context, "Connecté avec succès !", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Format QR invalide (id ou secret manquant)", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                .addOnCanceledListener {
                    Toast.makeText(context, "Scan canceled", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }) {
            Text("Scan QR Code")
        }
    }
}