@echo off
REM Script para executar testes da Dog API

echo ========================================
echo Dog API - Testes Automatizados
echo ========================================
echo.

REM Compilar
echo [1/2] Compilando projeto...
mvn compile
if %ERRORLEVEL% NEQ 0 (
    echo Erro na compilacao!
    exit /b 1
)

echo.
echo [2/2] Executando todos os testes...
mvn test

echo.
echo [3/3] Gerando relatorios HTML...
python generate_report.py
if %ERRORLEVEL% NEQ 0 (
    echo AVISO: Erro ao gerar relatorio HTML.
    pause
    exit /b 1
)

echo.
echo ========================================
echo Testes concluidos com SUCESSO!
echo ========================================
echo.
echo Abrindo relatorio em 3 segundos...
timeout /t 3 /nobreak

REM Abrir relatório HTML
set REPORTS_PATH=%cd%\target\surefire-reports\index.html

if exist "%REPORTS_PATH%" (
    echo.
    echo Abrindo: %REPORTS_PATH%
    explorer "%REPORTS_PATH%"
    echo ✓ Relatorio aberto!
) else (
    echo.
    echo ✗ ERRO: Arquivo nao encontrado: %REPORTS_PATH%
    pause
)
