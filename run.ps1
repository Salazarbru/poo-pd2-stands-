$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

$files = Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName }
javac -d out -encoding UTF-8 $files
if ($LASTEXITCODE -ne 0) {
    Write-Host "Error de compilacion." -ForegroundColor Red
    exit $LASTEXITCODE
}

java -cp out com.filey.cli.Main
