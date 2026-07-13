package com.enesguneri.flickdrop.presentation.setup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enesguneri.flickdrop.R

@Composable
fun SetupScreen(
    onNavigateToDiscovery: (String) -> Unit,
    viewModel: SetupViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // ViewModel'daki StateFlow'u Compose'un anlayacağı State'e dönüştürüyoruz
    // collectAsStateWithLifecycle(), uygulama arka plana atıldığında dinlemeyi durdurarak pil tasarrufu sağlar
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Başlık
        Text(
            text = stringResource(id = R.string.setup_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Alt Başlık
        Text(
            text = stringResource(id = R.string.setup_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // İsim Giriş Alanı
        OutlinedTextField(
            value = state.displayName,
            onValueChange = { newName ->
                viewModel.onEvent(SetupEvent.OnNameChanged(newName))
            },
            label = { Text(stringResource(id = R.string.setup_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Başlama Butonu
        Button(
            onClick = {
                viewModel.onEvent(SetupEvent.OnStartClicked)
                // İsim alanındaki metni alıp Radar ekranına geçiş tetikleyicisini çalıştırıyoruz
                onNavigateToDiscovery(state.displayName.trim())
            },
            enabled = state.isButtonEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.setup_button))
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Dil Değiştirme Butonları
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(
                onClick = { viewModel.onEvent(SetupEvent.OnLanguageChanged("tr")) }
            ) {
                Text(stringResource(id = R.string.language_tr))
            }
            TextButton(
                onClick = { viewModel.onEvent(SetupEvent.OnLanguageChanged("en")) }
            ) {
                Text(stringResource(id = R.string.language_en))
            }
        }
    }
}