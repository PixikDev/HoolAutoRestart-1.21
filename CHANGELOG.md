# Создаем CHANGELOG.md
@"
# Changelog

All notable changes to HoolAutoRestart will be documented in this file.

## [1.0.0] - $(Get-Date -Format "yyyy-MM-dd")

### 🎉 Initial Release

#### Added
- ✅ Automatic server restart on shutdown commands
- ✅ GitHub update checking system
- ✅ Cross-platform restart scripts (Windows/Linux/Mac)
- ✅ Operator notifications for new versions
- ✅ Safe world saving before restart
- ✅ Configurable update check interval

#### Technical
- 📦 Built for Minecraft 1.21
- ☕ Java 21 compatibility
- 🐛 Fixed version checking algorithm
- 📚 Comprehensive documentation

### 🚀 Features
- **Auto-Restart**: Intercepts stop commands and automatically restarts
- **Update Checks**: Regular GitHub API checks for new versions
- **Smart Notifications**: Alerts server operators about updates
- **Safe Operations**: Proper world saving and player kicking

---
*For upgrade instructions, please see [README.md](README.md)
"@ | Out-File -FilePath CHANGELOG.md -Encoding UTF8
