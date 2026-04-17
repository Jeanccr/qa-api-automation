#!/bin/bash

# Script para executar testes da Dog API

echo "========================================"
echo "Dog API - Testes Automatizados"
echo "========================================"
echo ""

# Limpar e compilar
echo "[1/3] Limpando e compilando projeto..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "Erro na compilacao!"
    exit 1
fi

echo ""
echo "[2/3] Executando todos os testes..."
mvn test

echo ""
echo "[3/3] Gerando relatorios HTML..."
python3 generate_report.py
if [ $? -ne 0 ]; then
    echo "AVISO: Erro ao gerar relatorio HTML. Verifique se Python 3 esta instalado."
fi

echo ""
echo "========================================"
echo "Testes concluidos com SUCESSO!"
echo "========================================"
echo ""

# Aguardar um pouco para garantir que os arquivos foram criados
sleep 2

# Obter caminho absoluto
REPORTS_PATH="$(pwd)/target/surefire-reports"

# Abrir em diferentes sistemas operacionais
if [ -f "$REPORTS_PATH/index.html" ]; then
    echo ""
    echo "Abrindo relatorio HTML..."
    
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        open "$REPORTS_PATH/index.html"
        echo "Relatorio aberto!"
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Linux
        xdg-open "$REPORTS_PATH/index.html" 2>/dev/null
        if [ $? -eq 0 ]; then
            echo "Relatorio aberto!"
        else
            echo "AVISO: Não foi possível abrir o relatorio automaticamente"
            echo "Abra manualmente: $REPORTS_PATH/index.html"
        fi
    fi
elif [ -d "$REPORTS_PATH" ]; then
    echo ""
    echo "Abrindo pasta de relatorios..."
    
    if [[ "$OSTYPE" == "darwin"* ]]; then
        open "$REPORTS_PATH"
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        xdg-open "$REPORTS_PATH" 2>/dev/null
    fi
    echo "Pasta aberta em: $REPORTS_PATH"
else
    echo ""
    echo "AVISO: Nenhum relatorio encontrado"
fi

echo ""
echo "Relatório disponível em: target/surefire-reports/"
