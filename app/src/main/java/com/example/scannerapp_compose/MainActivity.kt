package com.example.scannerapp_compose

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.scannerapp_compose.ui.theme.ScannerApp_ComposeTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : ComponentActivity() {

    private var textResult = mutableStateOf("")

    private val barCodeLauncher = registerForActivityResult(ScanContract()){
        result->
        if (result.contents == null){
            Toast.makeText(this,"cancelled",Toast.LENGTH_SHORT).show()
        }
        else{
            textResult.value = result.contents
        }

    }

  private   fun showCamera(){
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a QR code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setOrientationLocked(false)

        barCodeLauncher.launch(options)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        isGranted->
        run {
            if (isGranted) {
                showCamera()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScannerApp_ComposeTheme {
                // A surface container using the 'background' color from the theme
              Scaffold(
                  bottomBar = {
                      BottomAppBar(actions = {}, floatingActionButton = {
                          FloatingActionButton(onClick = { checkCameraPermission(this)}) {
                              Icon(painter = painterResource(id = R.drawable.scanner),
                                  contentDescription ="Qr Scan" )

                          }
                      })
                  }

                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(painter = painterResource(id = R.drawable.scanner), contentDescription =null,
                            modifier = Modifier.size(100.dp))
                        
                        Text(text = textResult.value, fontSize = 30.sp, fontWeight = FontWeight.Bold)

                    }

                }
            }
        }
    }
    private fun checkCameraPermission(context: Context){
        if(ContextCompat.checkSelfPermission(context,android.Manifest.permission.CAMERA)==
            PackageManager.PERMISSION_GRANTED){
            showCamera()
        }else if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
            Toast.makeText(this,"Camera required",Toast.LENGTH_SHORT).show()
        }
        else{
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }

    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScannerApp_ComposeTheme {
    }
}