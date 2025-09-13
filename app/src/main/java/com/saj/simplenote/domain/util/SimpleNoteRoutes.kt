package com.saj.simplenote.domain.util

enum class SimpleNoteRoutes (
    val route: String,
) {
    LoginScreen("login"),
    RegisterScreen("register"),
    SplashScreen("splash_screen"),
    OnboardingScreen("onboarding"),
    HomeScreen("home"),
    NoteScreen("note"),
    ReservationInfoScreen("reservation_info"),
    ReservableScreen("reservable"),
    ProfileScreen("profile"),
    DailySaleScreen("daily_sale"),
    AutomaticReservationScreen("automatic_reservation"),
    SettingsScreen("settings"),
    ChangePasswordScreen("change_password"),
    FoodPriorityScreen("food_priority"),
}