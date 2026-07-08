package com.enesguneri.flickdrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.enesguneri.flickdrop.presentation.setup.SetupScreen
import com.enesguneri.flickdrop.ui.theme.FlickDropTheme

class MainActivity : AppCompatActivity() {
    // AppCompat kullanılmasının nedeni dil değiştirme işleminin android 13'ten sonra
    // sadece ayarlardan değiştirilebilir hale gelmesiyle bizim manuel yapmak istememiz.
    // Bu işlemin de eski sürümlerle uyumlu olması için AppCompat yapılması gerekir.

    // Jetpack Compose'da Fragment'lar yerini @Composable fonksiyonlara bırakmıştır.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlickDropTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupScreen()
                }
            }
        }
    }
}
