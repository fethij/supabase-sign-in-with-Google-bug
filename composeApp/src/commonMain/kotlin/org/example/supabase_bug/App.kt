package org.example.supabase_bug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.appleNativeLogin
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithApple
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    val client = remember { client }
    val composeAuth = remember { client.composeAuth }

    val googleSignIn =
        composeAuth.rememberSignInWithGoogle(onResult = { result -> //optional error handling
            when (result) {
                is NativeSignInResult.Success -> {
                    println("#### Successful sign in")
                }

                is NativeSignInResult.ClosedByUser -> {
                    println("#### Closed by user")
                }

                is NativeSignInResult.Error -> {
                    println("#### Error occurred while signing in")
                }

                is NativeSignInResult.NetworkError -> {
                    println("#### Network error occurred while signing in")
                }
            }
        }, fallback = {
            println("#### Error occurred while signing in fallback")
//            auth.signInWith(Google)
//            auth.signInWith(Google)
        })

    val appleSignIn =
        composeAuth.rememberSignInWithApple(onResult = { result -> //optional error handling
            when (result) {
                is NativeSignInResult.Success -> {
                    println("#### Successful sign in")
                }

                is NativeSignInResult.ClosedByUser -> {
                    println("#### Closed by user")
                }

                is NativeSignInResult.Error -> {
                    println("#### Error occurred while signing in message: ${result.message}" +
                            " exception: ${result.exception}")
                }

                is NativeSignInResult.NetworkError -> {
                    println("#### Network error occurred while signing in")
                }
            }
        }, fallback = {
            println("#### Error occurred while signing in fallback")
//            auth.signInWith(Apple)
        })

    MaterialTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { googleSignIn.startFlow() }) {
                Text("Continue with Google")
            }
            Button(onClick = { appleSignIn.startFlow() }) {
                Text("Continue with Apple")
            }
        }
    }
}


val client: SupabaseClient
    get() = createSupabaseClient(
        supabaseUrl = "...",
        supabaseKey = "..."
    ) {
        defaultLogLevel = LogLevel.INFO // TODO change in production
        install(Postgrest)
        install(Auth) {
//        platformGoTrueConfig()
            flowType = FlowType.PKCE
        }
        install(Realtime)
        install(ComposeAuth) {
            appleNativeLogin()
            googleNativeLogin(
                serverClientId = "...", // TODO
            )
        }
    }