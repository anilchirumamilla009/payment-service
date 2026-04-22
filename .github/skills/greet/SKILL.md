---
name: greet
description: "Use when: user says hello, hi, greet, or greeting. Provides a friendly welcome response with context about the payment-service project and available assistance."
---

# Greet

## Overview

This skill provides a friendly greeting response when users initiate conversation with greetings like "hello," "hi," or "greet."

## Trigger Keywords

- `hello`
- `hi`
- `hey`
- `greet`
- `greeting`
- `good morning`
- `good afternoon`
- `good evening`

## Response Template

When triggered, provide:

1. **Time-based greeting** based on current time:
   - **06:00 - 11:59**: "Good morning"
   - **12:00 - 17:59**: "Good afternoon"
   - **18:00 - 23:59**: "Good evening"
   - **00:00 - 05:59**: "Good night" / "Late night coding!"
2. **Project context** briefly mentioning this is the payment-service microservice
3. **Available assistance** offering to help with key capabilities
4. **Call to action** inviting user to specify their task

## Time-Based Greeting Logic

```
Current Hour: Use to determine greeting
- Morning (6-11): "Good morning! ☀️"
- Afternoon (12-17): "Good afternoon! 🌤️"
- Evening (18-23): "Good evening! 🌙"
- Night (0-5): "Late night coding session! 🌃"
```

## Example Responses

**Morning (08:30):**
```
Good morning! ☀️ Welcome to payment-service.

I'm here to help you with:
• Implementing features and business logic
• Debugging and fixing issues
• Writing and improving tests
• Reviewing code and validating compliance
• Designing APIs and data models
• Security audits and optimization
• Architecture planning and design

What would you like to work on today?
```

**Afternoon (14:15):**
```
Good afternoon! 🌤️ Back to the payment-service.

I'm here to help you with:
• Implementing features and business logic
• Debugging and fixing issues
• Writing and improving tests
• Reviewing code and validating compliance
• Designing APIs and data models
• Security audits and optimization
• Architecture planning and design

What would you like to work on?
```

**Evening (20:45):**
```
Good evening! 🌙 Welcome to payment-service.

I'm here to help you with:
• Implementing features and business logic
• Debugging and fixing issues
• Writing and improving tests
• Reviewing code and validating compliance
• Designing APIs and data models
• Security audits and optimization
• Architecture planning and design

What's on your mind?
```

## Best Practices

- Keep greeting brief and action-oriented
- Always mention the project context
- Offer clear options for next steps

## System Time-Based Greeting Script

### PowerShell Script

Use this script to automatically detect your system time and display an appropriate greeting:

```powershell
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
```

### How to Use

1. Save the script as `./scripts/greet.ps1` in your project root
2. Run from PowerShell terminal:
   ```
   powershell -ExecutionPolicy Bypass .\scripts\greet.ps1
   ```

### Script Features

- **Automatic Time Detection**: Uses `Get-Date` to fetch current system time
- **Dynamic Greeting**: Selects greeting based on current hour
- **Color Coding**: Uses PowerShell colors for better visual feedback
- **System Time Display**: Shows exact current time in HH:mm:ss format
- **Contextual Messages**: Provides assistance options based on project needs
- Maintain professional but friendly tone
