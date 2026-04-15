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
echo [3/3] Gerando relatorio Allure...
mvn allure:report

echo.
echo ========================================
echo Testes concluidos!
echo Para abrir o relatorio: mvn allure:serve
echo ========================================
