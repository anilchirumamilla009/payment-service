# Payment Service - System Time Greeting Script
# Detects current system time and displays appropriate greeting

$hour = (Get-Date).Hour

if ($hour -ge 6 -and $hour -lt 12) {
    $greeting = "Good morning! ☀️"
    $timeOfDay = "morning"
} elseif ($hour -ge 12 -and $hour -lt 18) {
    $greeting = "Good afternoon! 🌤️"
    $timeOfDay = "afternoon"
} elseif ($hour -ge 18 -and $hour -lt 24) {
    $greeting = "Good evening! 🌙"
    $timeOfDay = "evening"
} else {
    $greeting = "Late night coding session! 🌃"
    $timeOfDay = "night"
}

$currentTime = Get-Date -Format "HH:mm:ss"

Write-Host "$greeting" -ForegroundColor Cyan
Write-Host "Current time: $currentTime" -ForegroundColor Gray
Write-Host ""
Write-Host "Welcome to payment-service." -ForegroundColor White
Write-Host ""
Write-Host "I'm here to help you with:" -ForegroundColor Yellow
Write-Host "  • Implementing features and business logic"
Write-Host "  • Debugging and fixing issues"
Write-Host "  • Writing and improving tests"
Write-Host "  • Reviewing code and validating compliance"
Write-Host "  • Designing APIs and data models"
Write-Host "  • Security audits and optimization"
Write-Host "  • Architecture planning and design"
Write-Host ""
Write-Host "What would you like to work on?" -ForegroundColor Green
