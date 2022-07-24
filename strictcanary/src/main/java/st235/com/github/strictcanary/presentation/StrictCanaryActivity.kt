package st235.com.github.strictcanary.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import st235.com.github.strictcanary.data.StrictPolicyViolation
import st235.com.github.strictcanary.presentation.ui.theme.StrictCanaryTheme

class StrictCanaryActivity : ComponentActivity() {

    companion object {

        private const val ARGS_KEY_VIOLATION = "args.violation"

        fun createIntent(context: Context, strictPolicyViolation: StrictPolicyViolation): Intent {
            val intent = Intent(context, StrictCanaryActivity::class.java)
            intent.putExtra(ARGS_KEY_VIOLATION, strictPolicyViolation)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            return intent
        }

        private fun extractViolationFromIntent(intent: Intent): StrictPolicyViolation {
            return intent.getParcelableExtra(ARGS_KEY_VIOLATION) ?:
                throw IllegalStateException("This activity cannot be started without violation info")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StrictCanaryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StrictCanaryTheme {
        Greeting("Android")
    }
}