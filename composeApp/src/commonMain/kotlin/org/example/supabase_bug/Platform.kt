package org.example.supabase_bug

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform