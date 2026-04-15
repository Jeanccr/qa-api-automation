@echo off
REM Script para executar testes da Dog API

echo ========================================
echo Dog API - Testes Automatizados
echo ========================================
echo.

REM Limpar e compilar
echo [1/3] Limpando e compilando projeto...
mvn clean compile
if %ERRORLEVEL% NEQ 0 (
    echo Erro na compilacao!
    exit /b 1
)

echo.
echo [2/3] Executando todos os testes...
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
    start "%REPORTS_PATH%"
    echo ✓ Relatorio aberto!
) else (
    echo.
    echo ✗ ERRO: Arquivo nao encontrado: %REPORTS_PATH%
    pause
)
